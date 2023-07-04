package io.github.betterthanupdates.apron.compat.mixin.concrete;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.mod_Concrete;

@Mixin(mod_Concrete.class)
public class ConcreteMixin {
	@ModifyConstant(method = "tool", remap = false, constant = @Constant(stringValue = "bk"))
	private static String tool$fix(String s) {
		return "field_2712";
	}
}
