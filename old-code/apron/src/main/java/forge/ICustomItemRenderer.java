/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.BlockRenderManager;
import io.github.betterthanupdates.Legacy;

@Legacy
@Environment(EnvType.CLIENT)
public interface ICustomItemRenderer {
	void renderInventory(BlockRenderManager blockRenderer, int itemId, int meta);
}
