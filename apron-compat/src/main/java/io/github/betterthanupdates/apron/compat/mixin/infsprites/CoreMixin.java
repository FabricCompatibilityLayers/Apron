package io.github.betterthanupdates.apron.compat.mixin.infsprites;

import net.mine_diver.infsprites.Core;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Core.class)
public abstract class CoreMixin {
	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "usedTerrainSprites"))
	private static String fixUTS(String constant) {
		return "USED_TERRAIN_SPRITES";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "usedItemSprites"))
	private static String fixUIS(String constant) {
		return "USED_ITEM_SPRITES";
	}

	@ModifyConstant(method = "preInit", remap = false, constant = @Constant(stringValue = "d"))
	private String preInit$fix(String constant) {
		return "field_2544";
	}
}
