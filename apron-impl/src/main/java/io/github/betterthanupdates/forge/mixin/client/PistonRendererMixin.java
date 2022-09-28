package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.block.BlockEntityRenderer;
import net.minecraft.client.render.entity.block.PistonRenderer;
import net.minecraft.entity.block.PistonBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(PistonRenderer.class)
public abstract class PistonRendererMixin extends BlockEntityRenderer {
	@Shadow
	private BlockRenderer field_1131;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render(Lnet/minecraft/entity/block/PistonBlockEntity;DDDF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;start()V"))
	private void forge$beforeBlockRender(PistonBlockEntity tileentitypiston, double d, double d1, double d2, float f, CallbackInfo ci) {
		Block block = Block.BY_ID[tileentitypiston.method_1518()];

		if (block != null && tileentitypiston.method_1519(f) < 1.0F) {
			ForgeHooksClient.beforeBlockRender(block, this.field_1131);
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render(Lnet/minecraft/entity/block/PistonBlockEntity;DDDF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderHelper;enableLighting()V"))
	private void forge$afterBlockRender(PistonBlockEntity tileentitypiston, double d, double d1, double d2, float f, CallbackInfo ci) {
		Block block = Block.BY_ID[tileentitypiston.method_1518()];
		ForgeHooksClient.afterBlockRender(block, this.field_1131);
	}
}
