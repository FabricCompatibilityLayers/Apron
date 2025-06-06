package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NetClientHandlerEntity {
	public Class entityClass = null;
	public boolean entityHasOwner = false;

	public NetClientHandlerEntity(Class class1, boolean flag) {
		this.entityClass = class1;
		this.entityHasOwner = flag;
	}
}
