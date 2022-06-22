package reforged;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ICustomDrop {
	int getIdDropped(Block arg, int i, Random random, ItemStack arg2);

	int getDamageDropped(Block arg, int i, ItemStack arg2);

	int getQuantityDropped(Block arg, int i, Random random, ItemStack arg2);
}
