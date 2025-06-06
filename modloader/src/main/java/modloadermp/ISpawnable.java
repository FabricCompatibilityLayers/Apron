package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface ISpawnable {
	@Environment(EnvType.CLIENT)
	void spawn(Packet230ModLoader var1);

	@Environment(EnvType.SERVER)
	Packet230ModLoader getSpawnPacket();
}
