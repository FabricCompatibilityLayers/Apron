package io.github.betterthanupdates.apron.impl.server;

import java.util.List;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

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

	/**
	 * This will set the {@link #instance} so that this implementation can be used by the API.
	 */
	@Override
	public void onInitializeServer() {
		ApronApi.super.onInitialize();
		instance = this;
	}
}
