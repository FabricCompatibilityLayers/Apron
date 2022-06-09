package io.github.betterthanupdates.forge.mixin.babricated;

import io.github.betterthanupdates.forge.BabricatedTranslationStorage;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Properties;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin implements BabricatedTranslationStorage {
    @Shadow private Properties translations;

    public TranslationStorageMixin(Properties translations) {
        this.translations = translations;
    }

    @Override
    public Properties getTranslations() {
        return this.translations;
    }
}
