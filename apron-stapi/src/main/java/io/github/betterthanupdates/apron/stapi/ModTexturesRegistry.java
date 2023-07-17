package io.github.betterthanupdates.apron.stapi;

import java.util.HashMap;
import java.util.Map;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

public class ModTexturesRegistry {
	public final Map<Integer, Identifier> INDEX_TO_FAKE_ID = new HashMap<>();
	public final Map<String, Identifier> PATH_TO_FAKE_ID = new HashMap<>();
	public void registerTexture(int id, String texture) {
		ModID modID = ApronStAPICompat.getModID();

		Identifier identifier = modID.id(texture.replace("/", "__"));

		INDEX_TO_FAKE_ID.put(id, identifier);
		PATH_TO_FAKE_ID.put(texture, identifier);
	}
}
