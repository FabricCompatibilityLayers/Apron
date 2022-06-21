package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface ISpawnable {
	@Environment(EnvType.CLIENT)
	default void spawn(ModLoaderPacket packet) {
	}

	@Environment(EnvType.SERVER)
	default ModLoaderPacket getSpawnPacket() {
		return null;
	}
}
