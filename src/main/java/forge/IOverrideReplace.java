/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

@SuppressWarnings("unused")
@Legacy
public interface IOverrideReplace {
	boolean canReplaceBlock(World world, int i, int j, int k, int l);

	boolean getReplacedSuccess();
}
