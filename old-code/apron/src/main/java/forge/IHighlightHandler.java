/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

import io.github.betterthanupdates.Legacy;

@Legacy
@Environment(EnvType.CLIENT)
public interface IHighlightHandler {
	boolean onBlockHighlight(WorldRenderer worldEventRenderer, PlayerEntity player, HitResult hitResult, int i, ItemStack itemStack, float f);
}
