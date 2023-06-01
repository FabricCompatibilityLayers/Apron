package io.github.betterthanupdates.shockahpi.mixin.client;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.DimensionBase;

import net.minecraft.world.chunk.ChunkIO;
import net.minecraft.world.chunk.WorldChunkLoader;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionFile;
import net.minecraft.world.dimension.McRegionDimensionFile;

@Mixin(McRegionDimensionFile.class)
public class McRegionDimensionFileMixin extends DimensionFile {
	public McRegionDimensionFileMixin(File file, String string, boolean bl) {
		super(file, string, bl);
	}

	/**
	 * @author SAPI
	 * @reason
	 */
	@Inject(method = "getChunkIO", at = @At("HEAD"), cancellable = true)
	public void getChunkIO(Dimension paramxa, CallbackInfoReturnable<ChunkIO> cir) {
		File localFile1 = this.getParentFolder();
		DimensionBase localDimensionBase = DimensionBase.getDimByProvider(paramxa.getClass());

		if (localDimensionBase.number != 0) {
			File localFile2 = new File(localFile1, "DIM" + localDimensionBase.number);
			localFile2.mkdirs();
			cir.setReturnValue(new WorldChunkLoader(localFile2));
		} else {
			cir.setReturnValue(new WorldChunkLoader(localFile1));
		}
	}
}
