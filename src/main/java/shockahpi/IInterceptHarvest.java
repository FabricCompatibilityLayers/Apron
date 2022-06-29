package shockahpi;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

/**
 * Part of ShockAhPI that allows interception of harvesting.
 *
 * @author ShockAh
 */
@Legacy
public interface IInterceptHarvest {
	boolean canIntercept(World world, PlayerEntity player, Loc pos, int i, int j);

	void intercept(World world, PlayerEntity player, Loc pos, int i, int j);
}
