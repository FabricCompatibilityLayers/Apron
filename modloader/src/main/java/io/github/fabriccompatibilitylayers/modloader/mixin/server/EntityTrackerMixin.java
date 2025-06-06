package io.github.fabriccompatibilitylayers.modloader.mixin.server;

import modloadermp.ModLoaderMp;
import net.minecraft.entity.Entity;
import net.minecraft.server.entity.EntityTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTracker.class)
public class EntityTrackerMixin {
	@Inject(method = "onEntityAdded", at = @At("RETURN"))
	private void modloadermp$HandleEntityTrackers(Entity entity, CallbackInfo ci) {
		ModLoaderMp.HandleEntityTrackers((EntityTracker) (Object) this, entity);
	}
}
