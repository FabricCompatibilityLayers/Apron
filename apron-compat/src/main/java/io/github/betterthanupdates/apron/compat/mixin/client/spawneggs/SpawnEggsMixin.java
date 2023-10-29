package io.github.betterthanupdates.apron.compat.mixin.client.spawneggs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.mod_spawnEggs;

@Mixin(mod_spawnEggs.class)
public class SpawnEggsMixin {
	@ModifyConstant(method = "AddRenderer", remap = false, constant = @Constant(stringValue = "c"))
	private static String AddRenderer$fix$1(String s) {
		return "field_759";
	}

	@ModifyConstant(method = "AddRenderer", remap = false, constant = @Constant(stringValue = "b"))
	private static String AddRenderer$fix$2(String s) {
		return "field_758";
	}
}
