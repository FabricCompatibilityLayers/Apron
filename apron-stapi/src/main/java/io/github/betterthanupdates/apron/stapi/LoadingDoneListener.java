package io.github.betterthanupdates.apron.stapi;

import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.Registry;

public class LoadingDoneListener implements Runnable {
	@Override
	public void run() {
		ApronStAPICompat.getModContents().forEach(entry -> {
			ModID modID = entry.getKey();
			ModContents modContents = entry.getValue();

			modContents.ITEMS.originalToInstance.forEach((integer, item) -> {
				Registry.register(ItemRegistry.INSTANCE,
						modContents.ITEMS.originalToAuto.get(integer),
						Identifier.of(modID, String.valueOf(integer)),
						item);
			});


			modContents.BLOCKS.originalToInstance.forEach((integer, block) -> {
				Registry.register(BlockRegistry.INSTANCE,
						modContents.BLOCKS.originalToAuto.get(integer),
						Identifier.of(modID, String.valueOf(integer)),
						block);
			});
		});
	}
}
