package io.github.betterthanupdates.apron.stapi.mixin;

import io.github.betterthanupdates.shockahpi.block.ShockAhPINetherPortalBlock;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.class_467;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.block.CustomPortal;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;
import net.modificationstation.stationapi.api.world.dimension.TeleportationManager;
import net.modificationstation.stationapi.impl.block.NetherPortalImpl;
import org.spongepowered.asm.mixin.Mixin;
import shockahpi.DimensionBase;
import shockahpi.Loc;

import java.util.Optional;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin implements ShockAhPINetherPortalBlock, TeleportationManager {
	@Override
	public void switchDimension(PlayerEntity player) {
		if (this instanceof CustomPortal) {
			CustomPortal p = ((CustomPortal) this);
			DimensionHelper.switchDimension(player, p.getDimension(player), p.getDimensionScale(player), p.getTravelAgent(player));
		} else if (this.getDimNumber() == -1) {
			NetherPortalImpl.switchDimension(player);
		} else {
			DimensionBase dimensionBase = DimensionBase.getDimByNumber(this.getDimNumber());
			Optional<Identifier> dimId = DimensionRegistry.INSTANCE.getId(this.getDimNumber());

			if (dimensionBase == null || dimId.isEmpty()) {
				NetherPortalImpl.switchDimension(player);
			} else {
				sapi$stapi$teleportToShockAhPIDimension(player, dimensionBase, dimId.get());
			}
		}
	}

	private void sapi$stapi$teleportToShockAhPIDimension(PlayerEntity player, DimensionBase dimensionBase, Identifier dimId) {
		class_467 teleporter = dimensionBase.getTeleporter();

		if (teleporter == null) {
			NetherPortalImpl.switchDimension(player);
		} else {
			DimensionBase currentDimension = DimensionBase.getDimByNumber(player.dimensionId);

			Loc loc = new Loc(1, 1, 1);

			if (currentDimension != null) {
				loc = currentDimension.getDistanceScale(loc, true);
			}

			loc = dimensionBase.getDistanceScale(loc, false);

			double scale = MathHelper.sqrt((float) (MathHelper.square(loc.x) +
					MathHelper.square(loc.y) +
					MathHelper.square(loc.z)));

			DimensionHelper.switchDimension(player, dimId, scale, teleporter);
		}
	}
}
