package io.github.betterthanupdates.apron.compat.mixin.overrideapi.sarcasm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.proxy.EntityRendererTransformer;

@Mixin(EntityRendererTransformer.class)
public class EntityRendererTransformerMixin {
	@ModifyConstant(method = "<clinit>", remap = false, constant = @Constant(stringValue = "ModLoader"))
	private static String clinit$fix(String constant) {
		return "modloader.ModLoader";
	}

	@ModifyConstant(method = "getRequestedMethods", remap = false, constant = @Constant(stringValue = "b"))
	private static String getRequestedMethods$fix(String constant) {
		return "method_1844";
	}

	@ModifyConstant(method = "transform", remap = false, constant = @Constant(stringValue = "b"))
	private static String transform$fix$1(String constant) {
		return "method_1844";
	}

	@ModifyConstant(method = "transform", remap = false, constant = @Constant(stringValue = "j", ordinal = 0))
	private static String transform$fix$2(String constant) {
		return "field_2349";
	}

	@ModifyConstant(method = "transform", remap = false, constant = @Constant(stringValue = "j", ordinal = 1))
	private static String transform$fix$3(String constant) {
		return "field_2349";
	}
	@ModifyConstant(method = "transform", remap = false, constant = @Constant(stringValue = "ModLoader"))
	private static String transform$fix$4(String constant) {
		return "modloader.ModLoader";
	}
}
