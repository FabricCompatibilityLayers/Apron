/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.world.World;

public interface IBlockSecondaryProperties {
	boolean isBlockNormalCube(World arg, int i, int j, int k);

	boolean isBlockReplaceable(World arg, int i, int j, int k);

	boolean isBlockBurning(World arg, int i, int j, int k);

	boolean isAirBlock(World arg, int i, int j, int k);
}
