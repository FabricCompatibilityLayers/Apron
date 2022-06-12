/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.world.BlockView;

public interface IConnectRedstone {
	boolean canConnectRedstone(final BlockView blockView, final int i, final int j, final int k, final int l);
}
