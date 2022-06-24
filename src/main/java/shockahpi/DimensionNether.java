package shockahpi;

import io.github.betterthanupdates.Legacy;
import net.minecraft.util.NetherTeleporter;
import net.minecraft.world.dimension.NetherDimension;

@Legacy
public class DimensionNether extends DimensionBase {
	@Legacy
	public DimensionNether() {
		super(-1, NetherDimension.class, NetherTeleporter.class);
		this.name = "Nether";
	}

	@Legacy
	public Loc getDistanceScale(Loc paramLoc, boolean paramBoolean) {
		double d = paramBoolean ? 8.0 : 0.125;
		return paramLoc.multiply(d, 1.0, d);
	}
}
