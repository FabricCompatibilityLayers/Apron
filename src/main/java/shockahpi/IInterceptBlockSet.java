package shockahpi;

import net.minecraft.world.World;

/**
 * Part of ShockAhPI that allows interception of block placement.
 * @author ShockAh
 */
public interface IInterceptBlockSet {
	boolean canIntercept(World arg, Loc loc, int i);

	int intercept(World arg, Loc loc, int i);
}
