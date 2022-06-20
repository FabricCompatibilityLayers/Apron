package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface ISpawnable {
	@Environment(EnvType.CLIENT)
	default void spawn(Packet230ModLoader packet) {}
	@Environment(EnvType.SERVER)
	default Packet230ModLoader getSpawnPacket() {
		return null;
	}
}
