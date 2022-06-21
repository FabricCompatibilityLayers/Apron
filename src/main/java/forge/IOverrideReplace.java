/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.world.World;

@SuppressWarnings("unused")
public interface IOverrideReplace {
	boolean canReplaceBlock(World world, int i, int j, int k, int l);

	boolean getReplacedSuccess();
}
