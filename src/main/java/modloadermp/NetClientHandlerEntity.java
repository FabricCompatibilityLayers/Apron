package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.Entity;

import io.github.betterthanupdates.Legacy;

@Legacy
@Environment(EnvType.CLIENT)
public class NetClientHandlerEntity {
	@Legacy
	public boolean entityHasOwner;
	@Legacy
	public Class<? extends Entity> entityClass;

	@Legacy
	public NetClientHandlerEntity(final Class<? extends Entity> entityClass, final boolean entityHasOwner) {
		this.entityHasOwner = entityHasOwner;
		this.entityClass = entityClass;
	}
}
