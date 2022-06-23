package io.github.betterthanupdates.forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ReforgedShearsItem {
	boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, PlayerEntity player);
}
