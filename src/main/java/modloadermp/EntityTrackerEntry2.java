package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class EntityTrackerEntry2 {
	public int entityId = -1;
	public boolean entityHasOwner = false;

	public EntityTrackerEntry2(int i, boolean flag) {
		this.entityId = i;
		this.entityHasOwner = flag;
	}
}
