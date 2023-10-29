package io.github.betterthanupdates.apron.compat.mixin.client.twilightforest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "net/minecraft/mod_TwilightForest")
public class TwilightForestMixin {
	@ModifyArg(target = {@Desc(value = "<init>")}, remap = false, at = @At(value = "INVOKE",
			remap = false, desc = @Desc(owner = Class.class, value = "forName", args = String.class, ret = Class.class)))
	private String fixInit(String string) {
		return "net.minecraft." + string;
	}
}
