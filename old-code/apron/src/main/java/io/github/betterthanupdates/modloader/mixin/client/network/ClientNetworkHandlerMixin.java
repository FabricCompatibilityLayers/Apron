package io.github.betterthanupdates.modloader.mixin.client.network;

import java.lang.reflect.Field;

import modloader.ModLoader;
import modloadermp.ModLoaderMp;
import modloadermp.NetClientHandlerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_454;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin extends NetworkHandler {
	@Shadow
	private class_454 field_1973;

	@Shadow
	protected abstract Entity method_1645(int i);

	/**
	 * @author StationAPI
	 */
	@ModifyVariable(
			method = "onEntitySpawn",
			index = 8,
			at = @At(
					value = "LOAD",
					ordinal = 0
			)
	)
	private Entity modloader$onEntitySpawn(Entity entity, EntitySpawnS2CPacket packet) {
		double d = (double) packet.x / 32.0;
		double d1 = (double) packet.y / 32.0;
		double d2 = (double) packet.z / 32.0;

		NetClientHandlerEntity netclienthandlerentity = ModLoaderMp.HandleNetClientHandlerEntities(packet.entityType);

		if (netclienthandlerentity != null) {
			try {
				entity = netclienthandlerentity.entityClass
						.getConstructor(World.class, Double.TYPE, Double.TYPE, Double.TYPE)
						.newInstance(this.field_1973, d, d1, d2);

				if (netclienthandlerentity.entityHasOwner) {
					Field field = netclienthandlerentity.entityClass.getField("owner");

					if (!Entity.class.isAssignableFrom(field.getType())) {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}

					Entity entity1 = this.method_1645(packet.entityData);

					if (entity1 == null) {
						ModLoaderMp.Log("Received spawn packet for entity with owner, but owner was not found.");
					} else {
						if (!field.getType().isAssignableFrom(entity1.getClass())) {
							throw new Exception(
									String.format("Tried to assign an entity of type %s to entity owner, which is of type %s.", entity1.getClass(), field.getType())
							);
						}

						field.set(entity, entity1);
					}
				}
			} catch (Exception e) {
				ModLoader.getLogger().throwing("NetClientHandler", "handleVehicleSpawn", e);
				ModLoader.ThrowException(String.format("Error initializing entity of type %s.", packet.entityType), e);
			}
		}

		return entity;
	}

	/**
	 * @author Risugami
	 * @reason ModLoaderMP can handle custom container GUIs
	 */
	@Inject(method = "onOpenScreen", at = @At("TAIL"))
	private void modloader$openContainer(OpenScreenS2CPacket packet, CallbackInfo ci) {
		int type = packet.screenHandlerId;
		switch (type) {
			case 0:
			case 1:
			case 2:
			case 3:
				return;
			default:
				ModLoaderMp.HandleGUI(packet);
		}
	}
}
