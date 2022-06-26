package io.github.betterthanupdates.forge.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ReforgedShearsItem {
	boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, PlayerEntity player);
}
