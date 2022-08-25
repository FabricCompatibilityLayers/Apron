package io.github.betterthanupdates.stapi;

import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;

import net.minecraft.item.ItemStack;

public class StationAPIHelper {
	public static ItemStack SmeltingRegistry$getResultFor(ItemStack itemStack) {
		return SmeltingRegistry.getResultFor(itemStack);
	}
}
