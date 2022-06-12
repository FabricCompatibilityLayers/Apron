package shockahpi;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Part of ShockAhPI that allows interception of harvesting
 * @author ShockAh
 */
public interface IInterceptHarvest {
	boolean canIntercept(World world, PlayerEntity player, BlockPos pos, int i, int j);
	void intercept(World world, PlayerEntity player, BlockPos pos, int i, int j);
}
