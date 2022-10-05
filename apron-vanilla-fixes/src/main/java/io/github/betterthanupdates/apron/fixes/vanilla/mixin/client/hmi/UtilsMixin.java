package io.github.betterthanupdates.apron.fixes.vanilla.mixin.client.hmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Pseudo
@Mixin(targets = {"hmi/Utils"})
public class UtilsMixin {
	@Inject(target = {@Desc(value = "getMethod", args = {Class.class, String[].class, Class[].class}, ret = Method.class)},
			cancellable = true, at = @At("HEAD"), remap = false)
	private static void fixMethod(Class<?> target, String[] names, Class<?>[] types, CallbackInfoReturnable<Method> ci) {
		ci.setReturnValue(ReflectionUtils.getMethod(target, names, types));
	}

	@Inject(target = {@Desc(value = "getField", args = {Class.class, String[].class}, ret = Field.class)},
			cancellable = true, at = @At("HEAD"), remap = false)
	private static void fixField(Class<?> target, String[] names, CallbackInfoReturnable<Field> ci) {
		ci.setReturnValue(ReflectionUtils.getField(target, names));
	}
}
