package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.dimension.DimensionData;
import net.minecraft.world.dimension.DimensionFile;

@Mixin(DimensionFile.class)
public abstract class DimensionFileMixin implements DimensionData {
//	@Shadow
//	@Final
//	private File parentFolder;
//
//	/**
//	 * @author SAPI
//	 * @reason
//	 */
//	@Overwrite
//	public ChunkIO getChunkIO(Dimension paramxa) {
//		DimensionBase localDimensionBase = DimensionBase.getDimByProvider(paramxa.getClass());
//
//		if (localDimensionBase.number != 0) {
//			File localFile = new File(this.parentFolder, "DIM" + localDimensionBase.number);
//			localFile.mkdirs();
//			return new WorldManager(localFile, true);
//		} else {
//			return new WorldManager(this.parentFolder, true);
//		}
//	}
}
