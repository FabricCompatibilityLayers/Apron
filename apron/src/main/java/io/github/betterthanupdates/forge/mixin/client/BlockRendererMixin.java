package io.github.betterthanupdates.forge.mixin.client;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.world.BlockView;

import io.github.betterthanupdates.forge.ForgeClientReflection;
import io.github.betterthanupdates.forge.client.render.ForgeTessellator;

@Environment(EnvType.CLIENT)
@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin {
	@Shadow
	public BlockView blockView;
	@Shadow
	public int textureOverride;
	@Shadow
	public boolean field_81;
	@Shadow
	public boolean mirrorTexture;
	@Shadow
	public boolean renderAllSides;
	@Shadow
	public int eastFaceRotation;
	@Shadow
	public int westFaceRotation;
	@Shadow
	public int southFaceRotation;
	@Shadow
	public int northFaceRotation;
	@Shadow
	public int topFaceRotation;
	@Shadow
	public int bottomFaceRotation;
	@Shadow
	public int field_55;

	private float f;
	private float[] af;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "<init>()V", at = @At("RETURN"))
	private void forge$init(CallbackInfo ci) {
		this.blockView = null;
		this.forgeCtr();
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "<init>(Lnet/minecraft/world/BlockView;)V", at = @At("RETURN"))
	private void ctrSetDefaultValues(BlockView blockView, CallbackInfo ci) {
		this.forgeCtr();
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	private void forgeCtr() {
		this.textureOverride = -1;
		this.mirrorTexture = false;
		this.renderAllSides = false;
		this.field_81 = true;
		this.eastFaceRotation = 0;
		this.westFaceRotation = 0;
		this.southFaceRotation = 0;
		this.northFaceRotation = 0;
		this.topFaceRotation = 0;
		this.bottomFaceRotation = 0;
		this.field_55 = 1;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render", cancellable = true, at = @At("RETURN"))
	private void forge$render(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
		int l = block.getRenderType();

		switch (l) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				break;
			default:
				cir.setReturnValue(ModLoader.RenderWorldBlock((BlockRenderer) (Object) this, this.blockView, i, j, k, block, l));
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "renderRedstoneDust", at = @At("HEAD"))
	public void renderRedstoneDust(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
		int l = this.blockView.getBlockMeta(i, j, k);
		f = block.getBrightness(this.blockView, i, j, k);
		af = ForgeClientReflection.BlockRenderer$redstoneColors[l];
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@ModifyArgs(method = "renderRedstoneDust", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V"))
	public void renderRedstoneDust(Args args) {
		args.set(0, f * af[0]);
		args.set(1, f * af[1]);
		args.set(2, f * af[2]);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_50", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/block/BlockRenderer;field_67:Z"))
	public boolean method_50() {
		return ((ForgeTessellator) Tessellator.INSTANCE).defaultTexture() && ForgeClientReflection.BlockRenderer$cfgGrassFix;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_58", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/block/BlockRenderer;field_67:Z"))
	public boolean method_58() {
		return ((ForgeTessellator) Tessellator.INSTANCE).defaultTexture() && ForgeClientReflection.BlockRenderer$cfgGrassFix;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_48", at = @At("RETURN"))
	private void forge$method_48(Block block, int i, float f, CallbackInfo ci) {
		int k = block.getRenderType();

		if (k != 0 && k != 16) {
			switch (k) {
				case 1:
				case 2:
				case 6:
				case 10:
				case 11:
				case 13:
					break;
				default:
					ModLoader.RenderInvBlock((BlockRenderer) (Object) this, block, i, k);
			}
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_42", at = @At("RETURN"), cancellable = true)
	private static void forge$method_42(int i, CallbackInfoReturnable<Boolean> cir) {
		switch (i) {
			case 0:
			case 10:
			case 11:
			case 13:
				break;
			default:
				cir.setReturnValue(ModLoader.RenderBlockIsItemFull3D(i));
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void forge$cinit(CallbackInfo ci) {
		for (int i = 0; i < ForgeClientReflection.BlockRenderer$redstoneColors.length; ++i) {
			float f = (float) i / 15.0F;
			float f1 = f * 0.6F + 0.4F;

			if (i == 0) {
				f = 0.0F;
			}

			float f2 = f * f * 0.7F - 0.5F;
			float f3 = f * f * 0.6F - 0.7F;

			if (f2 < 0.0F) {
				f2 = 0.0F;
			}

			if (f3 < 0.0F) {
				f3 = 0.0F;
			}

			ForgeClientReflection.BlockRenderer$redstoneColors[i] = new float[] {f1, f2, f3};
		}
	}
}
