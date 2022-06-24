package shockahpi;

import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IReach {
	@Legacy
	boolean reachItemMatches(ItemStack arg);

	@Legacy
	float getReach(ItemStack arg);
}
