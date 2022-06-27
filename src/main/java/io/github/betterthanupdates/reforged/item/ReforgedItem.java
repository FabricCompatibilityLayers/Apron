package io.github.betterthanupdates.reforged.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ReforgedItem {
	boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, PlayerEntity player);
}
