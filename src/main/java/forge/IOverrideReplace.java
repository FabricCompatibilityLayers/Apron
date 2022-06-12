/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.world.World;

@SuppressWarnings("unused")
public interface IOverrideReplace {
	boolean canReplaceBlock(final World level, final int i, final int j, final int k, final int l);

	boolean getReplacedSuccess();
}
