package shockahpi;

import net.minecraft.item.ItemStack;

/**
 * Part of ShockAhPI that allows changing the reach of an entity
 * @author ShockAh
 */
public interface IReachEntity {
	boolean reachEntityItemMatches(ItemStack stack);
	float getReachEntity(ItemStack stack);
}
