package io.github.betterthanupdates.apron.compat.mixin.client.overrideapi.old;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.utils.gui.GuiHandler;

@Mixin(GuiHandler.class)
public class GuiHandlerMixin {
	@ModifyConstant(method = "init", remap = false, constant = @Constant(stringValue = "e"))
	private static String fix(String constant) {
		return "field_154";
	}
}
