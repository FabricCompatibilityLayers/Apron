package io.github.betterthanupdates.forge.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * New methods for {@link net.minecraft.block.Block} by Minecraft Forge.
 */
public interface ForgeBlock {
	int getLightValue(BlockView blockView, int x, int y, int z);

	boolean isLadder();

	boolean isBlockNormalCube(World world, int x, int y, int z);

	boolean isBlockSolidOnSide(World world, int x, int y, int z, int side);

	boolean isBlockReplaceable(World world, int x, int y, int z);

	boolean isBlockBurning(World world, int x, int y, int z);

	boolean isAirBlock(World world, int x, int y, int z);

	/**
	 * Gets hardness for a block, taking into account its hardness.
	 *
	 * @param meta the meta value of the block state
	 * @return the hardness (time to break) of the block
	 */
	float getHardness(int meta);

	float blockStrength(World world, PlayerEntity player, int x, int y, int z);

	float blockStrength(PlayerEntity player, int meta);

	boolean canHarvestBlock(PlayerEntity player, int meta);
}
