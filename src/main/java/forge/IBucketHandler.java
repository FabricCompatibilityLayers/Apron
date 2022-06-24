/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Legacy
public interface IBucketHandler {
	@Legacy
	ItemStack fillCustomBucket(World arg, int i, int j, int k);
}
