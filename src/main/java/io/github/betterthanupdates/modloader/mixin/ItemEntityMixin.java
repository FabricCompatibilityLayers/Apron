package io.github.betterthanupdates.modloader.mixin;

import modloader.ModLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public int pickupDelay;

    @Shadow private int field_568;

    @Shadow public int age;

    @Shadow public ItemStack stack;

    public ItemEntityMixin(World arg) {
        super(arg);
    }

    /**
     * @author Risugami
     * @reason idk
     */
    @Overwrite
    public void tick() {
        super.tick();
        if (this.pickupDelay > 0) {
            --this.pickupDelay;
        }

        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;
        this.yVelocity -= 0.04F;
        if (this.world.getMaterial(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z)) == Material.LAVA) {
            this.yVelocity = 0.2000000029802322;
            this.xVelocity = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.zVelocity = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.world.playSound(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.method_1372(this.x, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0, this.z);
        this.move(this.xVelocity, this.yVelocity, this.zVelocity);
        float f1 = 0.98F;
        if (this.onGround) {
            f1 = 0.5880001F;
            int i = this.world.getBlockId(MathHelper.floor(this.x), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.z));
            if (i > 0) {
                f1 = Block.BY_ID[i].slipperiness * 0.98F;
            }
        }

        this.xVelocity *= (double)f1;
        this.yVelocity *= 0.98F;
        this.zVelocity *= (double)f1;
        if (this.onGround) {
            this.yVelocity *= -0.5;
        }

        ++this.field_568;
        ++this.age;
        if (this.age >= 6000) {
            this.remove();
        }

    }

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V"))
    private void modloader$onPlayerCollision(PlayerEntity par1, CallbackInfo ci) {
        ModLoader.OnItemPickup(par1, this.stack);
    }
}
