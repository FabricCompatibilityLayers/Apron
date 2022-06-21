package io.github.betterthanupdates.modloader.mixin.server;

import java.lang.reflect.Field;

import modloader.ModLoader;
import modloadermp.EntityTrackerEntry;
import modloadermp.ISpawnable;
import modloadermp.ModLoaderMp;
import modloadermp.ModLoaderPacket;
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
		EntityTrackerEntry trackerEntry = ModLoaderMp.HandleEntityTrackerEntries(this.entityToSync);

		if (trackerEntry != null) {
			try {
				if (this.entityToSync instanceof ISpawnable) {
					ModLoaderPacket packet = ((ISpawnable) this.entityToSync).getSpawnPacket();
					packet.modId = "Spawn".hashCode();

					if (trackerEntry.entityId > 127) {
						packet.packetType = trackerEntry.entityId - 256;
					} else {
						packet.packetType = trackerEntry.entityId;
					}

					cir.setReturnValue(packet);
				} else if (!trackerEntry.entityHasOwner) {
					cir.setReturnValue(new EntitySpawnS2CPacket(this.entityToSync, trackerEntry.entityId));
				} else {
					Field field = this.entityToSync.getClass().getField("owner");

					if (Entity.class.isAssignableFrom(field.getType())) {
						Entity entity = (Entity) field.get(this.entityToSync);
						cir.setReturnValue(new EntitySpawnS2CPacket(
								this.entityToSync, trackerEntry.entityId, entity == null ? this.entityToSync.entityId : entity.entityId
						));
					} else {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}
				}
			} catch (Exception e) {
				ModLoader.getLogger().throwing("EntityTrackerEntry", "getSpawnPacket", e);
				ModLoader.ThrowException(String.format("Error sending spawn packet for entity of type %s.", this.entityToSync.getClass()), e);
				cir.setReturnValue(null);
			}
		}
	}
}
