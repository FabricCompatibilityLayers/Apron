package reforged;

import net.minecraft.item.ItemStack;

public interface IReachEntity {
	boolean reachEntityItemMatches(ItemStack arg);

	float getReachEntity(ItemStack arg);
}
