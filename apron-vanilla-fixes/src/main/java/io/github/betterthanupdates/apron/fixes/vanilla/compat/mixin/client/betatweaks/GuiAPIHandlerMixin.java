package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.betatweaks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = {"betatweaks/GuiAPIHandler"})
public class GuiAPIHandlerMixin {
	@ModifyArg(target = {@Desc(value = "init")}, remap = false, at = @At(value = "INVOKE", target = "Lbetatweaks/Utils;classExists(Ljava/lang/String;)Z", remap = false))
	private String fixInit(String string) {
		return "guiapi." + string;
	}
}
