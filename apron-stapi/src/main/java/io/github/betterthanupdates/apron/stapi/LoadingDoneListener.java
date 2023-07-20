package io.github.betterthanupdates.apron.stapi;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.Registry;
import shockahpi.DimensionBase;
import shockahpi.SAPI;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.stat.achievement.Achievement;

import io.github.betterthanupdates.apron.stapi.mixin.AchievementPageAccessor;

public class LoadingDoneListener implements Runnable {
	public static int lastTotal = 0;
	@Override
	public void run() {
		AtomicInteger totalObjectsNumber = new AtomicInteger();
		// Get object number.
		ApronStAPICompat.getModContents().forEach(entry -> {
			ModContents modContents = entry.getValue();

			modContents.ITEMS.originalToInstance.forEach((integer, item) -> {
				totalObjectsNumber.getAndIncrement();
			});
			modContents.ITEMS.duplicatesInstances.forEach((integer, item) -> {
				totalObjectsNumber.getAndIncrement();
			});


			modContents.BLOCKS.originalToInstance.forEach((integer, block) -> {
				totalObjectsNumber.getAndIncrement();
			});
		});
		// Then Register
		if (lastTotal != totalObjectsNumber.get()) {
			ApronStAPICompat.LOGGER.warn("Some objects were registered later, trying to attribute identifier to them.");
			lastTotal = totalObjectsNumber.get();
			ApronStAPICompat.getModContents().forEach(entry -> {
				ModID modID = entry.getKey();
				ModContents modContents = entry.getValue();

				modContents.ITEMS.originalToInstance.forEach((integer, item) -> {
					Identifier id = Identifier.of(modID, String.valueOf(integer));
					if (!ItemRegistry.INSTANCE.containsId(id))
						Registry.register(ItemRegistry.INSTANCE,
							modContents.ITEMS.originalToAuto.get(integer),
							id,
							item);
				});
				modContents.ITEMS.duplicatesInstances.forEach((integer, item) -> {
					Identifier id = Identifier.of(modID, integer + "_");
					if (!ItemRegistry.INSTANCE.containsId(id))
						Registry.register(ItemRegistry.INSTANCE,
							modContents.ITEMS.duplicates.get(integer),
							id,
							item);
				});


				modContents.BLOCKS.originalToInstance.forEach((integer, block) -> {
					Identifier id = Identifier.of(modID, String.valueOf(integer));
					if (!BlockRegistry.INSTANCE.containsId(id))
						Registry.register(BlockRegistry.INSTANCE,
							modContents.BLOCKS.originalToAuto.get(integer),
							id,
							block);
				});
			});

//			DimensionBase.list.forEach(dimensionBase -> {
//				if (dimensionBase.number == 0 || dimensionBase.number == -1) return;
//
//				String name = dimensionBase.getClass().getSimpleName();
//
//				Registry.register(DimensionRegistry.INSTANCE,
//						ModID.of("mods").id(name), new DimensionContainer<>(dimensionBase::getWorldProvider));
//			});

			SAPI.getPages().forEach(page -> {
				String name = page.title;

				if ("Minecraft".equals(name)) {
					for (AchievementPage stPage : AchievementPageAccessor.getPAGES()) {
						if (stPage.name().equals("station-achievements-v0:minecraft")) {
							for (Achievement achievement : page.getAchievements())
								stPage.addAchievements(achievement);

							break;
						}
					}
				} else {
					boolean found = false;
					String idTitle = page.title
							.replace(" ", "_")
							.replace("&", "and");

					for (AchievementPage stPage : AchievementPageAccessor.getPAGES()) {
						if (stPage.name().equals("apron:" + idTitle)) {
							for (Achievement achievement : page.getAchievements())
								stPage.addAchievements(achievement);

							found = true;
							break;
						}
					}

					if (!found) {
						AchievementPage stPage = new AchievementPage(ModID.of("apron"), idTitle);

						ModLoader.AddLocalization("stationapi:achievementPage.apron:" + idTitle, page.title);

						for (Achievement achievement : page.getAchievements())
							stPage.addAchievements(achievement);
					}
				}
			});

			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				ModLoader.getMinecraftInstance().textureManager.reloadTexturesFromTexturePack();
			}

			if (FabricLoader.getInstance().isModLoaded("hmifabric")) {
				HMICompat.regenerateRecipeList();
			}
		}
	}
}
