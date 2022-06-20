package io.github.betterthanupdates.modloader.mixin.server;

import modloadermp.ModLoaderMp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerEntityTracker;

@Environment(EnvType.SERVER)
@Mixin(ServerEntityTracker.class)
public class ServerEntityTrackerMixin {

	@Inject(method = "syncEntity", at = @At("RETURN"))
	private void modloader$syncEntity(Entity entity, CallbackInfo ci) {
		ModLoaderMp.HandleEntityTrackers((ServerEntityTracker) (Object) this, entity);
	}
}
