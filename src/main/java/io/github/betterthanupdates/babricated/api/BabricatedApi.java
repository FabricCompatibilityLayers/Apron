package io.github.betterthanupdates.babricated.api;

import io.github.betterthanupdates.babricated.impl.client.ClientUtil;
import io.github.betterthanupdates.babricated.impl.server.ServerUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BabricatedApi extends ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
	@NotNull
	static BabricatedApi getInstance() {
		switch (FabricLoader.getInstance().getEnvironmentType()) {
			default:
			case CLIENT:
				return ClientUtil.instance;
			case SERVER:
				return ServerUtil.instance;
		}
	}

	@Override
	default void onInitialize() {}

	@Override
	default void onInitializeClient() {}

	@Override
	default void onInitializeServer() {}

	@Nullable Runnable getGame();
	@Nullable World getWorld();
	List<PlayerEntity> getPlayers();
	@Nullable PlayerEntity getPlayer();
}
