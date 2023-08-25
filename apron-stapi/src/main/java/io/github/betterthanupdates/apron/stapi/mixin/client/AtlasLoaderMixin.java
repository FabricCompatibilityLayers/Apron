package io.github.betterthanupdates.apron.stapi.mixin.client;

import java.util.List;

import net.modificationstation.stationapi.api.client.texture.atlas.AtlasLoader;
import net.modificationstation.stationapi.api.client.texture.atlas.AtlasSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.ModContents;

@Mixin(AtlasLoader.class)
public class AtlasLoaderMixin {
	@Shadow
	@Final
	private List<AtlasSource> sources;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void apron$addVirtualAtlasSources(List sourcess, CallbackInfo ci) {
		ApronStAPICompat.getModContents().forEach(entry -> {
			ModContents modContents = entry.getValue();

			this.sources.addAll(modContents.TERRAIN.GENERATED_ATLASES);
			this.sources.addAll(modContents.GUI_ITEMS.GENERATED_ATLASES);
		});

		this.sources.addAll(ApronStAPICompat.ATLAS_SOURCE_LIST);
	}
}
