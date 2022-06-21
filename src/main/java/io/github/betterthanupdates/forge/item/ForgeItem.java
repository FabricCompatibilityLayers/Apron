package io.github.betterthanupdates.forge.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ForgeItem {
	float getStrVsBlock(ItemStack stack, Block block, int meta);
}
