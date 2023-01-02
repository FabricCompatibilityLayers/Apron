package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.overrideapi;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import overrideapi.utils.gui.GuiHandler;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Pseudo
@Mixin(GuiHandler.class)
public class GuiHandlerMixin {
	@ModifyConstant(method = "init", constant = @Constant(stringValue = "e"), remap = false)
	private static String fixFieldNameP1(String constant) {
		return "field_154";
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Loverrideapi/utils/Reflection;findField(Ljava/lang/Class;[Ljava/lang/String;)Ljava/lang/reflect/Field;", remap = false))
	private Field fixFields(Class<?> name, String[] field) {
		return ReflectionUtils.getField(name, field);
	}
}
