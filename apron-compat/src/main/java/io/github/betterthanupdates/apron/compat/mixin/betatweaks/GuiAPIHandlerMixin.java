package io.github.betterthanupdates.apron.compat.mixin.betatweaks;

import betatweaks.GuiAPIHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiAPIHandler.class)
public class GuiAPIHandlerMixin {
	@ModifyArg(method = "init", remap = false, at = @At(value = "INVOKE", target = "Lbetatweaks/Utils;classExists(Ljava/lang/String;)Z", remap = false))
	private String fixInit(String string) {
		return "guiapi." + string;
	}
}
