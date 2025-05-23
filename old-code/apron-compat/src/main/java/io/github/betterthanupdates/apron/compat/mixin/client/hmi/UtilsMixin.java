package io.github.betterthanupdates.apron.compat.mixin.client.hmi;

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
	@Inject(method = "getField", at = @At("HEAD"), cancellable = true, remap = false)
	private static void fixField(Class<?> target, String[] names, CallbackInfoReturnable<Field> ci) {
		ci.setReturnValue(ReflectionUtils.getField(target, names));
	}
}
