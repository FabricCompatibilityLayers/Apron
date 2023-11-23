package io.github.betterthanupdates.apron.stapi.bhcreative;

import java.util.Map;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.ModContents;
import modloader.ModLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class CreativeTabsListener {
	@EventListener
	public void onTabInit(TabRegistryEvent event) {
		ApronStAPICompat.getModContents().forEach(entry -> {
			Namespace modID = entry.getKey();
			ModContents modContents = entry.getValue();

			if (!modContents.ITEMS.originalToInstance.isEmpty()) {
				Item firstItem = Item.DIAMOND;

				for (Map.Entry<Integer, Item> entry1 : modContents.ITEMS.originalToInstance.entrySet()) {
					firstItem = entry1.getValue();
					break;
				}

				Identifier tabId = modID.id("tab");

				SimpleTab tab = new SimpleTab(tabId, firstItem);

				event.register(tab);

				modContents.ITEMS.originalToInstance.forEach((integer, item) -> tab.addItem(new ItemStack(item)));

				ModLoader.AddLocalization("tab." + tabId + ".name", modID.toString().replace("mod_", ""));
			}
		});
	}
}
