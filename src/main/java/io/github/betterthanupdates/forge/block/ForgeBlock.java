package io.github.betterthanupdates.forge.block;

/**
 * New methods for {@link net.minecraft.block.Block} by Minecraft Forge
 */
public interface ForgeBlock {
	/**
	 * Gets hardness for a block, taking into account its hardness.
	 * @param meta the meta value of the block state
	 * @return the hardness (time to break) of the block
	 */
	float getHardness(int meta);
}
