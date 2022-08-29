package io.github.betterthanupdates.stapi;

import java.util.HashMap;
import java.util.Map;

import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
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

	public static final Map<Integer, Identifier> BLOCKS = new HashMap<>();
	public static final Map<Integer, Integer> OLD_BLOCKS = new HashMap<>();

	public static int assignBlockId(int id) {
		System.out.println(MOD_ID);
		if (MOD_ID.isEmpty()) return id;

		Identifier blockId = getCurrentModId().id(String.valueOf(id));

		id = getFreeBlockId();

		BLOCKS.put(id, blockId);
		OLD_BLOCKS.put(Integer.valueOf(blockId.id), id);

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
