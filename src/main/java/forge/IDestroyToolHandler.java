/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Legacy
public interface IDestroyToolHandler {
	@Legacy
	void onDestroyCurrentItem(PlayerEntity arg, ItemStack arg2);
}
