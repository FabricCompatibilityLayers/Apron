package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import modloader.ModLoader;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@ModifyReceiver(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;", remap = false))
	private Map modloader$AddAllRenderers(Map instance) {
		ModLoader.AddAllRenderers(instance);
		return instance;
	}
}
