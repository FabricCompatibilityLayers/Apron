package io.github.fabriccompatibilitylayers.modloader.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import modloadermp.ModLoaderMp;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {
	@WrapOperation(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;initScreenHandler()V"))
	private void modloadermp$HandleAllLogins(ServerPlayerEntity instance, Operation<Void> original) {
		original.call(instance);
		ModLoaderMp.HandleAllLogins(instance);
	}
}
