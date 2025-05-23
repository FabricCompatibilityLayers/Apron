package shockahpi;

import net.minecraft.class_467;
import net.minecraft.world.dimension.NetherDimension;

import io.github.betterthanupdates.Legacy;

@Legacy
public class DimensionNether extends DimensionBase {
	public DimensionNether() {
		super(-1, NetherDimension.class, class_467.class);
		this.name = "Nether";
	}

	public Loc getDistanceScale(Loc paramLoc, boolean paramBoolean) {
		double d = paramBoolean ? 8.0 : 0.125;
		return paramLoc.multiply(d, 1.0, d);
	}
}
