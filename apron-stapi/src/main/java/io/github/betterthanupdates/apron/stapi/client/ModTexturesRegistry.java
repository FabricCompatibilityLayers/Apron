package io.github.betterthanupdates.apron.stapi.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import net.modificationstation.stationapi.api.client.texture.atlas.AtlasSource;
import net.modificationstation.stationapi.api.client.texture.atlas.SingleAtlasSource;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class ModTexturesRegistry {
	private final String folderName;
	public final Map<Integer, Identifier> INDEX_TO_FAKE_ID = new HashMap<>();
	public final Map<String, Identifier> PATH_TO_FAKE_ID = new HashMap<>();

	public final List<AtlasSource> GENERATED_ATLASES = new ArrayList<>();

	public ModTexturesRegistry(String folderName) {
		this.folderName = folderName;
	}

	public void registerTexture(int id, String texture) {
		Namespace modID = ApronStAPICompat.getModID();

		Identifier identifier = modID.id(this.folderName + "/" + texture.replace("/", "__"));

		INDEX_TO_FAKE_ID.put(id, identifier);
		PATH_TO_FAKE_ID.put(texture, identifier);

		GENERATED_ATLASES.add(new SingleAtlasSource(Identifier.of(texture), Optional.of(identifier)));
	}
}
