/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface ISpecialResistance {
	float getSpecialExplosionResistance(World arg, int i, int j, int k, double d, double e, double f, Entity arg2);
}
