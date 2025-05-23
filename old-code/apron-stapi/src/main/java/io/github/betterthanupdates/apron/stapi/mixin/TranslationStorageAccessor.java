package io.github.betterthanupdates.apron.stapi.mixin;

import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resource.language.TranslationStorage;

@Mixin(TranslationStorage.class)
public interface TranslationStorageAccessor {
	@Accessor
	Properties getTranslations();
}
