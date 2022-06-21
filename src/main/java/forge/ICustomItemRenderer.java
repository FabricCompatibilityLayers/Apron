/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.client.render.block.BlockRenderer;

public interface ICustomItemRenderer {
	void renderInventory(BlockRenderer blockRenderer, int itemId, int meta);
}
