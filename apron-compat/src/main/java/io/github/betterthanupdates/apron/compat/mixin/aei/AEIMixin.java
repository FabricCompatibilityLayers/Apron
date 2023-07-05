package io.github.betterthanupdates.apron.compat.mixin.aei;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.mod_AEI;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Mixin(mod_AEI.class)
public class AEIMixin {
	@ModifyConstant(method = "OnTickInGUI", constant = @Constant(stringValue = "a"))
	private String OnTickInGUI$fix$1(String constant) {
		return "method_1280";
	}

	@Redirect(method = "OnTickInGUI", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;", remap = false))
	private Field OnTickInGUI$fix$2(Class instance, String name) {
		return ReflectionUtils.getField(instance, new String[]{name});
	}
}
