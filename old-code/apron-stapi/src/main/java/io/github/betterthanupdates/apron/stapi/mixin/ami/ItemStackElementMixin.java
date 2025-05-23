package io.github.betterthanupdates.apron.stapi.mixin.ami;

import net.glasslauncher.mods.alwaysmoreitems.util.ItemStackElement;
import net.modificationstation.stationapi.api.util.Namespace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStackElement.class)
public class ItemStackElementMixin {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/modificationstation/stationapi/api/util/Namespace;getName()Ljava/lang/String;", remap = false))
	private String apron$fixModName(Namespace instance) {
		if (instance.toString().startsWith("mod_")) {
			return instance.toString().replace("mod_", "");
		}

		return instance.getName();
	}
}
