package io.github.betterthanupdates.babricated.api;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import io.github.betterthanupdates.babricated.impl.client.ClientUtil;
import io.github.betterthanupdates.babricated.impl.server.ServerUtil;

public interface BabricatedApi extends ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
	@NotNull
	static BabricatedApi getInstance() {
		switch (FabricLoader.getInstance().getEnvironmentType()) {
			case SERVER:
				return ServerUtil.instance;
			case CLIENT:
			default:
				return ClientUtil.instance;
		}
	}

	@Override
	default void onInitialize() {
	}

	@Override
	default void onInitializeClient() {
	}

	@Override
	default void onInitializeServer() {
	}

	@Nullable Runnable getGame();

	@Nullable World getWorld();

	List<PlayerEntity> getPlayers();

	@Nullable PlayerEntity getPlayer();
}
