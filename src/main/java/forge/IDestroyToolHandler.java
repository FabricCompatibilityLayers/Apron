package forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IDestroyToolHandler {
    void onDestroyCurrentItem(final PlayerEntity player, final ItemStack item);
}
