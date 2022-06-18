package io.github.betterthanupdates.babricated.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.betterthanupdates.babricated.BabricatedForge.LOGGER;

/**
 * Provides quick access to fields in the {@link Minecraft client}.<br>
 * <br>
 * If any method inside this class is called before the client is initialized,
 * something has gone horribly wrong.
 */
@Environment(EnvType.CLIENT)
public class ClientUtil implements ClientModInitializer {
	@NotNull
	private static final Minecraft client = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();

	@NotNull
	public static Minecraft getClient() {
		return client;
	}

	@Nullable
	public static World getWorld() {
		return client.world;
	}

	@Nullable
	public static PlayerEntity getPlayer() {
		return client.player;
	}

	@Nullable
	public static PlayerContainer getPlayerContainer() {
		if (getPlayer() == null || !(getPlayer().container instanceof PlayerContainer)) return null;
		return (PlayerContainer) client.player.playerContainer;
	}

	@NotNull
	public static TextureManager getTextureManager() {
		return client.textureManager;
	}

	/**
	 * This will print a line when this class officially becomes usable.<br>
	 * If it's used any time before then, it's <i>probably</i> going to break something.
	 */
	@Override
	public void onInitializeClient() {
		LOGGER.info("Client Util now available.");
	}
}
