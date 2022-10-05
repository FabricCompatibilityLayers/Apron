package io.github.betterthanupdates.apron.fixes.vanilla.mixin.client.reimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Pseudo
@Mixin(targets = {"reifnsk/minimap/ReiMinimap"})
public class ReiMinimapMixin {
	@Inject(target = {@Desc(value = "getField", args = {Class.class, Object.class, String.class}, ret = Object.class)},
			cancellable = true, at = @At("HEAD"), remap = false)
	private static void fixField(Class<?> target, Object obj, String name, CallbackInfoReturnable<Object> ci) {
		ci.setReturnValue(ReflectionUtils.getField(target, obj, name));
	}
}
