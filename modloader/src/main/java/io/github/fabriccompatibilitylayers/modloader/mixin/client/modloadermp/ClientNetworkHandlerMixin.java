package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloadermp;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import modloader.ModLoader;
import modloadermp.ModLoaderMp;
import modloadermp.NetClientHandlerEntity;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;

	@Shadow
	protected abstract Entity getEntity(int i);

	@Definition(id = "obj", local = @Local(type = Entity.class, ordinal = 0))
	@Expression("obj != null")
	@Inject(method = "onEntitySpawn", at = @At("MIXINEXTRAS:EXPRESSION"), cancellable = true)
	private void modloadermp$HandleNetClientHandlerEntities(EntitySpawnS2CPacket packet23vehiclespawn, CallbackInfo ci,
															@Local LocalRef<Entity> objRef,
															@Local(ordinal = 0) double d,
															@Local(ordinal = 1) double d1,
															@Local(ordinal = 2) double d2) {
		NetClientHandlerEntity netclienthandlerentity = ModLoaderMp.HandleNetClientHandlerEntities(packet23vehiclespawn.entityType);
		if (netclienthandlerentity != null) {
			try {
				objRef.set((Entity) netclienthandlerentity.entityClass.getConstructor(World.class, Double.TYPE, Double.TYPE, Double.TYPE).newInstance(this.world, d, d1, d2));
				if (netclienthandlerentity.entityHasOwner) {
					Field field = netclienthandlerentity.entityClass.getField("owner");
					if (!Entity.class.isAssignableFrom(field.getType())) {
						throw new Exception(String.format("Entity's owner field must be of type Entity, but it is of type %s.", field.getType()));
					}

					Entity entity1 = this.getEntity(packet23vehiclespawn.entityData);
					if (entity1 == null) {
						ModLoaderMp.Log("Received spawn packet for entity with owner, but owner was not found.");
					} else {
						if (!field.getType().isAssignableFrom(entity1.getClass())) {
							throw new Exception(String.format("Tried to assign an entity of type %s to entity owner, which is of type %s.", entity1.getClass(), field.getType()));
						}

						field.set(objRef.get(), entity1);
					}
				}
			} catch (Exception exception) {
				ModLoader.getLogger().throwing("NetClientHandler", "handleVehicleSpawn", exception);
				ModLoader.ThrowException(String.format("Error initializing entity of type %s.", packet23vehiclespawn.entityType), exception);
				ci.cancel();
			}
		}
	}

	@Inject(method = "onOpenScreen", at = @At("RETURN"))
	private void modloadermp$HandleGUI(OpenScreenS2CPacket packet100openwindow, CallbackInfo ci) {
		int type = packet100openwindow.screenHandlerId;
		switch (type) {
			case 0:
			case 1:
			case 2:
			case 3:
				return;
			default:
				ModLoaderMp.HandleGUI(packet100openwindow);
		}
	}
}
