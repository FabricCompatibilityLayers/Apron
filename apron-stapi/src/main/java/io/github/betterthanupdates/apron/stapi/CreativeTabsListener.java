package io.github.betterthanupdates.apron.stapi;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ModID;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class CreativeTabsListener {
	@EventListener
	public void onTabInit(TabRegistryEvent event) {
//		tab = new SimpleTab(YOUR_MOD_ID.id("yourTabName"), ItemBase.apple); // Making tab
//		event.register(tab); // Registering tab
//		tab.addItem(new ItemInstance(ItemBase.apple)); // Adding apple
		ApronStAPICompat.getModContents().forEach(entry -> {
			ModID modID = entry.getKey();
			ModContents modContents = entry.getValue();

			SimpleTab tab = new SimpleTab(modID.id("tab"), Item.DIAMOND);

			event.register(tab);

			modContents.ITEMS.originalToInstance.forEach((integer, item) -> tab.addItem(new ItemStack(item)));
		});
	}
}
