package io.github.betterthanupdates.apron.stapi;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.github.betterthanupdates.apron.LifecycleUtils.CURRENT_MOD;

public class ApronStAPICompat {
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

	public static int registerTextureOverride(String target, String textureFile) {
		int textureIndex = -1;

		if ("/terrain.png".equals(target)) {
			textureIndex = ++terrainIndex;
			getModContent().TERRAIN.registerTexture(textureIndex, textureFile);
//			ExpandableAtlas atlas = Atlases.getTerrain();
		} else if ("/gui/items.png".equals(target)) {
			textureIndex = ++itemsIndex;
			getModContent().GUI_ITEMS.registerTexture(textureIndex, textureFile);
//			ExpandableAtlas atlas = Atlases.getGuiItems();
		}

		return textureIndex;
	}

	public static void preloadTexture(String texturePath) {

	}
}
