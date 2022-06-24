package reforged;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface ICustomDrop {
	@Legacy
	int getIdDropped(Block arg, int i, Random random, ItemStack arg2);

	@Legacy
	int getDamageDropped(Block arg, int i, ItemStack arg2);

	@Legacy
	int getQuantityDropped(Block arg, int i, Random random, ItemStack arg2);
}
