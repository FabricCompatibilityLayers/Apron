package io.github.betterthanupdates.apron.compat.mixin.client.aethermp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.mine_diver.aethermp.Core;

@Mixin(Core.class)
public class AetherMPMixin {

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "field_25102_a"))
	private static String fixAetherMPCrash(String constant) {
		return "field_2604";
	}
}
