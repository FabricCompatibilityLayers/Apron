package reforged;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface ICustomDrop {
	int getIdDropped(Block block, int i, Random random, ItemStack itemStack);

	int getDamageDropped(Block block, int i, ItemStack itemStack);

	int getQuantityDropped(Block block, int i, Random random, ItemStack itemStack);
}
