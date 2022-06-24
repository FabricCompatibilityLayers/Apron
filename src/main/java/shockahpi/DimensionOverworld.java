package shockahpi;

import io.github.betterthanupdates.Legacy;
import net.minecraft.world.dimension.OverworldDimension;

@Legacy
public class DimensionOverworld extends DimensionBase {
	@Legacy
	public DimensionOverworld() {
		super(0, OverworldDimension.class, null);
		this.name = "Overworld";
	}
}
