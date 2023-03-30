/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

@SuppressWarnings("unused")
@Legacy
public interface IBlockSecondaryProperties {
	boolean isBlockNormalCube(World world, int x, int y, int z);

	boolean isBlockReplaceable(World world, int x, int y, int z);

	boolean isBlockBurning(World world, int x, int y, int z);

	boolean isAirBlock(World world, int x, int y, int z);
}
