package io.github.betterthanupdates.reforged.recipe;

import net.minecraft.item.ItemStack;

public interface ReforgedSmeltingRecipeManager {
	void addSmelting(int itemID, int metadata, ItemStack itemstack);

	ItemStack getSmeltingResult(ItemStack item);

	ItemStack reforged$getSmeltingResult(ItemStack itemStack);
}
