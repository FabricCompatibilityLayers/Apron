package forge;

import net.minecraft.entity.player.PlayerEntity;

public interface ISpecialArmor {
    ArmorProperties getProperties(final PlayerEntity playerBase, final int i, final int j);
}
