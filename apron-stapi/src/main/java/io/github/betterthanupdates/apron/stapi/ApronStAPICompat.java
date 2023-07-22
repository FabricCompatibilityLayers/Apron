package io.github.betterthanupdates.apron.stapi;

import forge.ITextureProvider;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.client.texture.atlas.AtlasSource;
import net.modificationstation.stationapi.api.client.texture.atlas.UnstitchAtlasSource;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.betterthanupdates.apron.LifecycleUtils.CURRENT_MOD;

@Entrypoint(eventBus = @EventBusPolicy(registerStatic = false, registerInstance = false))
public class ApronStAPICompat {
	@Entrypoint.Logger
	public static final Logger LOGGER = Null.get();

	public static boolean isModLoaderTime() {
		return CURRENT_MOD != null;
	}

	public static ModID getModID() {
		return ModID.of(CURRENT_MOD);
	}

	private static final Map<ModID, ModContents> MOD_CONTENTS = new HashMap<>();

	public static ModContents getModContent(ModID modID) {
		if (!MOD_CONTENTS.containsKey(modID)) {
			MOD_CONTENTS.put(modID, new ModContents());
		}

		return MOD_CONTENTS.get(modID);
	}

	public static ModContents getModContent() {
		return getModContent(getModID());
	}

	public static Set<Map.Entry<ModID, ModContents>> getModContents() {
		return MOD_CONTENTS.entrySet();
	}

	private static int terrainIndex = 3000;
	private static int itemsIndex = 3000;

	public static final Map<Integer, Integer> INDEX_TO_FIXED_ITEM = new HashMap<>();
	public static final Map<Integer, Integer> INDEX_TO_FIXED_BLOCK = new HashMap<>();

	public static final Map<String, SpritesheetInstance> SPRITESHEET_MAP = new HashMap<>();

	public static final List<AtlasSource> ATLAS_SOURCE_LIST = new ArrayList<>();

	public static int registerTextureOverride(String target, String textureFile) {
		int textureIndex = -1;

		if (!textureFile.startsWith("/")) textureFile = "/" + textureFile;

		if ("/terrain.png".equals(target)) {
			textureIndex = ++terrainIndex;
			getModContent().TERRAIN.registerTexture(textureIndex, textureFile);
		} else if ("/gui/items.png".equals(target)) {
			textureIndex = ++itemsIndex;
			getModContent().GUI_ITEMS.registerTexture(textureIndex, textureFile);
		}

		return textureIndex;
	}

	public static void preloadTexture(String texturePath) {
		if (!texturePath.startsWith("/")) texturePath = "/" + texturePath;

		SPRITESHEET_MAP.put(texturePath, new SpritesheetInstance());
		SPRITESHEET_MAP.get(texturePath).HELPER = new ItemSpritesheet(texturePath);

		String idPath = "block/" + texturePath.replace("/", "__");

		int divisorSize = 16;
		int spriteSize = 1;

		Identifier texturePathId = Identifier.of(texturePath);

		List<UnstitchAtlasSource.Region> regions = new ArrayList<>();

		for (int y = 0; y < divisorSize; y++) for (int x = 0; x < divisorSize; x++) {
			int textureIndex = y * divisorSize + x;

			Identifier identifier = getModID().id(idPath + "_" + y + "_" + x);

			SPRITESHEET_MAP.get(texturePath).INDEX_TO_ID.put(textureIndex, identifier);

			regions.add(new UnstitchAtlasSource.Region(identifier, x, y, spriteSize, spriteSize));
		}

		ATLAS_SOURCE_LIST.add(new UnstitchAtlasSource(texturePathId, regions, divisorSize, divisorSize));

		LOGGER.info("Pre-Loaded texture atlas: " + texturePathId + " for mod " + getModID());
	}

	public static int fixItemTexture(int original, Item item) {
		if (item instanceof ITextureProvider textureProvider) {
			String textureFile = textureProvider.getTextureFile();

			if (!textureFile.startsWith("/")) textureFile = "/" + textureFile;

			SpritesheetInstance itemSpritesheet = ApronStAPICompat.SPRITESHEET_MAP.get(textureFile);

			if (itemSpritesheet != null) {
				if (item instanceof BlockItem) {
					if (itemSpritesheet.BLOCKS.containsKey(original)) {
						return itemSpritesheet.BLOCKS.get(original);
					}
				} else if (itemSpritesheet.ITEMS.containsKey(original)) {
					return itemSpritesheet.ITEMS.get(original);
				}
			}
		} else {
			if (item instanceof BlockItem) {
				if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(original)) {
					return ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(original);
				}
			} else if (ApronStAPICompat.INDEX_TO_FIXED_ITEM.containsKey(original)) {
				return ApronStAPICompat.INDEX_TO_FIXED_ITEM.get(original);
			}
		}

		return original;
	}

	public static int fixBlockTexture(int texture, Block block) {
		if (block instanceof ITextureProvider textureProvider) {
			String textureFile = textureProvider.getTextureFile();

			if (!textureFile.startsWith("/")) textureFile = "/" + textureFile;

			SpritesheetInstance spritesheetInstance = ApronStAPICompat.SPRITESHEET_MAP.get(textureFile);

			if (spritesheetInstance != null) {
				if (spritesheetInstance.BLOCKS.containsKey(texture)) {
					return spritesheetInstance.BLOCKS.get(texture);
				}
			}
		} else if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(texture)) {
			return ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(texture);
		}

		return texture;
	}
}
