package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.cursedmixinextensions.annotations.Public;
import fr.catcore.cursedmixinextensions.annotations.ReplaceConstructor;
import fr.catcore.cursedmixinextensions.annotations.ShadowConstructor;
import modloader.ModLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {


	@Shadow
	public net.minecraft.world.BlockView blockView;
	@Shadow
	public int textureOverride;
	@Shadow
	public boolean flipTextureHorizontally;
	@Shadow
	public boolean skipFaceCulling;
	@Shadow
	public boolean inventoryColorEnabled;
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
	public boolean useAo;
	@Shadow
	public float selfBrightness;
	@Shadow
	public float northBrightness;
	@Shadow
	public float bottomBrightness;
	@Shadow
	public float eastBrightness;
	@Shadow
	public float southBrightness;
	@Shadow
	public float topBrightness;
	@Shadow
	public float westBrightness;
	@Shadow
	public float northEastBottomBrightness;
	@Shadow
	public float northBottomBrightness;
	@Shadow
	public float northWestBottomBrightness;
	@Shadow
	public float eastBottomBrightness;
	@Shadow
	public float westBottomBrightness;
	@Shadow
	public float southEastBottomBrightness;
	@Shadow
	public float southBottomBrightness;
	@Shadow
	public float southWestBottomBrightness;
	@Shadow
	public float northEastTopBrightness;
	@Shadow
	public float northTopBrightness;
	@Shadow
	public float northWestTopBrightness;
	@Shadow
	public float eastTopBrightness;
	@Shadow
	public float southEastTopBrightness;
	@Shadow
	public float southTopBrightness;
	@Shadow
	public float westTopBrightness;
	@Shadow
	public float southWestTopBrightness;
	@Shadow
	public float northEastBrightness;
	@Shadow
	public float southEastBrightness;
	@Shadow
	public float northWestBrightness;
	@Shadow
	public float southWestBrightness;
	@Shadow
	public int useSurroundingBrightness;
	@Shadow
	public float firstVertexRed;
	@Shadow
	public float secondVertexRed;
	@Shadow
	public float thirdVertexRed;
	@Shadow
	public float fourthVertexRed;
	@Shadow
	public float firstVertexGreen;
	@Shadow
	public float secondVertexGreen;
	@Shadow
	public float thirdVertexGreen;
	@Shadow
	public float fourthVertexGreen;
	@Shadow
	public float firstVertexBlue;
	@Shadow
	public float secondVertexBlue;
	@Shadow
	public float thirdVertexBlue;
	@Shadow
	public float fourthVertexBlue;
	@Shadow
	public boolean topNorthEdgeTranslucent;
	@Shadow
	public boolean topEastEdgeTranslucent;
	@Shadow
	public boolean topWestEdgeTranslucent;
	@Shadow
	public boolean topSouthEdgeTranslucent;
	@Shadow
	public boolean northWestEdgeTranslucent;
	@Shadow
	public boolean southEastEdgeTranslucent;
	@Shadow
	public boolean southWestEdgeTranslucent;
	@Shadow
	public boolean northEastEdgeTranslucent;
	@Shadow
	public boolean bottomNorthEdgeTranslucent;
	@Shadow
	public boolean bottomEastEdgeTranslucent;
	@Shadow
	public boolean bottomWestEdgeTranslucent;
	@Shadow
	public boolean bottomSouthEdgeTranslucent;

	@Public
	private static boolean cfgGrassFix = true;
	@Public
	private static float[][] redstoneColors = new float[16][];

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void modloader$classInit(CallbackInfo ci) {
		for(int i = 0; i < redstoneColors.length; ++i) {
			float j = (float)i / 15.0F;
			float red = j * 0.6F + 0.4F;
			if (i == 0) {
				j = 0.0F;
			}

			float green = j * j * 0.7F - 0.5F;
			float blue = j * j * 0.6F - 0.7F;
			if (green < 0.0F) {
				green = 0.0F;
			}

			if (blue < 0.0F) {
				blue = 0.0F;
			}

			redstoneColors[i] = new float[]{red, green, blue};
		}
	}

	@ShadowConstructor
	public abstract void ctr();

	@ReplaceConstructor
	public void modloader$ctr(BlockView xp1) {
		ctr();
		this.blockView = xp1;
	}

	@Inject(method = "<init>()V", at = @At("RETURN"))
	private void modloader$ctr(CallbackInfo ci) {
		this.blockView = null;
		this.textureOverride = 0;
		this.flipTextureHorizontally = false;
		this.skipFaceCulling = false;
		this.inventoryColorEnabled = false;
		this.eastFaceRotation = 0;
		this.westFaceRotation = 0;
		this.southFaceRotation = 0;
		this.northFaceRotation = 0;
		this.topFaceRotation = 0;
		this.bottomFaceRotation = 0;
		this.useAo = false;
		this.selfBrightness = 0.0F;
		this.northBrightness = 0.0F;
		this.bottomBrightness = 0.0F;
		this.eastBrightness = 0.0F;
		this.southBrightness = 0.0F;
		this.topBrightness = 0.0F;
		this.westBrightness = 0.0F;
		this.northEastBottomBrightness = 0.0F;
		this.northBottomBrightness = 0.0F;
		this.northWestBottomBrightness = 0.0F;
		this.eastBottomBrightness = 0.0F;
		this.westBottomBrightness = 0.0F;
		this.southEastBottomBrightness = 0.0F;
		this.southBottomBrightness = 0.0F;
		this.southWestBottomBrightness = 0.0F;
		this.northEastTopBrightness = 0.0F;
		this.northTopBrightness = 0.0F;
		this.northWestTopBrightness = 0.0F;
		this.eastTopBrightness = 0.0F;
		this.southEastTopBrightness = 0.0F;
		this.southTopBrightness = 0.0F;
		this.westTopBrightness = 0.0F;
		this.southWestTopBrightness = 0.0F;
		this.northEastBrightness = 0.0F;
		this.southEastBrightness = 0.0F;
		this.northWestBrightness = 0.0F;
		this.southWestBrightness = 0.0F;
		this.useSurroundingBrightness = 0;
		this.firstVertexRed = 0.0F;
		this.secondVertexRed = 0.0F;
		this.thirdVertexRed = 0.0F;
		this.fourthVertexRed = 0.0F;
		this.firstVertexGreen = 0.0F;
		this.secondVertexGreen = 0.0F;
		this.thirdVertexGreen = 0.0F;
		this.fourthVertexGreen = 0.0F;
		this.firstVertexBlue = 0.0F;
		this.secondVertexBlue = 0.0F;
		this.thirdVertexBlue = 0.0F;
		this.fourthVertexBlue = 0.0F;
		this.topNorthEdgeTranslucent = false;
		this.topEastEdgeTranslucent = false;
		this.topWestEdgeTranslucent = false;
		this.topSouthEdgeTranslucent = false;
		this.northWestEdgeTranslucent = false;
		this.southEastEdgeTranslucent = false;
		this.southWestEdgeTranslucent = false;
		this.northEastEdgeTranslucent = false;
		this.bottomNorthEdgeTranslucent = false;
		this.bottomEastEdgeTranslucent = false;
		this.bottomWestEdgeTranslucent = false;
		this.bottomSouthEdgeTranslucent = false;
		this.textureOverride = -1;
		this.flipTextureHorizontally = false;
		this.skipFaceCulling = false;
		this.inventoryColorEnabled = true;
		this.eastFaceRotation = 0;
		this.westFaceRotation = 0;
		this.southFaceRotation = 0;
		this.northFaceRotation = 0;
		this.topFaceRotation = 0;
		this.bottomFaceRotation = 0;
		this.useSurroundingBrightness = 1;
	}

	@ModifyReturnValue(method = "render(Lnet/minecraft/block/Block;III)Z", at = @At(value = "RETURN", ordinal = 18))
	private boolean modloader$RenderWorldBlock(boolean original,
											   @Local(argsOnly = true) Block uu1,
											   @Local(ordinal = 0, argsOnly = true) int i1,
											   @Local(ordinal = 1, argsOnly = true) int j1,
											   @Local(ordinal = 2, argsOnly = true) int k1,
											   @Local(ordinal = 3) int l1) {
		if (!original) {
			return ModLoader.RenderWorldBlock((BlockRenderManager) (Object) this, this.blockView, i1, j1, k1, uu1, l1);
		}

		return true;
	}

	@Public
	private static void setRedstoneColors(float[][] colors) {
		if (colors.length != 16) {
			throw new IllegalArgumentException("Must be 16 colors.");
		} else {
			for(int i = 0; i < colors.length; ++i) {
				if (colors[i].length != 3) {
					throw new IllegalArgumentException("Must be 3 channels in a color.");
				}
			}

			redstoneColors = colors;
		}
	}

	@WrapOperation(method = "renderRedstoneDust", at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V", ordinal = 0),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V", ordinal = 3),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V", ordinal = 6),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V", ordinal = 8),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V", ordinal = 10),
	})
	private void modloader$RenderRedstoneColor(Tessellator instance, float g, float b, float v, Operation<Void> original,
											   @Local(ordinal = 3) int l1,
											   @Local(ordinal = 0) float f1) {
		float[] color = redstoneColors[l1];
		float f3 = color[0];
		float f4 = color[1];
		float f5 = color[2];
		original.call(instance, f1 * f3, f1 * f4, f1 * f5);
	}

	@WrapOperation(method = {"renderSmooth", "renderFlat"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/block/BlockRenderManager;fancyGraphics:Z"))
	private boolean modloader$ReplaceFancyGraphicsWithGrassFix(Operation<Boolean> original) {
		return cfgGrassFix;
	}

	@Inject(method = "render(Lnet/minecraft/block/Block;IF)V", at = @At("RETURN"))
	private void modloader$RenderInvBlock(Block uu1, int i1, float f1, CallbackInfo ci) {
		int k1 = uu1.getRenderType();

		if (k1 != 0 && k1 != 16) {
			switch (k1) {
				case 1:
				case 2:
				case 6:
				case 10:
				case 11:
				case 13:
					break;
				default:
					ModLoader.RenderInvBlock((BlockRenderManager) (Object) this, uu1, i1, k1);
			}
		}
	}

	@ModifyReturnValue(method = "isSideLit", at = @At(value = "RETURN", ordinal = 4))
	private static boolean modloader$RenderBlockIsItemFull3D(boolean original,
															 @Local(argsOnly = true) int i1) {
		return ModLoader.RenderBlockIsItemFull3D(i1);
	}
}
