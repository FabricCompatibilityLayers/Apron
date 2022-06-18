package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooksClient;
import net.minecraft.block.Block;
import net.minecraft.class_66;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.BlockEntityRenderDispatcher;
import net.minecraft.entity.BlockEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldPopulationRegion;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.List;

@Mixin(class_66.class)
public abstract class WorldRendererMixin {

    @Shadow public boolean field_249;

    @Shadow public static int field_230;

    @Shadow public int field_231;

    @Shadow public int field_232;

    @Shadow public int field_233;

    @Shadow public int field_234;

    @Shadow public int field_235;

    @Shadow public int field_236;

    @Shadow public boolean[] field_244;

    @Shadow public List field_224;

    @Shadow public World world;

    @Shadow private int field_225;

    @Shadow protected abstract void method_306();

    @Shadow private List field_228;

    @Shadow public boolean field_223;

    @Shadow private boolean field_227;

    @Inject(method = "method_296", at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/client/render/Tessellator;draw()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void forge$afterRenderPass(CallbackInfo ci, int var11) {
        ForgeHooksClient.afterRenderPass(var11);
    }

    /**
     * @author Eloraam
     * @reason Minecraft Forge client hooks
     */
    @Overwrite
    public void method_296() {
        if (this.field_249) {
            ++field_230;
            int i = this.field_231;
            int j = this.field_232;
            int k = this.field_233;
            int l = this.field_231 + this.field_234;
            int i1 = this.field_232 + this.field_235;
            int j1 = this.field_233 + this.field_236;

            for(int k1 = 0; k1 < 2; ++k1) {
                this.field_244[k1] = true;
            }

            Chunk.field_953 = false;
            HashSet hashset = new HashSet();
            hashset.addAll(this.field_224);
            this.field_224.clear();
            int l1 = 1;
            WorldPopulationRegion chunkcache = new WorldPopulationRegion(this.world, i - l1, j - l1, k - l1, l + l1, i1 + l1, j1 + l1);
            BlockRenderer renderblocks = new BlockRenderer(chunkcache);

            for(int i2 = 0; i2 < 2; ++i2) {
                boolean flag = false;
                boolean flag1 = false;
                boolean flag2 = false;

                for(int j2 = j; j2 < i1; ++j2) {
                    for(int k2 = k; k2 < j1; ++k2) {
                        for(int l2 = i; l2 < l; ++l2) {
                            int i3 = chunkcache.getBlockId(l2, j2, k2);
                            if (i3 > 0) {
                                if (!flag2) {
                                    flag2 = true;
                                    GL11.glNewList(this.field_225 + i2, 4864);
                                    GL11.glPushMatrix();
                                    this.method_306();
                                    float f = 1.000001F;
                                    GL11.glTranslatef((float)(-this.field_236) / 2.0F, (float)(-this.field_235) / 2.0F, (float)(-this.field_236) / 2.0F);
                                    GL11.glScalef(f, f, f);
                                    GL11.glTranslatef((float)this.field_236 / 2.0F, (float)this.field_235 / 2.0F, (float)this.field_236 / 2.0F);
                                    ForgeHooksClient.beforeRenderPass(i2);
                                    Tessellator.INSTANCE.start();
                                    Tessellator.INSTANCE.setOffset((double)(-this.field_231), (double)(-this.field_232), (double)(-this.field_233));
                                }

                                if (i2 == 0 && Block.HAS_BLOCK_ENTITY[i3]) {
                                    BlockEntity tileentity = chunkcache.getBlockEntity(l2, j2, k2);
                                    if (BlockEntityRenderDispatcher.INSTANCE.hasCustomRenderer(tileentity)) {
                                        this.field_224.add(tileentity);
                                    }
                                }

                                Block block = Block.BY_ID[i3];
                                int j3 = block.getRenderPass();

                                if (ForgeHooksClient.canRenderInPass(block, i2)) {
                                    ForgeHooksClient.beforeBlockRender(block, renderblocks);
                                    flag1 |= renderblocks.render(block, l2, j2, k2);
                                    ForgeHooksClient.afterBlockRender(block, renderblocks);
                                }
                            }
                        }
                    }
                }

                if (flag2) {
                    ForgeHooksClient.afterRenderPass(i2);
                    Tessellator.INSTANCE.draw();
                    GL11.glPopMatrix();
                    GL11.glEndList();
                    Tessellator.INSTANCE.setOffset(0.0, 0.0, 0.0);
                } else {
                    flag1 = false;
                }

                if (flag1) {
                    this.field_244[i2] = false;
                }

                if (!flag) {
                    break;
                }
            }

            HashSet hashset1 = new HashSet();
            hashset1.addAll(this.field_224);
            hashset1.removeAll(hashset);
            this.field_228.addAll(hashset1);
            hashset.removeAll(this.field_224);
            this.field_228.removeAll(hashset);
            this.field_223 = Chunk.field_953;
            this.field_227 = true;
        }
    }
}
