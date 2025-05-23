package io.github.betterthanupdates.apron.stapi.mixin.ami;

import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.mods.alwaysmoreitems.registry.AMIItemRegistry;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AMIItemRegistry.class)
public class AMIItemRegistryMixin {
	@Inject(method = "getModNameForItem", cancellable = true, at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false, ordinal = 1))
	private void apron$fixModName(Item item, CallbackInfoReturnable<String> cir, @Local Identifier identifier) {
		cir.setReturnValue(identifier.namespace.toString().replace("mod_", ""));
	}
}
