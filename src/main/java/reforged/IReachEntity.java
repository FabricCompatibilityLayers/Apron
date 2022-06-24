package reforged;

import io.github.betterthanupdates.Legacy;
import net.minecraft.item.ItemStack;

@Legacy
public interface IReachEntity {
	@Legacy
	boolean reachEntityItemMatches(ItemStack arg);

	@Legacy
	float getReachEntity(ItemStack arg);
}
