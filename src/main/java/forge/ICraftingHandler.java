/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface ICraftingHandler {
	void onTakenFromCrafting(final PlayerEntity playerBase, final Inventory itemInstance, final ItemStack inventoryBase);
}
