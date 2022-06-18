package io.github.betterthanupdates.babricated.impl.server;

import java.util.List;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import io.github.betterthanupdates.babricated.BabricatedForge;
import io.github.betterthanupdates.babricated.api.BabricatedApi;

public class ServerUtil implements BabricatedApi {
	@ApiStatus.Internal
	public static BabricatedApi instance;

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
	public void onInitializeServer() {
		if (instance == null) {
			instance = this;
			BabricatedForge.LOGGER.info("Server Util is now ready");
		} else {
			throw new RuntimeException("Server Util initialized more than once!");
		}
	}
}
