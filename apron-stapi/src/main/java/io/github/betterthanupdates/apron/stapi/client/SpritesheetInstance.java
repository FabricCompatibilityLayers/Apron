package io.github.betterthanupdates.apron.stapi.client;

import java.util.HashMap;
import java.util.Map;

import net.modificationstation.stationapi.api.registry.Identifier;

public class SpritesheetInstance {
	public final Map<Integer, Integer> ITEMS = new HashMap<>();
	public final Map<Integer, Integer> BLOCKS = new HashMap<>();
	public final Map<Integer, Identifier> INDEX_TO_ID = new HashMap<>();

	public ItemSpritesheet HELPER = null;
}
