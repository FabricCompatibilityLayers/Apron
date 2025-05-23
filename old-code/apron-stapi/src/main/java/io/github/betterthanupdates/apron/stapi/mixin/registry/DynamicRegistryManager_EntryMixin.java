package io.github.betterthanupdates.apron.stapi.mixin.registry;

import net.modificationstation.stationapi.api.registry.DynamicRegistryManager;
import net.modificationstation.stationapi.api.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;

@Mixin(DynamicRegistryManager.Entry.class)
public class DynamicRegistryManager_EntryMixin<T> {
	@Redirect(method = "freeze", at = @At(value = "INVOKE", target = "Lnet/modificationstation/stationapi/api/registry/Registry;freeze()Lnet/modificationstation/stationapi/api/registry/Registry;", remap = false), remap = false)
	private Registry<T> cancelFreezing(Registry<T> instance) {
		ApronStAPICompat.LOGGER.warn("[DynamicRegistryManager.Entry] Canceled Registry freezing for registry: {}", instance.getKey().toString());
		return instance;
	}
}
