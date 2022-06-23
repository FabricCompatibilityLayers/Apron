package io.github.betterthanupdates.forge;

import net.minecraft.item.ItemStack;

public interface ReforgedSmeltingRecipeRegistry {
	void addSmelting(int itemID, int metadata, ItemStack itemstack);

	ItemStack getSmeltingResult(ItemStack item);
}
