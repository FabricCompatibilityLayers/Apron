package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class EntityTrackerEntry {
	public int entityId;
	public boolean entityHasOwner;

	public EntityTrackerEntry(int i, boolean flag) {
		this.entityId = i;
		this.entityHasOwner = flag;
	}
}
