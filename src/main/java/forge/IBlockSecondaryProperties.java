/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.world.World;

@SuppressWarnings("unused")
@Legacy
public interface IBlockSecondaryProperties {
	@Legacy
	boolean isBlockNormalCube(World arg, int i, int j, int k);

	@Legacy
	boolean isBlockReplaceable(World arg, int i, int j, int k);

	@Legacy
	boolean isBlockBurning(World arg, int i, int j, int k);

	@Legacy
	boolean isAirBlock(World arg, int i, int j, int k);
}
