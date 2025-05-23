package io.github.betterthanupdates.apron.stapi;

import io.github.betterthanupdates.apron.LifecycleUtils;
import io.github.betterthanupdates.apron.stapi.hmi.HMICompat;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.modificationstation.stationapi.api.client.gui.screen.achievement.AchievementPage;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import shockahpi.SAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import io.github.betterthanupdates.apron.stapi.mixin.AchievementPageAccessor;

import static net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema.putItem;
import static net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema.putState;

public class LoadingDoneListener implements Runnable {
	public static int lastTotal = 0;

	public static final Map<Integer, Identifier> IDS = new HashMap<>();
	public static final Map<Integer, Boolean> IDS_TYPE = new HashMap<>();
	public static final List<LateUpdate> LATE_UPDATES = new ArrayList<>();

	public static boolean allowConversion = true;
	public static boolean registeredFixer = false;

	private static void addId(int oldId, Identifier identifier, boolean block) {
		if (!allowConversion) return;

		if (IDS.containsKey(oldId)) {
			if (!IDS.get(oldId).toString().equals(identifier.toString())) {
				allowConversion = false;
				ApronStAPICompat.LOGGER.warn("Found conflicting ids for items/blocks between mods " + IDS.get(oldId).namespace + " and " + identifier.namespace + " for id " + oldId);
			} else {
				IDS_TYPE.put(oldId, block);
			}
		} else {
			IDS.put(oldId, identifier);
			IDS_TYPE.put(oldId, block);
		}
	}

	public static void registerFixes() {
		if (!registeredFixer) {
			registeredFixer = true;

			if (LoadingDoneListener.allowConversion) {
				LoadingDoneListener.IDS.forEach((old, identifier) -> {
					boolean block = LoadingDoneListener.IDS_TYPE.get(old);

					if (block) {
						putState(old, identifier.toString());
					} else {
						putItem(old, identifier.toString());
					}
				});
			}
		}
	}

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

		LifecycleUtils.SHOCKAHPI_DIMENSIONS.forEach((modId, list) -> totalObjectsNumber.getAndAdd(list.size()));

		// Then Register
		if (lastTotal != totalObjectsNumber.get()) {
			if (lastTotal != 0) ApronStAPICompat.LOGGER.warn("Some objects were registered later, trying to attribute identifier to them.");
			lastTotal = totalObjectsNumber.get();

			ApronStAPICompat.getModContents().forEach(entry -> {
				Namespace modID = entry.getKey();
				ModContents modContents = entry.getValue();

				modContents.ITEMS.originalToInstance.forEach((integer, item) -> {
					Identifier id = Identifier.of(modID, String.valueOf(integer));
					if (!ItemRegistry.INSTANCE.containsId(id)) {
						Registry.register(ItemRegistry.INSTANCE,
								modContents.ITEMS.originalToAuto.get(integer),
								id,
								item);

						addId(integer, id, false);
					}
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
					if (!BlockRegistry.INSTANCE.containsId(id)) {
						Registry.register(BlockRegistry.INSTANCE,
								modContents.BLOCKS.originalToAuto.get(integer),
								id,
								block);

						addId(integer, id, true);
					}
				});
			});

			LATE_UPDATES.forEach(LateUpdate::update);

			LifecycleUtils.SHOCKAHPI_DIMENSIONS.forEach((modStringId, list) -> {
				Namespace namespace;

				if (modStringId == null) namespace = Namespace.MINECRAFT;
				else namespace = Namespace.of(modStringId);

				list.forEach(dimensionBase -> {
                    if (dimensionBase.number != 0 && dimensionBase.number != -1) {
                        Identifier dimId = namespace.id(String.valueOf(dimensionBase.number));

                        if (!DimensionRegistry.INSTANCE.containsId(dimId)) {
                            ApronStAPICompat.LOGGER.info("Registering Dimension with num " + dimensionBase.number + " and id " + dimId + " : " +  dimensionBase.getClass().getName());
                            Registry.register(DimensionRegistry.INSTANCE, dimensionBase.number, dimId,
                                    new DimensionContainer<>(dimensionBase::getWorldProvider));
                        }
                    }
                });
			});

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
						AchievementPage stPage = new AchievementPage(Namespace.of("apron").id(idTitle));

						ModLoader.AddLocalization("gui.stationapi.achievementPage.apron." + idTitle, page.title);

						for (Achievement achievement : page.getAchievements())
							stPage.addAchievements(achievement);
					}
				}
			});

			List<Achievement> registeredAchievements = new ArrayList<>();

			for (AchievementPage page : AchievementPageAccessor.getPAGES()) {
				registeredAchievements.addAll(page.getAchievements());
			}

			for (Achievement achievement : (List<Achievement>) Achievements.ACHIEVEMENTS) {
				if (!registeredAchievements.contains(achievement)) {
					for (AchievementPage page : AchievementPageAccessor.getPAGES()) {
						if (page.name().equals("station-achievements-v0:minecraft")) {
							page.addAchievements(achievement);
						}
					}
				}
			}

			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				ModLoader.getMinecraftInstance().textureManager.method_1096();
			}

			if (FabricLoader.getInstance().isModLoaded("hmifabric")) {
				HMICompat.regenerateRecipeList();
			}
		}
	}

	public static interface LateUpdate {
		void update();
	}
}
