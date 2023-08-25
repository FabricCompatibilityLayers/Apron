package io.github.betterthanupdates.apron.stapi.mixin.datafixer;

import com.mojang.datafixers.schemas.Schema;
import io.github.betterthanupdates.apron.stapi.LoadingDoneListener;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StationFlatteningItemStackSchema.class)
public abstract class StationFlatteningItemStackSchemaMixin {
	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	private void registerModFixes(int versionKey, Schema parent, CallbackInfo ci) {
		LoadingDoneListener.registerFixes();
	}
}
