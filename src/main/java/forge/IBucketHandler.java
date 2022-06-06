package forge;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBucketHandler {
    ItemStack fillCustomBucket(final World level, final int i, final int j, final int k);
}
