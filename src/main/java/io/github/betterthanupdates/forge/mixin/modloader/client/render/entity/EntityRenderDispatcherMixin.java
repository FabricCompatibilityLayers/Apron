package io.github.betterthanupdates.forge.mixin.modloader.client.render.entity;

import modloader.ModLoader;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Shadow private Map renderers;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;", shift = At.Shift.BEFORE))
    private void modloader$ctr(CallbackInfo ci) {
        ModLoader.AddAllRenderers(this.renderers);
    }
}
