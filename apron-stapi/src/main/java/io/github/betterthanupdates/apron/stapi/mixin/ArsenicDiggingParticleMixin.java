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
		textureIndex = ApronStAPICompat.fixBlockTexture(textureIndex, ((DiggingParticleEntityAccessor) entity).getBlock());

		return instance.getTexture(textureIndex);
	}
}
