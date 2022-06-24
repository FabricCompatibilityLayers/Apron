package reforged;

import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IReachEntity {
	@Legacy
	boolean reachEntityItemMatches(ItemStack arg);

	@Legacy
	float getReachEntity(ItemStack arg);
}
