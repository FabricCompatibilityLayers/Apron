/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.entity.player.PlayerEntity;

@Legacy
public interface ISpecialArmor {
	@Legacy
	ArmorProperties getProperties(PlayerEntity arg, int i, int j);
}
