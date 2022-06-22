package io.github.betterthanupdates.forge.block;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * New methods for {@link net.minecraft.block.Block} by Minecraft Forge.
 */
public interface ForgeBlock {
	int quantityDropped(int i, Random random);

	int quantityDropped(int i, Random random, ItemStack stack);

	int damageDropped(int i, ItemStack stack);

	int idDropped(int i, Random random, ItemStack stack);

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
