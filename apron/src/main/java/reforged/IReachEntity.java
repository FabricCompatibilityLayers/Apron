package reforged;

import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IReachEntity {
	boolean reachEntityItemMatches(ItemStack itemStack);

	float getReachEntity(ItemStack itemStack);
}
