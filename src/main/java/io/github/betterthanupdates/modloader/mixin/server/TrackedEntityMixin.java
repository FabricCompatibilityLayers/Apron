package io.github.betterthanupdates.modloader.mixin.server;

import java.lang.reflect.Field;

import modloader.ModLoader;
import modloadermp.EntityTrackerEntry2;
import modloadermp.ISpawnable;
import modloadermp.ModLoaderMp;
import modloadermp.Packet230ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.TrackedEntity;

@Environment(EnvType.SERVER)
@Mixin(TrackedEntity.class)
public class TrackedEntityMixin {
	@Shadow
	public Entity entityToSync;

	@Inject(method = "method_600", at = @At("HEAD"), cancellable = true)
	private void modloader$method_600(CallbackInfoReturnable<AbstractPacket> cir) {
		EntityTrackerEntry2 entitytrackerentry2 = ModLoaderMp.HandleEntityTrackerEntries(this.entityToSync);

		if (entitytrackerentry2 != null) {
			try {
				if (this.entityToSync instanceof ISpawnable) {
					Packet230ModLoader packet = ((ISpawnable)this.entityToSync).getSpawnPacket();
					packet.modId = "Spawn".hashCode();

					if (entitytrackerentry2.entityId > 127) {
						packet.packetType = entitytrackerentry2.entityId - 256;
					} else {
						packet.packetType = entitytrackerentry2.entityId;
					}

					cir.setReturnValue(packet);
				} else if (!entitytrackerentry2.entityHasOwner) {
					cir.setReturnValue(new EntitySpawnS2CPacket(this.entityToSync, entitytrackerentry2.entityId));
				} else {
					Field field = this.entityToSync.getClass().getField("owner");

					if (Entity.class.isAssignableFrom(field.getType())) {
						Entity entity = (Entity)field.get(this.entityToSync);
						cir.setReturnValue(new EntitySpawnS2CPacket(
								this.entityToSync, entitytrackerentry2.entityId, entity == null ? this.entityToSync.entityId : entity.entityId
						));
					} else {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}
				}
			} catch (Exception var4) {
				ModLoader.getLogger().throwing("EntityTrackerEntry", "getSpawnPacket", var4);
				ModLoader.ThrowException(String.format("Error sending spawn packet for entity of type %s.", this.entityToSync.getClass()), var4);
				cir.setReturnValue(null);
			}
		}
	}
}
