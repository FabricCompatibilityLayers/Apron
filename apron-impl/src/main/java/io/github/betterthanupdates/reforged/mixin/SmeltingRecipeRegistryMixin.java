package io.github.betterthanupdates.reforged.mixin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;

import io.github.betterthanupdates.reforged.recipe.ReforgedSmeltingRecipeRegistry;
import io.github.betterthanupdates.stapi.StationAPIHelper;

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
		if (!FabricLoader.getInstance().isModLoaded("stationapi")) {
			ItemStack reforgedStack = this.reforged$getSmeltingResult(item);
			return reforgedStack != null ? reforgedStack : this.recipes.get(item.itemId);
		} else {
			return StationAPIHelper.SmeltingRegistry$getResultFor(item);
		}
	}

	@Override
	public ItemStack reforged$getSmeltingResult(ItemStack itemStack) {
		if (itemStack != null) {
			return this.metaSmeltingList.get(Arrays.asList(itemStack.itemId, itemStack.getMeta()));
		}

		return null;
	}
}
