/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IUseItemFirst {
	@Legacy
	boolean onItemUseFirst(ItemStack arg, PlayerEntity arg2, World arg3, int i, int j, int k, int l);
}
