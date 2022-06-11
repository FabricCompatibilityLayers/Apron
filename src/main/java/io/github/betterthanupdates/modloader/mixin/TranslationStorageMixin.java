package io.github.betterthanupdates.modloader.mixin;

import io.github.betterthanupdates.modloader.client.resource.language.ModLoaderTranslationStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Properties;

/**
 * Implements methods found in the ModLoader API
 */
@Environment(EnvType.CLIENT)
@Mixin(TranslationStorage.class)
public abstract class TranslationStorageMixin implements ModLoaderTranslationStorage {
	@Shadow private Properties translations;

	/**
	 * @return the localizations from the currently loaded .lang file
	 */
	@Override
	public Properties getTranslations() {
		return this.translations;
	}
}
