package io.github.fabriccompatibilitylayers.modloader.mixin.server;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import modloader.ModLoader;
import modloadermp.EntityTrackerEntry2;
import modloadermp.ISpawnable;
import modloadermp.ModLoaderMp;
import modloadermp.Packet230ModLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.entity.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
	@Shadow
	public Entity currentTrackedEntity;

	@WrapMethod(method = "createAddEntityPacket")
	private Packet modloadermp$HandleEntityTrackerEntries(Operation<Packet> original) {
		EntityTrackerEntry2 entitytrackerentry2 = ModLoaderMp.HandleEntityTrackerEntries(this.currentTrackedEntity);
		if (entitytrackerentry2 != null) {
			try {
				if (this.currentTrackedEntity instanceof ISpawnable) {
					Packet230ModLoader packet = ((ISpawnable)this.currentTrackedEntity).getSpawnPacket();
					packet.modId = "Spawn".hashCode();
					if (entitytrackerentry2.entityId > 127) {
						packet.packetType = entitytrackerentry2.entityId - 256;
					} else {
						packet.packetType = entitytrackerentry2.entityId;
					}

					return packet;
				} else if (!entitytrackerentry2.entityHasOwner) {
					return new EntitySpawnS2CPacket(this.currentTrackedEntity, entitytrackerentry2.entityId);
				} else {
					Field field = this.currentTrackedEntity.getClass().getField("owner");
					if (Entity.class.isAssignableFrom(field.getType())) {
						Entity entity = (Entity)field.get(this.currentTrackedEntity);
						return new EntitySpawnS2CPacket(this.currentTrackedEntity, entitytrackerentry2.entityId, entity == null ? this.currentTrackedEntity.id : entity.id);
					} else {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}
				}
			} catch (Exception exception) {
				ModLoader.getLogger().throwing("EntityTrackerEntry", "getSpawnPacket", exception);
				ModLoader.ThrowException(String.format("Error sending spawn packet for entity of type %s.", this.currentTrackedEntity.getClass()), exception);
				return null;
			}
		} else {
			return original.call();
		}
	}
}
