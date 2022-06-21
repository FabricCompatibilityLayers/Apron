package io.github.betterthanupdates.shockahpi.mixin.client;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
	 * @reason yes
	 */
	@Overwrite
	public ChunkIO getChunkIO(Dimension paramxa) {
		File localFile1 = this.getParentFolder();
		DimensionBase localDimensionBase = DimensionBase.getDimByProvider(paramxa.getClass());

		if (localDimensionBase.number != 0) {
			File localFile2 = new File(localFile1, "DIM" + localDimensionBase.number);
			localFile2.mkdirs();
			return new WorldChunkLoader(localFile2);
		} else {
			return new WorldChunkLoader(localFile1);
		}
	}
}
