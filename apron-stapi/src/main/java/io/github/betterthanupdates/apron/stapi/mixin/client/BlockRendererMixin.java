package io.github.betterthanupdates.apron.stapi.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.client.render.block.BlockRenderer;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;

@Mixin(BlockRenderer.class)
public class BlockRendererMixin {
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
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}

	@ModifyVariable(
			method = {"renderTorchTilted", "method_56", "method_47", "renderFire", "renderLadder"},
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
					opcode = 180,
					ordinal = 1,
					shift = At.Shift.BY,
					by = 3
			),
			ordinal = 0
	)
	private int apron$stapi$fixOtherTextureIndex1(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}

	@ModifyVariable(
			method = {"method_56", "method_47"},
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
					opcode = 180,
					ordinal = 1,
					shift = At.Shift.BY,
					by = 3
			),
			ordinal = 1
	)
	private int apron$stapi$fixOtherTextureIndex2(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}

	@ModifyVariable(
			method = {"renderFire", "renderLadder"},
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
					opcode = 180,
					ordinal = 1,
					shift = At.Shift.BY,
					by = 3
			),
			ordinal = 3
	)
	private int apron$stapi$fixOtherTextureIndex3(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}

	@ModifyVariable(
			method = {"renderRails"},
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
					opcode = 180,
					ordinal = 1,
					shift = At.Shift.BY,
					by = 3
			),
			ordinal = 4
	)
	private int apron$stapi$fixOtherTextureIndex4(int texture, @Local(ordinal = 0) RailBlock block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}

	@ModifyVariable(
			method = {"renderRedstoneDust"},
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
					opcode = 180,
					ordinal = 1,
					shift = At.Shift.BY,
					by = 3
			),
			ordinal = 4
	)
	private int apron$stapi$fixOtherTextureIndex5(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}
}
