package io.github.betterthanupdates.apron.stapi.resources;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.resource.language.TranslationInvalidationEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import net.minecraft.client.resource.language.TranslationStorage;

import io.github.betterthanupdates.apron.LifecycleUtils;
import io.github.betterthanupdates.apron.stapi.mixin.TranslationStorageAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class ApronResourceEvents {
	@EventListener(priority = ListenerPriority.LOWEST)
	private static void onTranslationReload(TranslationInvalidationEvent event) {
		((TranslationStorageAccessor)TranslationStorage.getInstance()).getTranslations().putAll(LifecycleUtils.CACHED_TRANSLATIONS);
	}
}
