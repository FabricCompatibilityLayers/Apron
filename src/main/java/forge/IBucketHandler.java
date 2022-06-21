/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBucketHandler {
	ItemStack fillCustomBucket(World world, int i, int j, int k);
}
