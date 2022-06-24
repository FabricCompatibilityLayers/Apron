package shockahpi;

import io.github.betterthanupdates.Legacy;
import net.minecraft.item.ItemStack;

@Legacy
public interface IReach {
	@Legacy
	boolean reachItemMatches(ItemStack arg);

	@Legacy
	float getReach(ItemStack arg);
}
