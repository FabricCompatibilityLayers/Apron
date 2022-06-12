package shockahpi;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Part of ShockAhPI that allows interception of block placement
 * @author ShockAh
 */
public interface IInterceptBlockSet {
	boolean canIntercept(World world, BlockPos pos, int meta);
	int intercept(World world, BlockPos pos, int meta);
}
