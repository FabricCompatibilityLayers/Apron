package io.github.betterthanupdates.apron.stapi;

import static io.github.betterthanupdates.apron.stapi.ApronStAPICompat.LOGGER;

import com.google.common.collect.ImmutableList;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;

public class TextureEvent {
	@EventListener
	public void textureEvent(TextureRegisterEvent event) {
		ApronStAPICompat.INDEX_TO_FIXED_ITEM.clear();
		ApronStAPICompat.INDEX_TO_FIXED_BLOCK.clear();

		ApronStAPICompat.getModContents().forEach(entry -> {
			ModContents modContents = entry.getValue();

			ExpandableAtlas itemAtlas = Atlases.getGuiItems();

			modContents.GUI_ITEMS.INDEX_TO_FAKE_ID.forEach((i, identifier) -> {
				Atlas.Sprite sprite = itemAtlas.addTexture(identifier);
				LOGGER.info("Item " + sprite.getId() + " " + i + " -> " + sprite.index);
				ApronStAPICompat.INDEX_TO_FIXED_ITEM.put(i, sprite.index);
			});

			ExpandableAtlas blockAtlas = Atlases.getTerrain();

			modContents.TERRAIN.INDEX_TO_FAKE_ID.forEach((i, identifier) -> {
				Atlas.Sprite sprite = blockAtlas.addTexture(identifier);
				LOGGER.info("Block " + sprite.getId() + " " + i + " -> " + sprite.index);
				ApronStAPICompat.INDEX_TO_FIXED_BLOCK.put(i, sprite.index);
			});
		});

		ApronStAPICompat.SPRITESHEET_MAP.forEach((texturePath, spritesheetInstance) -> {
			spritesheetInstance.ITEMS.clear();
			spritesheetInstance.BLOCKS.clear();

			ExpandableAtlas itemAtlas = Atlases.getGuiItems();

			ImmutableList<Atlas.Sprite> itemSprites = itemAtlas.addSpritesheet(16, spritesheetInstance.HELPER);

			for (int i = 0; i < itemSprites.size(); i++) {
				spritesheetInstance.ITEMS.put(i, itemSprites.get(i).index);
			}

			ExpandableAtlas blockAtlas = Atlases.getTerrain();

			ImmutableList<Atlas.Sprite> blockSprites = blockAtlas.addSpritesheet(16, spritesheetInstance.HELPER);

			for (int i = 0; i < blockSprites.size(); i++) {
				spritesheetInstance.BLOCKS.put(i, blockSprites.get(i).index);
			}
		});
	}
}
