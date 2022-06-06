package forge;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface ISpecialResistance {
    float getSpecialExplosionResistance(final World level, final int i, final int j, final int k, final double d, final double e, final double f, final Entity entityBase);
}
