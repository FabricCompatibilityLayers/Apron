package io.github.betterthanupdates.modloader.mixin.server;

import java.lang.reflect.Field;

import modloader.ModLoader;
import modloadermp.EntityTrackerEntry;
import modloadermp.ISpawnable;
import modloadermp.ModLoaderMp;
import modloadermp.ModLoaderPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_174;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

@Environment(EnvType.SERVER)
@Mixin(class_174.class)
public class TrackedEntityMixin {
	@Shadow
	public Entity field_597;

	@Inject(method = "method_600", at = @At("HEAD"), cancellable = true)
	private void modloader$method_600(CallbackInfoReturnable<Packet> cir) {
		EntityTrackerEntry trackerEntry = ModLoaderMp.HandleEntityTrackerEntries(this.field_597);

		if (trackerEntry != null) {
			try {
				if (this.field_597 instanceof ISpawnable) {
					ModLoaderPacket packet = ((ISpawnable) this.field_597).getSpawnPacket();
					packet.modId = "Spawn".hashCode();

					if (trackerEntry.entityId > 127) {
						packet.packetType = trackerEntry.entityId - 256;
					} else {
						packet.packetType = trackerEntry.entityId;
					}

					cir.setReturnValue(packet);
				} else if (!trackerEntry.entityHasOwner) {
					cir.setReturnValue(new EntitySpawnS2CPacket(this.field_597, trackerEntry.entityId));
				} else {
					Field field = this.field_597.getClass().getField("owner");

					if (Entity.class.isAssignableFrom(field.getType())) {
						Entity entity = (Entity) field.get(this.field_597);
						cir.setReturnValue(new EntitySpawnS2CPacket(
								this.field_597, trackerEntry.entityId, entity == null ? this.field_597.id : entity.id
						));
					} else {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}
				}
			} catch (Exception e) {
				ModLoader.getLogger().throwing("EntityTrackerEntry", "getSpawnPacket", e);
				ModLoader.ThrowException(String.format("Error sending spawn packet for entity of type %s.", this.field_597.getClass()), e);
				cir.setReturnValue(null);
			}
		}
	}
}
