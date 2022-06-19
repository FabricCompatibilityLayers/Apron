/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.client.render.WorldEventRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

public interface IHighlightHandler {
	boolean onBlockHighlight(WorldEventRenderer renderer, PlayerEntity player, HitResult result, int i, ItemStack stack, float f);
}
