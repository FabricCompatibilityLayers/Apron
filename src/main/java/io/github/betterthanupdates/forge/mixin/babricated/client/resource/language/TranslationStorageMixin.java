package io.github.betterthanupdates.forge.mixin.babricated.client.resource.language;

import io.github.betterthanupdates.forge.mixininterface.TranslationStorageAccessor;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Properties;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin implements TranslationStorageAccessor {
    @Shadow private Properties translations;

    @Override
    public Properties getTranslations() {
        return this.translations;
    }
}
