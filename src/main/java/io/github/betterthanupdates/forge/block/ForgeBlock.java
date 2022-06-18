package io.github.betterthanupdates.forge.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * New methods for {@link net.minecraft.block.Block} by Minecraft Forge
 */
public interface ForgeBlock {
	int getLightValue(BlockView iba, int i, int j, int k);

	boolean isLadder();

	boolean isBlockNormalCube(World world, int i, int j, int k);

	boolean isBlockSolidOnSide(World world, int i, int j, int k, int side);

	boolean isBlockReplaceable(World world, int i, int j, int k);

	boolean isBlockBurning(World world, int i, int j, int k);

	boolean isAirBlock(World world, int i, int j, int k);

	/**
	 * Gets hardness for a block, taking into account its hardness.
	 * @param meta the meta value of the block state
	 * @return the hardness (time to break) of the block
	 */
	float getHardness(int meta);

	float blockStrength(World world, PlayerEntity player, int i, int j, int k);

	float blockStrength(PlayerEntity player, int md);

	boolean canHarvestBlock(PlayerEntity player, int md);
}
