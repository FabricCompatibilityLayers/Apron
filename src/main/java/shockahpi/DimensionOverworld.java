package shockahpi;

import net.minecraft.world.dimension.OverworldDimension;

import io.github.betterthanupdates.Legacy;

@Legacy
public class DimensionOverworld extends DimensionBase {
	public DimensionOverworld() {
		super(0, OverworldDimension.class, null);
		this.name = "Overworld";
	}
}
