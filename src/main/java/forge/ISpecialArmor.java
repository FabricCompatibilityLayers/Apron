/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.entity.player.PlayerEntity;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface ISpecialArmor {
	ArmorProperties getProperties(PlayerEntity player, int itemId, int meta);
}
