package io.github.betterthanupdates.apron.stapi.mixin.datafixer;

import com.mojang.datafixers.schemas.Schema;
import io.github.betterthanupdates.apron.stapi.LoadingDoneListener;
import net.modificationstation.stationapi.api.vanillafix.datadamager.schema.McRegionItemStackDamagerSchema;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(McRegionItemStackDamagerSchema.class)
public class McRegionItemStackDamagerSchemaMixin {
	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	private void registerModFixes(int versionKey, Schema parent, CallbackInfo ci) {
		LoadingDoneListener.registerFixes();
	}
}
