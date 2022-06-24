/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.client.render.block.BlockRenderer;

@Legacy
public interface ICustomItemRenderer {
	@Legacy
	void renderInventory(BlockRenderer arg, int i, int j);
}
