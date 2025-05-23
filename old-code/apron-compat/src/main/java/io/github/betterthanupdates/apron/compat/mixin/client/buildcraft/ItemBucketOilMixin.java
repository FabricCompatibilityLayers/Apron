package io.github.betterthanupdates.apron.compat.mixin.client.buildcraft;

import buildcraft.energy.ItemBucketOil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemBucketOil.class)
public class ItemBucketOilMixin {
	/**
	 * @author Cat Core
	 * @reason energy texture atlas is not registered
	 */
	@Overwrite(remap = false)
	public String getTextureFile() {
		return "/net/minecraft/src/buildcraft/core/gui/item_textures.png";
	}
}
