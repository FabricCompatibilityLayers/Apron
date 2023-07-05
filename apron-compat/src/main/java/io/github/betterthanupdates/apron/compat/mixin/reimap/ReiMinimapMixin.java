package io.github.betterthanupdates.apron.compat.mixin.reimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reifnsk.minimap.ReiMinimap;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Mixin(ReiMinimap.class)
public class ReiMinimapMixin {
	@Inject(method = "getField(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;",
			cancellable = true, at = @At("HEAD"), remap = false)
	private static void fixField(Class<?> target, Object obj, String name, CallbackInfoReturnable<Object> ci) {
		ci.setReturnValue(ReflectionUtils.getField(target, obj, name));
	}
}
