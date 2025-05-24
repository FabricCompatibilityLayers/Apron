package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import modloader.ModLoader;
import net.minecraft.client.CrashReportPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.stream.Collectors;

@Mixin(CrashReportPanel.class)
public class CrashReportPanelMixin {
//	@Definition(id = "str4", local = @Local(type = String.class, ordinal = 3))
//	@Expression("str4 = ?")
//	@WrapOperation(method = "<init>", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
//	private void modloader$AddModList(String instance, String value, Operation<Void> original) {
//		original.call(instance,
//				instance +
//						"Mods loaded: " + (ModLoader.getLoadedMods().size() + 1) + "\n" +
//						"ModLoader Beta 1.7.3" + "\n" +
//						ModLoader.getLoadedMods().stream()
//								.map(mod -> mod.getClass().getName() + " " + mod.Version())
//								.collect(Collectors.joining("\n")) + "\n"
//				);
//	}
	@ModifyConstant(method = "<init>", constant = @Constant(stringValue = "\n", ordinal = 8))
	private String modloader$AddModList(String value) {
		return "Mods loaded: " + (ModLoader.getLoadedMods().size() + 1) + "\n" +
						"ModLoader Beta 1.7.3" + "\n" +
						ModLoader.getLoadedMods().stream()
								.map(mod -> mod.getClass().getName() + " " + mod.Version())
								.collect(Collectors.joining("\n")) + "\n";
	}
}
