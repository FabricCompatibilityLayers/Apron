package io.github.betterthanupdates.apron.compat.mixin.client.overrideapi.sarcasm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.proxy.EntityRendererTransformer;

@Mixin(EntityRendererTransformer.class)
public class EntityRendererTransformerMixin {
	@ModifyConstant(method = "getRequestedMethods", remap = false, constant = @Constant(stringValue = "b"))
	private static String getRequestedMethods$fix(String constant) {
		return "method_1844";
	}
}
