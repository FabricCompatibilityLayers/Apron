package io.github.betterthanupdates.stapi;

import java.util.HashMap;
import java.util.Map;

import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.ModID;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class StationAPIHelper {
	public static ItemStack SmeltingRegistry$getResultFor(ItemStack itemStack) {
		return SmeltingRegistry.getResultFor(itemStack);
	}

	public static String MOD_ID = "";

	public static ModID getCurrentModId() {
		return ModID.of(MOD_ID);
	}

	public static final Map<Integer, ModID> BLOCKS = new HashMap<>();

	public static int assignBlockId(int id) {
		if (MOD_ID.isEmpty()) return id;

		id = getFreeBlockId();

		BLOCKS.put(id, getCurrentModId());

		return id;
	}

	public static int getFreeBlockId() {
		for (int i = 1; i < Block.BY_ID.length; i++) {
			if (Block.BY_ID[i] == null) {
				return i;
			}
		}

		throw new RuntimeException("There are no more available IDs for blocks!");
	}
}
