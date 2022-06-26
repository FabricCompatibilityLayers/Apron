/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.world.BlockView;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IConnectRedstone {
	boolean canConnectRedstone(BlockView blockView, int i, int j, int k, int l);
}
