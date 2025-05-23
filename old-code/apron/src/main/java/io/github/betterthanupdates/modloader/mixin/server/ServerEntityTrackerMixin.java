package io.github.betterthanupdates.modloader.mixin.server;

import modloadermp.ModLoaderMp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_488;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;

@Environment(EnvType.SERVER)
@Mixin(class_488.class)
public class ServerEntityTrackerMixin {
	@Inject(method = "method_1665", at = @At("RETURN"))
	private void modloader$syncEntity(Entity entity, CallbackInfo ci) {
		ModLoaderMp.HandleEntityTrackers((class_488) (Object) this, entity);
	}
}
