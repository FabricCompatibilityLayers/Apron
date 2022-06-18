package io.github.betterthanupdates.babricated;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static io.github.betterthanupdates.babricated.BabricatedForge.LOGGER;

/**
 * This should be safe to reference from either environment.
 */
public class SafeUtil implements ModInitializer {
	private static final ClassLoader CLASS_LOADER = SafeUtil.class.getClassLoader();

	/**
	 * Gets the current instance of the game from Fabric,
	 * whether it be on client or server.
	 * @return null if game hasn't started
	 */
	@Nullable
	public static Runnable getGame() {
		return (Runnable) FabricLoaderImpl.INSTANCE.getGameInstance();
	}

	/**
	 * @return
	 *      On client: The player's current world.<br>
	 *      On server: The server's Overworld.
	 */
	@Nullable
	public static World getWorld() {
		return getWorld(null);
	}

	/**
	 * @param player The player to get a world for.
	 *
	 * @return
	 *      On client: The player's current world.<br>
	 *      On server: The player's current world, or the overworld as a fallback.
	 */
	@Nullable
	public static World getWorld(@Nullable PlayerEntity player) {
		if (player != null) {
			return player.world;
		}

		switch (FabricLoader.getInstance().getEnvironmentType()) {
			case SERVER:
				try {
					Class<?> serverClass = CLASS_LOADER.loadClass("net.minecraft.server.MinecraftServer");
					if (serverClass != null) {
						MinecraftServer server = (MinecraftServer) getGame();
						if (server != null) {
							server.getWorld(0);
						}
					}
				} catch (ClassNotFoundException ignored) {
					LOGGER.error("Wrong environment!");
					return null;
				}
				break;
			case CLIENT:
				try {
					Class<?> clientClass = CLASS_LOADER.loadClass("net.minecraft.client.Minecraft");
					if (clientClass != null) {
						Minecraft client = (Minecraft) getGame();
						if (client != null) {
							return client.world;
						}
					}
				} catch (ClassNotFoundException e) {
					LOGGER.error("Wrong environment!");
					return null;
				}
		}

		return null;
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Safe Util is now ready for use.");
	}
}
