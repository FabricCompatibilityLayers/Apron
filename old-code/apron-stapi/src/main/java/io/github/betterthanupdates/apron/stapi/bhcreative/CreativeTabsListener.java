package io.github.betterthanupdates.apron.stapi.bhcreative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.ModContents;
import modloader.ModLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class CreativeTabsListener {
	private static final List<String> translationKeys = new ArrayList<>();
	private static final Map<Item, Integer> METAS = new HashMap<>();

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

				modContents.ITEMS.originalToInstance.forEach((integer, item) -> {
					addItemToTab(tab, item);
				});

				ModLoader.AddLocalization("tab." + modID + ".tab.name", modID.toString().replace("mod_", ""));
			}
		});
	}

	private void addItemToTab(SimpleTab tab, Item item) {
		if (!METAS.containsKey(item)) {
			int meta;

			for (meta = 0;; meta++) {
				ItemStack stack = new ItemStack(item, 1, meta);

				String translationKey = stack.getTranslationKey();

				if (translationKeys.contains(translationKey)
						|| TranslationStorage.getInstance().getClientTranslation(translationKey).isEmpty()
						|| (meta > 0 && !item.hasSubtypes())
				) break;

				tab.addItem(stack);
				translationKeys.add(translationKey);
			}

			METAS.put(item, meta);
		} else {
			for (int i = 0; i < METAS.get(item); i++) {
				ItemStack stack = new ItemStack(item, 1, i);
				tab.addItem(stack);
			}
		}
	}
}
