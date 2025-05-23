package io.github.betterthanupdates.apron.impl.client;

import java.util.List;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
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
public final class ApronClientImpl implements ApronApi {
	@Nullable
	private Minecraft client;

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

	@Override
	public String getModLoaderVersion() {
		return "ModLoader Beta 1.7.3";
	}

	@Override
	public String getModLoaderMPVersion() {
		return "Beta 1.7.3";
	}

	@Override
	public String translate(String key) {
		return TranslationStorage.getInstance().get(key);
	}

	@Override
	public String translate(String key, Object... args) {
		return TranslationStorage.getInstance().get(key, args);
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
		return this.client;
	}

	/**
	 * This will set the {@link #instance} so that this implementation can be used by the API.
	 */
	@Override
	public void onInitialized() {
		ApronApi.super.onInitialized();
		this.client = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
	}

	@Override
	public boolean isClient() {
		return true;
	}

	@Override
	public String getUsedItemSpritesString() {
		return "1111111111111111111111111111111111111101111111011111111111111001111111111111111111111111111011111111100110000011111110000000001111111001100000110000000100000011000000010000001100000000000000110000000000000000000000000000000000000000000000001100000000000000";
	}

	@Override
	public String getUsedTerrainSpritesString() {
		return "1111111111111111111111111111110111111111111111111111110111111111111111111111000111111011111111111111001111111110111111111111100011111111000010001111011110000000111111000000000011111100000000001111000000000111111000000000001101000000000001111111111111000011";
	}
}
