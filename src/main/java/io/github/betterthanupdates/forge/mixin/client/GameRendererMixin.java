package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.WorldEventRenderer;
import net.minecraft.client.util.Camera;
import net.minecraft.client.util.CameraFrustum;
import net.minecraft.client.util.CameraView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.source.WorldSource;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	private Minecraft client;

	@Shadow
	public abstract void method_1838(float f);

	@Shadow
	public static int field_2341;

	@Shadow
	protected abstract void method_1852(float f);

	@Shadow
	protected abstract void method_1840(float f, int i);

	@Shadow
	protected abstract void method_1842(int i, float f);

	@Shadow
	private double field_2331;

	@Shadow
	protected abstract void method_1847(float f);

	@Shadow
	private Entity field_2352;

	@Shadow
	protected abstract void method_1845(float f, int i);

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void method_1841(float f, long l) {
		GL11.glEnable(2884);
		GL11.glEnable(2929);

		if (this.client.viewEntity == null) {
			this.client.viewEntity = this.client.player;
		}

		this.method_1838(f);
		LivingEntity livingEntity = this.client.viewEntity;
		WorldEventRenderer renderGlobal = this.client.worldRenderer;
		ParticleManager effectRenderer = this.client.particleManager;
		double x = livingEntity.prevRenderX + (livingEntity.x - livingEntity.prevRenderX) * (double) f;
		double y = livingEntity.prevRenderY + (livingEntity.y - livingEntity.prevRenderY) * (double) f;
		double z = livingEntity.prevRenderZ + (livingEntity.z - livingEntity.prevRenderZ) * (double) f;
		WorldSource worldSource = this.client.world.getCache();

		if (worldSource instanceof ChunkCache) {
			ChunkCache chunkproviderloadorgenerate = (ChunkCache) worldSource;
			int j = MathHelper.floor((float) ((int) x)) >> 4;
			int k = MathHelper.floor((float) ((int) z)) >> 4;
			chunkproviderloadorgenerate.method_1242(j, k);
		}

		for (int i = 0; i < 2; ++i) {
			if (this.client.options.anaglyph3d) {
				field_2341 = i;

				if (field_2341 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			GL11.glViewport(0, 0, this.client.actualWidth, this.client.actualHeight);
			this.method_1852(f);
			GL11.glClear(16640);
			GL11.glEnable(2884);
			this.method_1840(f, i);
			CameraFrustum.getInstance();

			if (this.client.options.viewDistance < 2) {
				this.method_1842(-1, f);
				renderGlobal.renderSky(f);
			}

			GL11.glEnable(2912);
			this.method_1842(1, f);

			if (this.client.options.ao) {
				GL11.glShadeModel(7425);
			}

			CameraView frustum = new Camera();
			frustum.setPosition(x, y, z);
			this.client.worldRenderer.method_1550(frustum, f);

			if (i == 0) {
				while (!this.client.worldRenderer.method_1549(livingEntity, false) && l != 0L) {
					long l1 = l - System.nanoTime();

					if (l1 < 0L || l1 > 1000000000L) {
						break;
					}
				}
			}

			this.method_1842(0, f);
			GL11.glEnable(2912);
			GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/terrain.png"));
			RenderHelper.disableLighting();
			renderGlobal.method_1548(livingEntity, 0, (double) f);
			GL11.glShadeModel(7424);
			RenderHelper.enableLighting();
			renderGlobal.renderEntities(livingEntity.getPosition(f), frustum, f);
			effectRenderer.method_327(livingEntity, f);
			RenderHelper.disableLighting();
			this.method_1842(0, f);
			effectRenderer.method_324(livingEntity, f);

			if (this.client.hitResult != null && livingEntity.isInFluid(Material.WATER) && livingEntity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) livingEntity;
				GL11.glDisable(3008);

				if (!ForgeHooksClient.onBlockHighlight(renderGlobal, player, this.client.hitResult, 0, player.inventory.getHeldItem(), f)) {
					renderGlobal.method_1547(player, this.client.hitResult, 0, player.inventory.getHeldItem(), f);
					renderGlobal.method_1554(player, this.client.hitResult, 0, player.inventory.getHeldItem(), f);
				}

				GL11.glEnable(3008);
			}

			GL11.glBlendFunc(770, 771);
			this.method_1842(0, f);
			GL11.glEnable(3042);
			GL11.glDisable(2884);
			GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/terrain.png"));

			// Some stuff is changed by ShockAhPI here.
			if (this.client.options.fancyGraphics) {
				if (this.client.options.ao) {
					GL11.glShadeModel(7425);
				}

				if (this.client.options.anaglyph3d) {
					if (field_2341 == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				}

				int i1 = renderGlobal.method_1548(livingEntity, 1, (double) f);
				GL11.glShadeModel(7424);
			} else {
				renderGlobal.method_1548(livingEntity, 1, (double) f);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(2884);
			GL11.glDisable(3042);

			if (this.field_2331 == 1.0 && livingEntity instanceof PlayerEntity && this.client.hitResult != null && !livingEntity.isInFluid(Material.WATER)) {
				PlayerEntity player = (PlayerEntity) livingEntity;
				GL11.glDisable(3008);

				if (!ForgeHooksClient.onBlockHighlight(renderGlobal, player, this.client.hitResult, 0, player.inventory.getHeldItem(), f)) {
					renderGlobal.method_1547(player, this.client.hitResult, 0, player.inventory.getHeldItem(), f);
					renderGlobal.method_1554(player, this.client.hitResult, 0, player.inventory.getHeldItem(), f);
				}

				GL11.glEnable(3008);
			}

			this.method_1847(f);
			GL11.glDisable(2912);

			if (this.field_2352 == null) {
				// ???
			}

			this.method_1842(0, f);
			GL11.glEnable(2912);
			renderGlobal.method_1552(f);
			GL11.glDisable(2912);
			this.method_1842(1, f);

			if (this.field_2331 == 1.0) {
				GL11.glClear(256);
				this.method_1845(f, i);
			}

			if (!this.client.options.anaglyph3d) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}
}
