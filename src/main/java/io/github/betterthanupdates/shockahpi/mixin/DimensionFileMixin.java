package io.github.betterthanupdates.shockahpi.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import shockahpi.DimensionBase;

import net.minecraft.server.PlayerHandler;
import net.minecraft.world.WorldManager;
import net.minecraft.world.chunk.ChunkIO;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;
import net.minecraft.world.dimension.DimensionFile;

@Mixin(DimensionFile.class)
public abstract class DimensionFileMixin implements PlayerHandler, DimensionData {

	@Shadow
	@Final
	private File parentFolder;

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public ChunkIO getChunkIO(Dimension paramxa) {
		DimensionBase localDimensionBase = DimensionBase.getDimByProvider(paramxa.getClass());
		if (localDimensionBase.number != 0) {
			File localFile = new File(this.parentFolder, "DIM" + localDimensionBase.number);
			localFile.mkdirs();
			return new WorldManager(localFile, true);
		} else {
			return new WorldManager(this.parentFolder, true);
		}
	}
}
