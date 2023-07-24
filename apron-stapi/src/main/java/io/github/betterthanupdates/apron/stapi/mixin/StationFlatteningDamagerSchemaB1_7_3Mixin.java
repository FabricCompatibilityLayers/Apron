package io.github.betterthanupdates.apron.stapi.mixin;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import io.github.betterthanupdates.apron.LifecycleUtils;
import io.github.betterthanupdates.apron.stapi.LoadingDoneListener;
import net.modificationstation.stationapi.api.vanillafix.datadamager.schema.StationFlatteningDamagerSchemaB1_7_3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(StationFlatteningDamagerSchemaB1_7_3.class)
public class StationFlatteningDamagerSchemaB1_7_3Mixin extends Schema {
	public StationFlatteningDamagerSchemaB1_7_3Mixin(int versionKey, Schema parent) {
		super(versionKey, parent);
	}

	@Inject(method = "registerBlockEntities", at = @At("RETURN"), cancellable = true, remap = false)
	private void registerBlockEntitiesFix(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
		Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();

		if (LoadingDoneListener.allowConversion) {
			for (String id : LifecycleUtils.MOD_BLOCK_ENTITIES) {
				registerSimple(map, id);
			}
		}

		cir.setReturnValue(map);
	}

	@Inject(method = "registerEntities", at = @At("RETURN"), cancellable = true, remap = false)
	private void registerEntitiesFix(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
		Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();

		if (LoadingDoneListener.allowConversion) {
			for (String id : LifecycleUtils.MOD_ENTITIES) {
				registerSimple(map, id);
			}
		}

		cir.setReturnValue(map);
	}
}
