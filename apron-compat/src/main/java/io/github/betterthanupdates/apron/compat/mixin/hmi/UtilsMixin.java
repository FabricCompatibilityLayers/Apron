package io.github.betterthanupdates.apron.compat.mixin.hmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import hmi.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Mixin(Utils.class)
public class UtilsMixin {
	@Inject(method = "getMethod", at = @At("HEAD"), cancellable = true, remap = false)
	private static void fixMethod(Class<?> target, String[] names, Class<?>[] types, CallbackInfoReturnable<Method> ci) {
		ci.setReturnValue(ReflectionUtils.getMethod(target, names, types));
	}

	@Inject(method = "getField", at = @At("HEAD"), cancellable = true, remap = false)
	private static void fixField(Class<?> target, String[] names, CallbackInfoReturnable<Field> ci) {
		ci.setReturnValue(ReflectionUtils.getField(target, names));
	}
}
