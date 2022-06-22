package shockahpi;

import net.minecraft.item.ItemStack;

public interface IReach {
	boolean reachItemMatches(ItemStack arg);

	float getReach(ItemStack arg);
}
