package io.github.betterthanupdates.apron.impl.client;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import io.github.betterthanupdates.apron.api.ApronApi;

/**
 * Provides quick access to fields in the {@link Minecraft client}.<br>
 * <br>
 * If any method inside this class is called before the client is initialized,
 * something has gone horribly wrong.
 */
public final class ApronClientImpl implements ApronApi, ClientModInitializer {
	@ApiStatus.Internal
	public static ApronClientImpl instance;

	@Nullable
	private static Minecraft client = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();

	@Nullable
	public World getWorld() {
		Minecraft client = (Minecraft) getGame();

		if (client != null) {
			return ((Minecraft) getGame()).world;
		}

		return null;
	}

	@Override
	public List<@Nullable PlayerEntity> getPlayers() {
		PlayerEntity player = getPlayer();

		if (player != null) {
			return ImmutableList.of(player);
		}

		return ImmutableList.of();
	}

	@Nullable
	public PlayerEntity getPlayer() {
		Minecraft client = (Minecraft) getGame();

		if (client != null) {
			return ((Minecraft) getGame()).player;
		}

		return null;
	}

	@Nullable
	public TextureManager getTextureManager() {
		Minecraft client = (Minecraft) getGame();

		if (client != null) {
			return ((Minecraft) getGame()).textureManager;
		}

		return null;
	}

	@Override
	public Runnable getGame() {
		return client;
	}

	/**
	 * This will set the {@link #instance} so that this implementation can be used by the API.
	 */
	@Override
	public void onInitializeClient() {
		ApronApi.super.onInitialize();
		instance = this;
	}
}
