package io.github.betterthanupdates.apron.compat.mixin.client.overrideapi.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import overrideapi.utils.Reflection;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Mixin(Reflection.class)
public class ReflectionMixin {
	@Inject(method = "findField", remap = false, at = @At("HEAD"), cancellable = true)
	private static void findField$fix(Class<?> target, String[] names, CallbackInfoReturnable<Field> cir) {
		cir.setReturnValue(ReflectionUtils.getField(target, names));
	}
}
