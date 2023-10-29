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

	@Inject(method = "invokePrivateMethod", remap = false, at = @At("HEAD"), cancellable = true)
	private static void invokePrivateMethod$fix(Object target, String name, Object[] args, Class<?>[] parameterTypes, CallbackInfoReturnable<Object> cir) {
		try {
			Method method = ReflectionUtils.getMethod(target.getClass(), new String[]{name}, parameterTypes);
			method.setAccessible(true);
			cir.setReturnValue(method.invoke(target, args));
		} catch (Exception var5) {
			throw new RuntimeException(var5);
		}
	}

	@Inject(method = "invokePrivateStaticMethod", remap = false, at = @At("HEAD"), cancellable = true)
	private static void invokePrivateStaticMethod$fix(Class<?> target, String name, Object[] args, Class<?>[] parameterTypes, CallbackInfoReturnable<Object> cir) {
		try {
			Method method = ReflectionUtils.getMethod(target, new String[]{name}, parameterTypes);
			method.setAccessible(true);
			cir.setReturnValue(method.invoke(null, args));
		} catch (Exception var5) {
			throw new RuntimeException(var5);
		}
	}
}
