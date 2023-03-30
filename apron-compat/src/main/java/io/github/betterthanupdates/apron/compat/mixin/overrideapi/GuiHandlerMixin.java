package io.github.betterthanupdates.apron.compat.mixin.overrideapi;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.utils.gui.GuiHandler;

@Pseudo
@Mixin(GuiHandler.class)
public class GuiHandlerMixin {
	@ModifyConstant(method = "init", constant = @Constant(stringValue = "e"), remap = false)
	private static String fixFieldNameP1(String constant) {
		return "field_154";
	}
}
