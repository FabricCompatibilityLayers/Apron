/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

public interface IHighlightHandler {
	boolean onBlockHighlight(final WorldRenderer worldRenderer, final PlayerEntity playerBase, final HitResult hitResult, final int i, final ItemStack itemInstance, final float f);
}
