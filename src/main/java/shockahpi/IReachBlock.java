package shockahpi;

import net.minecraft.item.ItemStack;

/**
 * Part of ShockAhPI that allows changing the reach of a block
 * @author ShockAh
 */
public interface IReachBlock {
	boolean reachBlockItemMatches(ItemStack stack);
	float getReachBlock(ItemStack stack);
}
