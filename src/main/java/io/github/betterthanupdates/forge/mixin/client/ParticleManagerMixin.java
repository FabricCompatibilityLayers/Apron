package io.github.betterthanupdates.forge.mixin.client;

import java.util.ArrayList;
import java.util.List;

import forge.BlockTextureParticles;
import forge.ITextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.entity.particle.DiggingParticleEntity;
import net.minecraft.client.entity.particle.ParticleEntity;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.client.particle.ForgeParticleManager;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin implements ForgeParticleManager {
	@Shadow
	private TextureManager textureManager;
	@Shadow
	protected World world;

	@Shadow
	public abstract void addParticle(ParticleEntity particle);

	// Forge Fields
	@Unique
	private final List<BlockTextureParticles> effectList = new ArrayList<>();

	@Inject(method = "method_320", at = @At("RETURN"))
	private void reforged$method_320(CallbackInfo ci) {
		for (int x = 0; x < this.effectList.size(); ++x) {
			BlockTextureParticles entry = this.effectList.get(x);

			for (int y = 0; y < entry.effects.size(); ++y) {
				ParticleEntity entityfx = entry.effects.get(y);

				if (entityfx.removed) {
					entry.effects.remove(y--);
				}
			}

			if (this.effectList.size() == 0) {
				this.effectList.remove(x--);
			}
		}
	}

	@Redirect(method = "method_324", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/particle/ParticleEntity;method_2002(Lnet/minecraft/client/render/Tessellator;FFFFFF)V", ordinal = 0))
	private void reforged$method_2002(ParticleEntity entityfx, Tessellator tessellator, float f, float f1, float f5, float f2, float f3, float f4) {
		if (!(entityfx instanceof DiggingParticleEntity)) {
			entityfx.method_2002(tessellator, f, f1, f5, f2, f3, f4);
		}
	}

	@Inject(method = "method_324", at = @At("RETURN"))
	private void reforged$method_324(Entity entity, float f, CallbackInfo ci) {
		float f1 = MathHelper.cos(entity.yaw * 3.141593F / 180.0F);
		float f2 = MathHelper.sin(entity.yaw * 3.141593F / 180.0F);
		float f3 = -f2 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f4 = f1 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f5 = MathHelper.cos(entity.pitch * 3.141593F / 180.0F);

		Tessellator tessellator = Tessellator.INSTANCE;

		for (BlockTextureParticles entry : this.effectList) {
			GL11.glBindTexture(3553, this.textureManager.getTextureId(entry.texture));
			tessellator.start();

			for (int y = 0; y < entry.effects.size(); ++y) {
				ParticleEntity entityfx = entry.effects.get(y);
				entityfx.method_2002(tessellator, f, f1, f5, f2, f3, f4);
			}

			tessellator.tessellate();
		}
	}

	@Inject(method = "method_323", at = @At("RETURN"))
	private void reforged$method_323(World par1, CallbackInfo ci) {
		for (BlockTextureParticles entry : this.effectList) {
			entry.effects.clear();
		}

		this.effectList.clear();
	}

	Block cachedBlock;

	@Inject(method = "addBlockBreakParticles", at = @At("HEAD"))
	private void reforged$addBlockBreakParticles(int j, int k, int l, int m, int par5, CallbackInfo ci) {
		if (m != 0) {
			this.cachedBlock = Block.BY_ID[m];
		}
	}

	@Redirect(method = "addBlockBreakParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/client/entity/particle/ParticleEntity;)V"))
	private void reforged$addBlockBreakParticles(ParticleManager instance, ParticleEntity particleEntity) {
		((ForgeParticleManager) instance).addDigParticleEffect((DiggingParticleEntity) particleEntity, this.cachedBlock);
	}

	Block cachedBlock2;

	@Inject(method = "addBlockClickParticle", at = @At("HEAD"))
	private void reforged$addBlockClickParticle(int j, int k, int l, int par4, CallbackInfo ci) {
		int id = this.world.getBlockId(j, k, l);

		if (id != 0) {
			this.cachedBlock2 = Block.BY_ID[id];
		}
	}

	@Redirect(method = "addBlockClickParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/client/entity/particle/ParticleEntity;)V"))
	private void reforged$addBlockClickParticle(ParticleManager instance, ParticleEntity particleEntity) {
		((ForgeParticleManager) instance).addDigParticleEffect((DiggingParticleEntity) particleEntity, this.cachedBlock2);
	}

	@Override
	public void addDigParticleEffect(DiggingParticleEntity dig_effect, Block block) {
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

		this.addParticle(dig_effect);
	}
}
