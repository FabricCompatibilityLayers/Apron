package io.github.betterthanupdates.apron.stapi.mixin.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.client.render.block.BlockRenderManager;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;

@Mixin(BlockRenderManager.class)
public class BlockRendererMixin {
	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.BlockRenderManagerMixin",
			name = "stationapi_block_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true
	)
	private int apron$stapi$fixBlockTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.BedRendererMixin",
			name = "stationapi_bed_captureTexture1",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixBedTextureIndex1(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}

	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.BedRendererMixin",
			name = "stationapi_bed_captureTexture2",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixBedTextureIndex2(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.CropRendererMixin",
			name = "stationapi_column_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 1
	)
	private int apron$stapi$fixColumnTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.CrossRendererMixin",
			name = "stationapi_crossed_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 1
	)
	private int apron$stapi$fixCrossedTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.FireRendererMixin",
			name = "stationapi_fire_captureTexture1",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixFireTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.FluidRendererMixin",
			name = "stationapi_fluid_captureTexture1",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixFluidTextureIndex1(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}

	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.FluidRendererMixin",
			name = "stationapi_fluid_captureTexture2",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixFluidTextureIndex2(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.LadderRendererMixin",
			name = "stationapi_ladder_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixLadderTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.LeverRendererMixin",
			name = "stationapi_lever_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixLeverTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.RailRendererMixin",
			name = "stationapi_rails_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixRailTextureIndex(int texture, @Local(ordinal = 0) RailBlock block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.RepeaterRendererMixin",
			name = "stationapi_repeater_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true,
			ordinal = 3
	)
	private int apron$stapi$fixRepeaterTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}


	@TargetHandler(
			mixin = "net.modificationstation.stationapi.mixin.arsenic.client.block.TorchRendererMixin",
			name = "stationapi_torch_captureTexture",
			prefix = "handler"
	)
	@ModifyVariable(
			method = "@MixinSquared:Handler", at = @At("HEAD"),
			argsOnly = true
	)
	private int apron$stapi$fixTorchTextureIndex(int texture, @Local(ordinal = 0) Block block) {
		return ApronStAPICompat.fixBlockTexture(texture, block);
	}
}
