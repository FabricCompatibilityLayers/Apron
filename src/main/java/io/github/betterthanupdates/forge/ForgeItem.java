package io.github.betterthanupdates.forge;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ForgeItem {
    float getStrVsBlock(ItemStack itemstack, Block block, int md);
}
