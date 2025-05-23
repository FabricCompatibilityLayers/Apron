package io.github.betterthanupdates.apron.stapi.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.Block;
import net.minecraft.client.particle.BlockParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;

@Mixin(BlockParticle.class)
public class DiggingParticleEntityMixin extends Particle {
	public DiggingParticleEntityMixin(World arg, double d, double e, double f, double g, double h, double i) {
		super(arg, d, e, f, g, h, i);
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getTexture(II)I"))
	private int apron$fixTexture(Block instance, int i, int j, Operation<Integer> operation) {
		int texture = operation.call(instance, i, j);

		texture = ApronStAPICompat.fixBlockTexture(texture, instance);

		return texture;
	}
}
