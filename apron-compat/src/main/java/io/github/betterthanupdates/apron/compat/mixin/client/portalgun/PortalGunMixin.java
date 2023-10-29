package io.github.betterthanupdates.apron.compat.mixin.client.portalgun;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = {"net/minecraft/mod_PortalGun"})
public class PortalGunMixin {
	@ModifyArg(target = {@Desc(value = "initAchs")}, remap = false, at = @At(value = "INVOKE",
			remap = false, desc = @Desc(owner = Class.class, value = "forName", args = String.class, ret = Class.class)))
	private static String fixArch(String string) {
		if (string.equals("ACPage")) {
			string = "shockahpi.AchievementPage";
		}

		return string;
	}

	@ModifyArg(target = {@Desc(value = "checkForClasses")}, remap = false, at = @At(value = "INVOKE",
			remap = false, desc = @Desc(owner = Class.class, value = "forName", args = String.class, ret = Class.class)))
	private static String fixCompat(String string) {
		return "net.minecraft." + string;
	}
}
