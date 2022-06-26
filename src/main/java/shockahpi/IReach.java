package shockahpi;

import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IReach {
	
	boolean reachItemMatches(ItemStack arg);

	
	float getReach(ItemStack arg);
}
