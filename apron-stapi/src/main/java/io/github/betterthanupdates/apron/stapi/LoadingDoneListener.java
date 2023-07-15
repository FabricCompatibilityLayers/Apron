package io.github.betterthanupdates.apron.stapi;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.Registry;

import java.util.concurrent.atomic.AtomicInteger;

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
			System.out.println("Warning, some objects were registered later, trying to attribute identifier to them.");
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

			if (FabricLoader.getInstance().isModLoaded("hmifabric")) {
				HMICompat.regenerateRecipeList();
			}
		}
	}
}
