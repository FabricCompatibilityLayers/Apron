package io.github.betterthanupdates.apron.stapi;

import net.modificationstation.stationapi.api.client.texture.SpritesheetHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class ItemSpritesheet implements SpritesheetHelper {
	private final String texturePath;
	protected ItemSpritesheet(String texturePath) {
		this.texturePath = texturePath;
	}

	@Override
	public Identifier generateIdentifier(int textureIndex) {
		return ApronStAPICompat.SPRITESHEET_MAP.get(texturePath).INDEX_TO_ID.get(textureIndex);
	}

	@Override
	public BiTuple<Integer, Integer> getResolutionMultiplier(int textureIndex) {
		return DEFAULT_RESOLUTION_MULTIPLIER;
	}
}
