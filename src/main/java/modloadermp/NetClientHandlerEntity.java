package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class NetClientHandlerEntity {
	public boolean entityHasOwner;
	public Class<? extends Entity> entityClass;

	public NetClientHandlerEntity(final Class<? extends Entity> entityClass, final boolean entityHasOwner) {
		this.entityHasOwner = entityHasOwner;
		this.entityClass = entityClass;
	}
}
