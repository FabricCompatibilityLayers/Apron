package io.github.betterthanupdates.reforged.block;

import java.util.Random;

import net.minecraft.item.ItemStack;

/**
 * New methods for {@link net.minecraft.block.Block} by Reforged.
 */
public interface ReforgedBlock {
	int quantityDropped(int i, Random random);

	int quantityDropped(int i, Random random, ItemStack stack);

	int damageDropped(int i, ItemStack stack);

	int idDropped(int i, Random random, ItemStack stack);
}
