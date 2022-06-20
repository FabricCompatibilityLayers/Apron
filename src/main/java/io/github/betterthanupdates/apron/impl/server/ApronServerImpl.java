package io.github.betterthanupdates.apron.impl.server;

import java.util.List;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resource.language.Internationalization;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import io.github.betterthanupdates.apron.api.ApronApi;

public final class ApronServerImpl implements ApronApi, DedicatedServerModInitializer {
	@ApiStatus.Internal
	public static ApronApi instance;

	private static final MinecraftServer server = (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();

	@Override
	public @Nullable Runnable getGame() {
		return (MinecraftServer) FabricLoaderImpl.INSTANCE.getGameInstance();
	}

	/**
	 * @return the overworld
	 */
	@Override
	public @Nullable World getWorld() {
		MinecraftServer server = (MinecraftServer) getGame();

		if (server != null) {
			return server.getWorld(0);
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PlayerEntity> getPlayers() {
		MinecraftServer server = (MinecraftServer) getGame();

		if (server != null) {
			return server.serverPlayerConnectionManager.players;
		}

		return null;
	}

	/**
	 * @return the first player in the player list
	 */
	@Override
	public @Nullable PlayerEntity getPlayer() {
		return getPlayers().get(0);
	}

	@Override
	public String getModLoaderVersion() {
		return "ModLoader Server Beta 1.6.6v4";
	}

	@Override
	public String translate(String key) {
		return Internationalization.translate(key);
	}

	@Override
	public String translate(String key, Object... args) {
		return Internationalization.translate(key, args);
	}

	/**
	 * This will set the {@link #instance} so that this implementation can be used by the API.
	 */
	@Override
	public void onInitializeServer() {
		ApronApi.super.onInitialize();
		instance = this;
	}

	@Override
	public boolean isClient() {
		return false;
	}

	@Override
	public String getUsedItemSpritesString() {
		return "1111111111111111111111111111111111111101111111011111111111111001111111111111111111111011111010111111100110000011111110000000001111111001100000110000000100000011000000010000001100000000000000110000000000000000000000000000000000000000000000001100000000000000";
	}

	@Override
	public String getUsedTerrainSpritesString() {
		return "1111111111111111111111111111110111111111111111111111110111111111111111111111000111111011111111111111001111000000111111111111100011111111000010001111011110000000111111000000000011111100000000001111000000000111111000000000001101000000000001111111111111000011";
	}
}
