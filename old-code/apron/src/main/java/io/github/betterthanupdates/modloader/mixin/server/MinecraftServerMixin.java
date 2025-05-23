package io.github.betterthanupdates.modloader.mixin.server;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_39;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;

@Environment(EnvType.SERVER)
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements Runnable, class_39 {

	@Inject(method = "method_2166", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V", remap = false))
	private void modloader$start(CallbackInfoReturnable<Boolean> cir) {
		ModLoader.Init((MinecraftServer) (Object) this);
	}

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1), remap = false)
	public void run(CallbackInfo ci) {
		ModLoader.OnTick((MinecraftServer) (Object) this);
	}
}
