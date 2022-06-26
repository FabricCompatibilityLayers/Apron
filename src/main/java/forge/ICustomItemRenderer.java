/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.client.render.block.BlockRenderer;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface ICustomItemRenderer {
	void renderInventory(BlockRenderer blockRenderer, int i, int j);
}
