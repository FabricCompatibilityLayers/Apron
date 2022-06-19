package shockahpi;

import net.minecraft.world.World;

/**
 * Part of ShockAhPI that allows interception of block placement.
 * @author ShockAh
 */
public interface IInterceptBlockSet {
	boolean canIntercept(World world, Loc pos, int meta);
	int intercept(World world, Loc pos, int meta);
}
