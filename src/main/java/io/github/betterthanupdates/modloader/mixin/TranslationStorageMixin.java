package io.github.betterthanupdates.modloader.mixin;

import io.github.betterthanupdates.modloader.client.resource.language.ModLoaderTranslationStorage;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Properties;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin implements ModLoaderTranslationStorage {
    @Shadow private Properties translations;

    @Override
    public Properties getTranslations() {
        return this.translations;
    }
}
