package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public float limbDistance;

    @Shadow public float field_1048;

    @Shadow public float field_1050;

    public LivingEntityMixin(World arg) {
        super(arg);
    }

    /**
     * @author Forge
     * @reason Some float value aren't exactly the same.
     */
    @Overwrite
    public void travel(float f, float f1) {
        if (this.method_1334()) {
            double d = this.y;
            this.movementInputToVelocity(f, f1, 0.02F);
            this.move(this.xVelocity, this.yVelocity, this.zVelocity);
            this.xVelocity *= 0.8F;
            this.yVelocity *= 0.8F;
            this.zVelocity *= 0.8F;
            this.yVelocity -= 0.02;
            if (this.field_1624 && this.method_1344(this.xVelocity, this.yVelocity + 0.6F - this.y + d, this.zVelocity)) {
                this.yVelocity = 0.3F;
            }
        } else if (this.method_1335()) {
            double d1 = this.y;
            this.movementInputToVelocity(f, f1, 0.02F);
            this.move(this.xVelocity, this.yVelocity, this.zVelocity);
            this.xVelocity *= 0.5;
            this.yVelocity *= 0.5;
            this.zVelocity *= 0.5;
            this.yVelocity -= 0.02;
            if (this.field_1624 && this.method_1344(this.xVelocity, this.yVelocity + 0.6F - this.y + d1, this.zVelocity)) {
                this.yVelocity = 0.3F;
            }
        } else {
            float f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.5460001F;
                int i = this.world.getBlockId(MathHelper.floor(this.x), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.z));
                if (i > 0) {
                    f2 = Block.BY_ID[i].slipperiness * 0.91F;
                }
            }

            float f3 = 0.1627714F / (f2 * f2 * f2);
            this.movementInputToVelocity(f, f1, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.5460001F;
                int j = this.world.getBlockId(MathHelper.floor(this.x), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.z));
                if (j > 0) {
                    f2 = Block.BY_ID[j].slipperiness * 0.91F;
                }
            }

            if (this.method_932()) {
                float f4 = 0.15F;
                if (this.xVelocity < (double)(-f4)) {
                    this.xVelocity = (double)(-f4);
                }

                if (this.xVelocity > (double)f4) {
                    this.xVelocity = (double)f4;
                }

                if (this.zVelocity < (double)(-f4)) {
                    this.zVelocity = (double)(-f4);
                }

                if (this.zVelocity > (double)f4) {
                    this.zVelocity = (double)f4;
                }

                this.fallDistance = 0.0F;
                if (this.yVelocity < -0.15) {
                    this.yVelocity = -0.15;
                }

                if (this.method_1373() && this.yVelocity < 0.0) {
                    this.yVelocity = 0.0;
                }
            }

            this.move(this.xVelocity, this.yVelocity, this.zVelocity);
            if (this.field_1624 && this.method_932()) {
                this.yVelocity = 0.2;
            }

            this.yVelocity -= 0.08;
            this.yVelocity *= 0.98F;
            this.xVelocity *= (double)f2;
            this.zVelocity *= (double)f2;
        }

        this.field_1048 = this.limbDistance;
        double d2 = this.x - this.prevX;
        double d3 = this.z - this.prevZ;
        float f5 = MathHelper.sqrt(d2 * d2 + d3 * d3) * 4.0F;
        if (f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.limbDistance += (f5 - this.limbDistance) * 0.4F;
        this.field_1050 += this.limbDistance;
    }

    /**
     * @author Forge
     * @reason
     */
    @Overwrite
    public boolean method_932() {
        int i = MathHelper.floor(this.x);
        int j = MathHelper.floor(this.boundingBox.minY);
        int k = MathHelper.floor(this.z);
        Block block = Block.BY_ID[this.world.getBlockId(i, j, k)];
        return block == null ? false : ((ForgeBlock)block).isLadder();
    }
}
