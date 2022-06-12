package io.github.betterthanupdates.modloader.mixin;

import modloader.ModLoader;
import modloadermp.ModLoaderMp;
import modloadermp.NetClientHandlerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.ThrownEggEntity;
import net.minecraft.network.ClientPlayPacketHandler;
import net.minecraft.packet.play.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

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
	@Overwrite
	public void onEntitySpawn(EntitySpawnS2CPacket packet) {
		double d = (double)packet.x / 32.0;
		double d1 = (double)packet.y / 32.0;
		double d2 = (double)packet.z / 32.0;
		Entity obj = null;
		if (packet.type == 10) {
			obj = new ChestMinecartEntity(this.world, d, d1, d2, 0);
		}

		if (packet.type == 11) {
			obj = new ChestMinecartEntity(this.world, d, d1, d2, 1);
		}

		if (packet.type == 12) {
			obj = new ChestMinecartEntity(this.world, d, d1, d2, 2);
		}

		if (packet.type == 90) {
			obj = new FishHookEntity(this.world, d, d1, d2);
		}

		if (packet.type == 60) {
			obj = new ArrowEntity(this.world, d, d1, d2);
		}

		if (packet.type == 61) {
			obj = new SnowballEntity(this.world, d, d1, d2);
		}

		if (packet.type == 63) {
			obj = new FireballEntity(
					this.world,
					d,
					d1,
					d2,
					(double)packet.field_1667 / 8000.0,
					(double)packet.field_1668 / 8000.0,
					(double)packet.field_1669 / 8000.0
			);
			packet.flag = 0;
		}

		if (packet.type == 62) {
			obj = new ThrownEggEntity(this.world, d, d1, d2);
		}

		if (packet.type == 1) {
			obj = new BoatEntity(this.world, d, d1, d2);
		}

		if (packet.type == 50) {
			obj = new PrimedTntEntity(this.world, d, d1, d2);
		}

		if (packet.type == 70) {
			obj = new FallingBlockEntity(this.world, d, d1, d2, Block.SAND.id);
		}

		if (packet.type == 71) {
			obj = new FallingBlockEntity(this.world, d, d1, d2, Block.GRAVEL.id);
		}

		NetClientHandlerEntity netclienthandlerentity = ModLoaderMp.HandleNetClientHandlerEntities(packet.type);
		if (netclienthandlerentity != null) {
			try {
				obj = netclienthandlerentity.entityClass
						.getConstructor(World.class, Double.TYPE, Double.TYPE, Double.TYPE)
						.newInstance(this.world, d, d1, d2);
				if (netclienthandlerentity.entityHasOwner) {
					Field field = netclienthandlerentity.entityClass.getField("owner");
					if (!Entity.class.isAssignableFrom(field.getType())) {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}

					Entity entity1 = this.getEntity(packet.flag);
					if (entity1 == null) {
						ModLoaderMp.Log("Received spawn packet for entity with owner, but owner was not found.");
					} else {
						if (!field.getType().isAssignableFrom(entity1.getClass())) {
							throw new Exception(
									String.format("Tried to assign an entity of type %s to entity owner, which is of type %s.", entity1.getClass(), field.getType())
							);
						}

						field.set(obj, entity1);
					}
				}
			} catch (Exception var12) {
				ModLoader.getLogger().throwing("NetClientHandler", "handleVehicleSpawn", var12);
				ModLoader.ThrowException(String.format("Error initializing entity of type %s.", packet.type), var12);
				return;
			}
		}

		if (obj != null) {
			obj.clientX = packet.x;
			obj.clientY = packet.y;
			obj.clientZ = packet.z;
			obj.yaw = 0.0F;
			obj.pitch = 0.0F;
			obj.entityId = packet.entityId;
			this.world.method_1495(packet.entityId, obj);
			if (packet.flag > 0) {
				if (packet.type == 60) {
					Entity entity = this.getEntity(packet.flag);
					if (entity instanceof LivingEntity) {
						((ArrowEntity)obj).owner = (LivingEntity)entity;
					}
				}

				obj.setVelocity(
						(double)packet.field_1667 / 8000.0,
						(double)packet.field_1668 / 8000.0,
						(double)packet.field_1669 / 8000.0
				);
			}
		}
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
