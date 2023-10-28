/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.BlockRenderer;

import io.github.betterthanupdates.Legacy;

@Legacy
@Environment(EnvType.CLIENT)
public interface ICustomItemRenderer {
	void renderInventory(BlockRenderer blockRenderer, int itemId, int meta);
}
