package io.github.betterthanupdates.modloader.mixin.server;

import modloadermp.ModLoaderMp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.network.PacketHandler;
import net.minecraft.packet.login.LoginRequestPacket;
import net.minecraft.server.entity.player.ServerPlayerEntity;
import net.minecraft.server.network.ServerLoginPacketHandler;

@Environment(EnvType.SERVER)
@Mixin(ServerLoginPacketHandler.class)
public abstract class ServerLoginPacketHandlerMixin extends PacketHandler {
	@Inject(method = "complete", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void modloader$complete(LoginRequestPacket par1, CallbackInfo ci, ServerPlayerEntity entityplayermp) {
		if (entityplayermp != null) ModLoaderMp.HandleAllLogins(entityplayermp);
	}
}
