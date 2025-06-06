package io.github.fabriccompatibilitylayers.modloader.mixin.client.modmenu.babric;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import io.github.fabriccompatibilitylayers.modloader.compat.modmenu.babric.MLModContainer;
import io.github.prospector.modmenu.gui.ModListWidget;
import modloader.ModLoader;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ModListWidget.class)
public class ModListWidgetMixin {
	@ModifyReceiver(method = "filter(Ljava/lang/String;ZZ)V", at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V", remap = false, ordinal = 0), remap = false, require = 0)
	private List<ModContainer> modloader$addMLMods(List<ModContainer> instance, Comparator<ModContainer> comparator) {
		instance.addAll(ModLoader.getLoadedMods().stream().map(MLModContainer::new).collect(Collectors.toList()));
		return instance;
	}
}
