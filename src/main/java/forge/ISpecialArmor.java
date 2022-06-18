/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.entity.player.PlayerEntity;

public interface ISpecialArmor {
	ArmorProperties getProperties(PlayerEntity arg, int i, int j);
}
