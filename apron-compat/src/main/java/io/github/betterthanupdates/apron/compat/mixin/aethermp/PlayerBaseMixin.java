package io.github.betterthanupdates.apron.compat.mixin.aethermp;

import net.mine_diver.aethermp.player.PlayerBaseAetherMp;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(PlayerBaseAetherMp.class)
public class PlayerBaseMixin {

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "saveHandler"))
	private static String fixAetherMPCrash(String constant) {
		return "field_219";
	}
}
