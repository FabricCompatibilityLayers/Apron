package io.github.betterthanupdates.apron.stapi.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import forge.ITextureProvider;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.particle.ArsenicDiggingParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.client.entity.particle.DiggingParticleEntity;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.SpritesheetInstance;

@Mixin(ArsenicDiggingParticle.class)
public class ArsenicDiggingParticleMixin {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/modificationstation/stationapi/api/client/texture/atlas/Atlas;getTexture(I)Lnet/modificationstation/stationapi/api/client/texture/atlas/Atlas$Sprite;"))
	private Atlas.Sprite apron$stapi$fixTextureIndex(Atlas instance, int textureIndex, @Local(ordinal = 0)DiggingParticleEntity entity) {
		if (((DiggingParticleEntityAccessor) entity).getBlock() instanceof ITextureProvider textureProvider) {
			SpritesheetInstance spritesheetInstance = ApronStAPICompat.SPRITESHEET_MAP.get(textureProvider.getTextureFile());

			if (spritesheetInstance != null) {
				if (spritesheetInstance.BLOCKS.containsKey(textureIndex)) {
					textureIndex = spritesheetInstance.BLOCKS.get(textureIndex);
				}
			}
		} else if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(textureIndex)) {
			textureIndex = ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(textureIndex);
		}

		return instance.getTexture(textureIndex);
	}
}
