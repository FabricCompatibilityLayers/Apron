/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface ICraftingHandler {
	void onTakenFromCrafting(PlayerEntity player, ItemStack itemStack, Inventory inventory);
}
