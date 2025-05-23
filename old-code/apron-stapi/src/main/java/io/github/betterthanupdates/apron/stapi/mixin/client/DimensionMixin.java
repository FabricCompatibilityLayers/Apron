package io.github.betterthanupdates.apron.stapi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.client.world.dimension.TravelMessageProvider;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import shockahpi.DimensionBase;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Mixin(Dimension.class)
@Environment(EnvType.CLIENT)
@EnvironmentInterface(value = EnvType.CLIENT, itf = TravelMessageProvider.class)
public class DimensionMixin implements TravelMessageProvider {
	@Shadow
	public int id;

	@Override
	@Environment(EnvType.CLIENT)
	public String getEnteringTranslationKey() {
		DimensionBase base = DimensionBase.getDimByNumber(id);

		if (base != null) return "Entering the " + base.name;

		return "gui." + Identifier.of(NAMESPACE, "entering");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getLeavingTranslationKey() {
		DimensionBase base = DimensionBase.getDimByNumber(id);

		if (base != null) return "Leaving the " + base.name;

		return "gui." + Identifier.of(NAMESPACE, "leaving");
	}
}
