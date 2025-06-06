package io.github.fabriccompatibilitylayers.modloader.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import modloader.ModLoader;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	protected abstract void shutdown();

	@Shadow
	public boolean stopped;

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V",  ordinal = 0, remap = false))
	private void modloader$Init(CallbackInfoReturnable<Boolean> cir) {
		ModLoader.Init((MinecraftServer) (Object) this);
	}

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1, remap = false))
	private void modloader$OnTick(CallbackInfo ci) {
		ModLoader.OnTick((MinecraftServer) (Object) this);
	}

	@WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/System;exit(I)V"))
	private void modloader$wrapShutdown(int status, Operation<Void> original) {
		try {
			this.shutdown();
			this.stopped = true;
		} catch (Throwable throwable2) {
			throwable2.printStackTrace();
		} finally {
			original.call(status);
		}
	}
}
