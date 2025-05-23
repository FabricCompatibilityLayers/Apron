package io.github.betterthanupdates.forge.mixin.client;

import java.util.ArrayList;
import java.util.List;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import forge.BlockTextureParticles;
import forge.ITextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_75;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.particle.BlockParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.client.particle.ForgeParticleManager;

@Environment(EnvType.CLIENT)
@Mixin(class_75.class)
public abstract class ParticleManagerMixin implements ForgeParticleManager {
	@Shadow
	private TextureManager field_271;
	@Shadow
	public abstract void method_325(Particle particle);

	// Forge Fields
	@Unique
	private final List<BlockTextureParticles> effectList = new ArrayList<>();

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_320", at = @At("RETURN"))
	private void forge$method_320(CallbackInfo ci) {
		for (int x = 0; x < this.effectList.size(); ++x) {
			BlockTextureParticles entry = this.effectList.get(x);

			for (int y = 0; y < entry.effects.size(); ++y) {
				Particle entityfx = entry.effects.get(y);

				if (entityfx.dead) {
					entry.effects.remove(y--);
				}
			}

			if (this.effectList.size() == 0) {
				this.effectList.remove(x--);
			}
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@WrapWithCondition(method = "method_324", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;method_2002(Lnet/minecraft/client/render/Tessellator;FFFFFF)V", ordinal = 0))
	private boolean forge$method_2002(Particle entityfx, Tessellator tessellator, float f, float f1, float f5, float f2, float f3, float f4) {
		return !(entityfx instanceof BlockParticle);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_324", at = @At("RETURN"))
	private void forge$method_324(Entity entity, float f, CallbackInfo ci) {
		float f1 = MathHelper.cos(entity.yaw * 3.141593F / 180.0F);
		float f2 = MathHelper.sin(entity.yaw * 3.141593F / 180.0F);
		float f3 = -f2 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f4 = f1 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f5 = MathHelper.cos(entity.pitch * 3.141593F / 180.0F);

		Tessellator tessellator = Tessellator.INSTANCE;

		for (BlockTextureParticles entry : this.effectList) {
			GL11.glBindTexture(3553, this.field_271.getTextureId(entry.texture));
			tessellator.startQuads();

			for (int y = 0; y < entry.effects.size(); ++y) {
				Particle entityfx = entry.effects.get(y);
				entityfx.method_2002(tessellator, f, f1, f5, f2, f3, f4);
			}

			tessellator.draw();
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_323", at = @At("RETURN"))
	private void forge$method_323(World par1, CallbackInfo ci) {
		for (BlockTextureParticles entry : this.effectList) {
			entry.effects.clear();
		}

		this.effectList.clear();
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_322", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_75;method_325(Lnet/minecraft/client/particle/Particle;)V"))
	private void forge$addBlockBreakParticles(class_75 instance, Particle particleEntity, @Local(ordinal = 0) Block block) {
		((ForgeParticleManager) instance).addDigParticleEffect((BlockParticle) particleEntity, block);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_321", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_75;method_325(Lnet/minecraft/client/particle/Particle;)V"))
	private void forge$addBlockClickParticle(class_75 instance, Particle particleEntity, @Local(ordinal = 0) Block block) {
		((ForgeParticleManager) instance).addDigParticleEffect((BlockParticle) particleEntity, block);
	}

	@Override
	public void addDigParticleEffect(BlockParticle dig_effect, Block block) {
		boolean added = false;
		String comp;

		if (block instanceof ITextureProvider) {
			comp = ((ITextureProvider) block).getTextureFile();
		} else {
			comp = "/terrain.png";
		}

		for (BlockTextureParticles entry : this.effectList) {
			if (entry.texture.equals(comp)) {
				entry.effects.add(dig_effect);
				added = true;
			}
		}

		if (!added) {
			BlockTextureParticles entry = new BlockTextureParticles();
			entry.texture = comp;
			entry.effects.add(dig_effect);
			this.effectList.add(entry);
		}

		this.method_325(dig_effect);
	}
}
