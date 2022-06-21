/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IUseItemFirst {
	boolean onItemUseFirst(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int l);
}
