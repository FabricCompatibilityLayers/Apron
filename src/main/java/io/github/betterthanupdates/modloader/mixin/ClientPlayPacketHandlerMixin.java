package io.github.betterthanupdates.modloader.mixin;

import modloadermp.ModLoaderMp;
import modloadermp.NetClientHandlerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientPlayPacketHandler;
import net.minecraft.packet.play.EntitySpawnS2CPacket;
import net.minecraft.packet.play.OpenContainerS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayPacketHandler.class)
public abstract class ClientPlayPacketHandlerMixin {
	@Shadow private ClientWorld world;

	@Shadow protected abstract Entity getEntity(int i);

	/**
	 * @author Risugami
	 * @reason ModLoaderMP can handle spawning custom entities
	 * TODO(halotroop2288): rewrite as an {@link Inject} Mixin
	 */
	@Inject(method = "onEntitySpawn(Lnet/minecraft/packet/play/EntitySpawnS2CPacket;)V",
			at = @At(value = "CONSTANT", args = "nullValue=true"), locals = LocalCapture.PRINT
	)
	public void modloader$onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
		NetClientHandlerEntity netclienthandlerentity = ModLoaderMp.HandleNetClientHandlerEntities(packet.type);

//		if (netclienthandlerentity != null) {
//			try {
//				entity = netclienthandlerentity.entityClass
//						.getConstructor(World.class, Double.TYPE, Double.TYPE, Double.TYPE)
//						.newInstance(this.world, d, d1, d2);
//				if (netclienthandlerentity.entityHasOwner) {
//					Field field = netclienthandlerentity.entityClass.getField("owner");
//					if (!Entity.class.isAssignableFrom(field.getType())) {
//						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
//					}
//
//					Entity entity1 = this.getEntity(packet.flag);
//					if (entity1 == null) {
//						ModLoaderMp.Log("Received spawn packet for entity with owner, but owner was not found.");
//					} else {
//						if (!field.getType().isAssignableFrom(entity1.getClass())) {
//							throw new Exception(
//									String.format("Tried to assign an entity of type %s to entity owner, which is of type %s.", entity1.getClass(), field.getType())
//							);
//						}
//
//						field.set(entity, entity1);
//					}
//				}
//			} catch (Exception var12) {
//				ModLoader.getLogger().throwing("NetClientHandler", "handleVehicleSpawn", var12);
//				ModLoader.ThrowException(String.format("Error initializing entity of type %s.", packet.type), var12);
//			}
//		}
//
//		if (entity != null) {
//			entity.clientX = packet.x;
//			entity.clientY = packet.y;
//			entity.clientZ = packet.z;
//			entity.yaw = 0.0F;
//			entity.pitch = 0.0F;
//			entity.entityId = packet.entityId;
//			this.world.method_1495(packet.entityId, entity);
//			if (packet.flag > 0) {
//				if (packet.type == 60) {
//					Entity var9 = this.getEntity(packet.flag);
//					if (var9 instanceof LivingEntity) {
//						((ArrowEntity)entity).owner = (LivingEntity)var9;
//					}
//				}
//
//				entity.setVelocity((double)packet.field_1667 / 8000.0, (double)packet.field_1668 / 8000.0, (double)packet.field_1669 / 8000.0);
//			}
//		}
	}

	/**
	 * @author Risugami
	 * @reason ModLoaderMP can handle custom container GUIs
	 */
	@Inject(method = "onOpenContainer", at = @At("TAIL"))
	private void modloader$openContainer(OpenContainerS2CPacket packet, CallbackInfo ci) {
		int type = packet.inventoryType;
		switch (type) {
			case 0: case 1: case 2: case 3:
				return;
			default:
				ModLoaderMp.HandleGUI(packet);
		}
	}
}
