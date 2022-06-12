/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.entity.player.PlayerEntity;

public interface ISpecialArmor {
	ArmorProperties getProperties(final PlayerEntity playerBase, final int i, final int j);
}
