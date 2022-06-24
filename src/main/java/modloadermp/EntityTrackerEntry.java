package modloadermp;

import io.github.betterthanupdates.Legacy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Legacy
@Environment(EnvType.SERVER)
public class EntityTrackerEntry {
	@Legacy
	public int entityId;
	@Legacy
	public boolean entityHasOwner;

	@Legacy
	public EntityTrackerEntry(int i, boolean flag) {
		this.entityId = i;
		this.entityHasOwner = flag;
	}
}
