package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.overrideapi;

import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.OverrideAPI;

@Pseudo
@Mixin(OverrideAPI.class)
public class OverrideAPIMixin {
	@ModifyConstant(method = "overrideVanillaBlock", constant = @Constant(stringValue = "aX"), remap = false)
	private static String fixFieldNameP1(String constant) {
		return "field_1681";
	}

	@ModifyConstant(method = "overrideVanillaBlock", constant = @Constant(stringValue = "blocksEffectiveAgainst"), remap = false)
	private static String fixFieldNameP2(String constant) {
		return "effectiveBlocks";
	}

	@ModifyConstant(method = "getUniqueButtonID", constant = @Constant(stringValue = "e"), remap = false)
	private static String fixFieldNameP3(String constant) {
		return "field_154";
	}

	@ModifyConstant(method = "getUniqueButtonID", constant = @Constant(stringValue = "controlList"), remap = false)
	private static String fixFieldNameP4(String constant) {
		return "buttons";
	}
}
