package modloadermp;

import net.minecraft.entity.Entity;

public class NetClientHandlerEntity {
	public boolean entityHasOwner;
	public Class<? extends Entity> entityClass;
	
	public NetClientHandlerEntity(final Class<? extends Entity> entityClass, final boolean entityHasOwner) {
		this.entityHasOwner = entityHasOwner;
		this.entityClass = entityClass;
	}
}
