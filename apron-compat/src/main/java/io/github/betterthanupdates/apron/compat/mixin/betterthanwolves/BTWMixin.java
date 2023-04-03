package io.github.betterthanupdates.apron.compat.mixin.betterthanwolves;

import net.minecraft.mod_FCBetterThanWolves;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = mod_FCBetterThanWolves.class, remap = false)
public class BTWMixin {
	@ModifyConstant(method = "SetIconIndexAsUsedInModLoader", constant = @Constant(stringValue = "usedItemSprites"))
	private String changeFieldName(String constant) {
		return "USED_ITEM_SPRITES";
	}
}
