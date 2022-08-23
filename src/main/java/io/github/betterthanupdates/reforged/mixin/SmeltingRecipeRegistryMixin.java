package io.github.betterthanupdates.reforged.mixin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;

import io.github.betterthanupdates.reforged.recipe.ReforgedSmeltingRecipeRegistry;

@Mixin(SmeltingRecipeRegistry.class)
public class SmeltingRecipeRegistryMixin implements ReforgedSmeltingRecipeRegistry {
	@Shadow
	private Map<Integer, ItemStack> recipes;
	@Unique
	private final Map<List<Integer>, ItemStack> metaSmeltingList = new HashMap<>();

	@Override
	public void addSmelting(int itemID, int metadata, ItemStack itemstack) {
		this.metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
	}

	@Override
	public ItemStack getSmeltingResult(ItemStack item) {
		if (item == null) {
			return null;
		} else {
			ItemStack ret = this.metaSmeltingList.get(Arrays.asList(item.itemId, item.getMeta()));
			return ret != null ? ret : this.recipes.get(item.itemId);
		}
	}
}
