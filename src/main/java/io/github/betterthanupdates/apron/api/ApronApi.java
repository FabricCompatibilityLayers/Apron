package io.github.betterthanupdates.apron.api;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import io.github.betterthanupdates.apron.Apron;
import io.github.betterthanupdates.apron.impl.client.ApronClientImpl;
import io.github.betterthanupdates.apron.impl.server.ApronServerImpl;

public interface ApronApi extends ModInitializer {
	@NotNull
	static ApronApi getInstance() {
		switch (Apron.getEnvironment()) {
			case SERVER:
				return ApronServerImpl.instance;
			case CLIENT:
			default:
				return ApronClientImpl.instance;
		}
	}

	/**
	 * @return the list of biomes shipped in vanilla Minecraft.
	 */
	default Biome[] getStandardBiomes() {
		List<Biome> biomes = new ArrayList<>();

		// Vanilla Biomes
		biomes.add(Biome.RAINFOREST);
		biomes.add(Biome.SWAMPLAND);
		biomes.add(Biome.SEASONAL_FOREST);
		biomes.add(Biome.FOREST);
		biomes.add(Biome.SAVANNA);
		biomes.add(Biome.SHRUBLAND);
		biomes.add(Biome.TAIGA);
		biomes.add(Biome.DESERT);
		biomes.add(Biome.PLAINS);
		biomes.add(Biome.ICE_DESERT);
		biomes.add(Biome.TUNDRA);

		return biomes.toArray(new Biome[0]);
	}

	/**
	 * This will print a line when this class officially becomes usable.<br>
	 * If it's used any time before then, it's <i>probably</i> going to break something.
	 */
	@Override
	@MustBeInvokedByOverriders
	default void onInitialize() {
		if (getInstance() != null) {
			throw new RuntimeException("Tried to create two Apron APIs at once!");
		}

		Apron.LOGGER.info("Apron API is now ready for use!");
	}

	@Nullable Runnable getGame();

	@Nullable World getWorld();

	List<PlayerEntity> getPlayers();

	@Nullable PlayerEntity getPlayer();

	String getModLoaderVersion();

	String getModLoaderMPVersion();

	String translate(String key);

	String translate(String key, Object... args);

	boolean isClient();

	String getUsedItemSpritesString();

	String getUsedTerrainSpritesString();

	default Logger getLogger(String name) {
		return Logger.get(Apron.NAME, name);
	}
}
