package io.github.betterthanupdates.modloader.mixin;

import modloader.ModLoader;
import modloadermp.ModLoaderMp;
import modloadermp.NetClientHandlerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientPlayPacketHandler;
import net.minecraft.packet.play.EntitySpawnS2CPacket;
import net.minecraft.packet.play.OpenContainerS2CPacket;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Environment(value= EnvType.CLIENT)
@Mixin(ClientPlayPacketHandler.class)
public abstract class ClientPlayPacketHandlerMixin {
	@Shadow private ClientWorld world;

	@Shadow protected abstract Entity method_1645(int i);

	/**
	 * @author Risugami
	 * @reason ModLoaderMP can handle spawning custom entities
	 */
	@Inject(method = "onEntitySpawn", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, ordinal = 0,
			shift = At.Shift.BEFORE, target = "Lnet/minecraft/packet/play/EntitySpawnS2CPacket;type:I"))
	private void modloader$spawnEntity(EntitySpawnS2CPacket packet, CallbackInfo ci) {
		double xVelocity = (double)packet.x / 32.0;
		double yVelocity = (double)packet.y / 32.0;
		double zVelocity = (double)packet.z / 32.0;
		Entity entity;

		NetClientHandlerEntity entityHandler = ModLoaderMp.HandleNetClientHandlerEntities(packet.type);
		if (entityHandler != null) {
			try {
				entity = entityHandler.entityClass
						.getConstructor(World.class, Double.TYPE, Double.TYPE, Double.TYPE)
						.newInstance(this.world, xVelocity, yVelocity, zVelocity);
				if (entityHandler.entityHasOwner) {
					Field field = entityHandler.entityClass.getField("owner");
					if (!Entity.class.isAssignableFrom(field.getType())) {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}

					Entity entity1 = this.method_1645(packet.flag);
					if (entity1 == null) {
						ModLoaderMp.Log("Received spawn packet for entity with owner, but owner was not found.");
					} else {
						if (!field.getType().isAssignableFrom(entity1.getClass())) {
							throw new Exception(String.format("Tried to assign an entity of type %s to entity owner, which is of type %s.", entity1.getClass(), field.getType()));
						}
						entity = entity1;
					}
				}
			} catch (Exception e) {
				ModLoader.getLogger().throwing("NetClientHandler", "handleVehicleSpawn", e);
				ModLoader.ThrowException("Error initializing entity of type " + packet.type + '.', e);
				// Don't cancel/return. Continue with vanilla procedure
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
