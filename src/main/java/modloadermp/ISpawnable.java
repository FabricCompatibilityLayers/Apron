package modloadermp;

import io.github.betterthanupdates.Legacy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Legacy
public interface ISpawnable {
	@Legacy
	@Environment(EnvType.CLIENT)
	default void spawn(ModLoaderPacket packet) {
	}

	@Legacy
	@Environment(EnvType.SERVER)
	default ModLoaderPacket getSpawnPacket() {
		return null;
	}
}
