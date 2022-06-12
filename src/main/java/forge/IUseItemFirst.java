/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IUseItemFirst {
	boolean onItemUseFirst(final ItemStack itemInstance, final PlayerEntity playerBase, final World level, final int i, final int j, final int k, final int l);
}
