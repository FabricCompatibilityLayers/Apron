package io.github.betterthanupdates.shockahpi.mixin.client;

import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.dimension.SkyDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shockahpi.DimensionBase;

@Mixin(Dimension.class)
public class DimensionMixin {
	/**
	 * @author ShockAh
	 * @reason add custom dimensions support.
	 */
	@Overwrite
	public static Dimension getByID(int i) {
		DimensionBase dimensionbase = DimensionBase.getDimByNumber(i);
		if (dimensionbase != null) {
			return dimensionbase.getWorldProvider();
		} else {
			return (Dimension)(i == -1 ? new NetherDimension() : (i == 0 ? new OverworldDimension() : (i == 1 ? new SkyDimension() : null)));
		}
	}
}
