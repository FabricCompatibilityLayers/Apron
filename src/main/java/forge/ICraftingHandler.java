/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Legacy
public interface ICraftingHandler {
	@Legacy
	void onTakenFromCrafting(PlayerEntity arg, ItemStack arg2, Inventory arg3);
}
