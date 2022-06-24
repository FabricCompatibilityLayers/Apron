/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.world.World;

@SuppressWarnings("unused")
@Legacy
public interface IOverrideReplace {
	@Legacy
	boolean canReplaceBlock(World arg, int i, int j, int k, int l);

	@Legacy
	boolean getReplacedSuccess();
}
