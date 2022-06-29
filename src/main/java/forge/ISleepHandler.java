/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SleepStatus;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface ISleepHandler {
	SleepStatus sleepInBedAt(PlayerEntity player, int x, int y, int z);
}
