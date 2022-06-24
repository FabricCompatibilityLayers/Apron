package shockahpi;

import io.github.betterthanupdates.Legacy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Part of ShockAhPI that allows interception of harvesting.
 * @author ShockAh
 */
@Legacy
public interface IInterceptHarvest {
	@Legacy
	boolean canIntercept(World arg, PlayerEntity arg2, Loc loc, int i, int j);

	@Legacy
	void intercept(World arg, PlayerEntity arg2, Loc loc, int i, int j);
}
