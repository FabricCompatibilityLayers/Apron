/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.class_141;
import net.minecraft.entity.player.PlayerEntity;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface ISleepHandler {
	class_141 sleepInBedAt(PlayerEntity player, int x, int y, int z);
}
