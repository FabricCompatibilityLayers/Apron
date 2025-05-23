package io.github.betterthanupdates.modloader.mixin.server.network;

import com.llamalad7.mixinextras.sugar.Local;
import modloadermp.ModLoaderMp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin extends NetworkHandler {
	@Inject(method = "accept", at = @At("RETURN"))
	private void modloader$complete(LoginHelloPacket par1, CallbackInfo ci, @Local(ordinal = 0) ServerPlayerEntity entityplayermp) {
		if (entityplayermp != null) ModLoaderMp.HandleAllLogins(entityplayermp);
	}
}
