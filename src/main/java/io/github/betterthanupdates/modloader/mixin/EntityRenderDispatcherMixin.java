package io.github.betterthanupdates.modloader.mixin;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Environment(value= EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	@Shadow private Map renderers;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;", shift = At.Shift.BEFORE))
	private void modloader$ctor(CallbackInfo ci) {
		ModLoader.AddAllRenderers((Map<Class<? extends Entity>, EntityRenderer>) this.renderers);
	}
}
