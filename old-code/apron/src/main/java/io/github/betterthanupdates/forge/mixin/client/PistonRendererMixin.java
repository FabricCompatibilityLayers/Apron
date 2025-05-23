package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_283;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.class_282;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

@Environment(EnvType.CLIENT)
@Mixin(class_282.class)
public abstract class PistonRendererMixin extends BlockEntityRenderer {
	@Shadow
	private BlockRenderManager field_1131;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render(Lnet/minecraft/class_283;DDDF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;startQuads()V"))
	private void forge$beforeBlockRender(class_283 tileentitypiston, double d, double d1, double d2, float f, CallbackInfo ci) {
		Block block = Block.BLOCKS[tileentitypiston.method_1518()];

		if (block != null && tileentitypiston.method_1519(f) < 1.0F) {
			ForgeHooksClient.beforeBlockRender(block, this.field_1131);
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render(Lnet/minecraft/class_283;DDDF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_583;method_1930()V"))
	private void forge$afterBlockRender(class_283 tileentitypiston, double d, double d1, double d2, float f, CallbackInfo ci) {
		Block block = Block.BLOCKS[tileentitypiston.method_1518()];
		ForgeHooksClient.afterBlockRender(block, this.field_1131);
	}
}
