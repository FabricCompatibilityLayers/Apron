package io.github.betterthanupdates.apron.compat.mixin.client.overrideapi.sarcasm;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.utils.Util;

@Mixin(Util.class)
public class UtilMixin {
	@ModifyConstant(method = "<clinit>", remap = false, constant = @Constant(stringValue = "uu"))
	private static String fix$obfuscated(String constant) {
		return Block.class.getName();
	}
}
