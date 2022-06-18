package io.github.betterthanupdates.babricated.impl.client;

import io.github.betterthanupdates.babricated.api.BabricatedApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.List;

import static io.github.betterthanupdates.babricated.BabricatedForge.LOGGER;

/**
 * Provides quick access to fields in the {@link Minecraft client}.<br>
 * <br>
 * If any method inside this class is called before the client is initialized,
 * something has gone horribly wrong.
 */
public final class ClientUtil implements BabricatedApi {
	@ApiStatus.Internal
	public static ClientUtil instance;

	@Nullable
	private static Minecraft client = null;

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
	 * This will print a line when this class officially becomes usable.<br>
	 * If it's used any time before then, it's <i>probably</i> going to break something.
	 */
	@Override
	public void onInitializeClient() {
		if (instance == null) {
			instance = this;
			client = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
			LOGGER.info("Client Util is now ready");
		} else {
			throw new RuntimeException("Client Util initialized more than once!");
		}
	}
}
