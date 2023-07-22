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
}
