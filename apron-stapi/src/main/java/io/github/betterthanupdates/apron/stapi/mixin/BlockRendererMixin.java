package io.github.betterthanupdates.apron.stapi.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import forge.ITextureProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.client.render.block.BlockRenderer;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.SpritesheetInstance;

@Mixin(BlockRenderer.class)
public class BlockRendererMixin {
//	@Redirect(method = {
//			"method_48", "method_53", "renderFluid", "renderTorchTilted", "renderLadder", "renderFire", "renderLever", "renderRedstoneRepeater"
//	}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getTextureForSide(I)I"))
//	private int apron$stapi$fixTextureIndex(Block instance, int i) {
//		int texture = instance.getTextureForSide(i);
//
//		if (instance instanceof ITextureProvider textureProvider) {
//			SpritesheetInstance spritesheetInstance = ApronStAPICompat.SPRITESHEET_MAP.get(textureProvider.getTextureFile());
//
//			if (spritesheetInstance != null) {
//				if (spritesheetInstance.BLOCKS.containsKey(texture)) {
//					return spritesheetInstance.BLOCKS.get(texture);
//				}
//			}
//		} else if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(texture)) {
//			return ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(texture);
//		}
//
//		return texture;
//	}
//
//	@Redirect(method = {
//			"method_48", "renderFluid", "method_56", "method_47", "renderRedstoneDust"
//	}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getTextureForSide(II)I"))
//	private int apron$stapi$fixTextureIndex(Block instance, int i, int j) {
//		int texture = instance.getTextureForSide(i, j);
//
//		if (instance instanceof ITextureProvider textureProvider) {
//			SpritesheetInstance spritesheetInstance = ApronStAPICompat.SPRITESHEET_MAP.get(textureProvider.getTextureFile());
//
//			if (spritesheetInstance != null) {
//				if (spritesheetInstance.BLOCKS.containsKey(texture)) {
//					return spritesheetInstance.BLOCKS.get(texture);
//				}
//			}
//		} else if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(texture)) {
//			return ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(texture);
//		}
//
//		return texture;
//	}
//
//	@Redirect(method = {
//			"renderRails"
//	}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RailBlock;getTextureForSide(II)I"))
//	private int apron$stapi$fixTextureIndex(RailBlock instance, int i, int j) {
//		int texture = instance.getTextureForSide(i, j);
//

//	}
	@ModifyVariable(
			method = {"renderBottomFace", "renderTopFace", "renderEastFace", "renderWestFace", "renderNorthFace", "renderSouthFace"},
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
					opcode = 180,
					ordinal = 1,
					shift = At.Shift.BY,
					by = 3
			),
			argsOnly = true
	)
	private int apron$stapi$fixTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		if (block instanceof ITextureProvider textureProvider) {
			SpritesheetInstance spritesheetInstance = ApronStAPICompat.SPRITESHEET_MAP.get(textureProvider.getTextureFile());

			if (spritesheetInstance != null) {
				if (spritesheetInstance.BLOCKS.containsKey(texture)) {
					return spritesheetInstance.BLOCKS.get(texture);
				}
			}
		} else if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(texture)) {
			return ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(texture);
		}

		return texture;
	}
}
