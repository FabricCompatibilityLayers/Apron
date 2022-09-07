package io.github.betterthanupdates.stapi;

import java.util.HashMap;
import java.util.Map;

import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import shockahpi.AchievementPage;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.achievement.Achievement;

public class StationAPIHelper {
	public static ItemStack SmeltingRegistry$getResultFor(ItemStack itemStack) {
		return SmeltingRegistry.getResultFor(itemStack);
	}

	public static String MOD_ID = "";

	public static ModID getCurrentModId() {
		return ModID.of(MOD_ID);
	}

	public static final Map<Integer, ModID> BLOCKS = new HashMap<>();
	public static final Map<Integer, ModID> ITEMS = new HashMap<>();

	public static int assignBlockId(int id) {
		if (MOD_ID.isEmpty()) return id;

		id = getFreeBlockId();

		BLOCKS.put(id, getCurrentModId());

		return id;
	}

	public static int assignItemId(int id) {
		if (MOD_ID.isEmpty()) return id;

		id = getFreeItemId();

		ITEMS.put(id, getCurrentModId());

		return id - 256;
	}

	public static int getFreeBlockId() {
		for (int i = 1; i < Block.BY_ID.length; i++) {
			if (Block.BY_ID[i] == null) {
				return i;
			}
		}

		throw new RuntimeException("There are no more available IDs for blocks!");
	}

	public static int getFreeItemId() {
		for (int i = 1; i < Item.byId.length; i++) {
			if (Item.byId[i] == null) {
				return i;
			}
		}

		throw new RuntimeException("There are no more available IDs for items!");
	}

	public static void createStAPIAchievementPage(AchievementPage page) {
		new StAPIAchievementPage(page);
	}

	public static void registerMinecraftAchievement(Achievement... achievements) {
		net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage mcPage = net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage.getCurrentPage();

		while (!mcPage.name().equals("Minecraft")) {
			mcPage = net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage.nextPage();
		}

		mcPage.addAchievements(achievements);
	}
}
