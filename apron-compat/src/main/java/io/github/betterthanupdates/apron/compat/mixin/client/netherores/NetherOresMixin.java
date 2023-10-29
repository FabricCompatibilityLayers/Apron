package io.github.betterthanupdates.apron.compat.mixin.client.netherores;

import net.minecraft.mod_NetherOres;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(mod_NetherOres.class)
public class NetherOresMixin {

	@ModifyConstant(method = "<init>", remap = false, constant = @Constant(stringValue = "d"))
	public String init(String s) {
		return "method_1792";
	}
}
