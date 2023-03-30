package io.github.betterthanupdates.modloader.mixin.server;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandSource;

@Environment(EnvType.SERVER)
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements Runnable, CommandSource {

	@Inject(method = "start", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V", remap = false))
	private void modloader$start(CallbackInfoReturnable<Boolean> cir) {
		ModLoader.Init((MinecraftServer) (Object) this);
	}

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1), remap = false)
	public void run(CallbackInfo ci) {
		ModLoader.OnTick((MinecraftServer) (Object) this);
	}
}
