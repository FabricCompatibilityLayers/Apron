package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooksClient;
import net.minecraft.block.material.Material;
import net.minecraft.class_573;
import net.minecraft.class_598;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.source.WorldSource;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
	 * @author Forge
	 */
	@Overwrite
	public void method_1841(float f, long l) {
		GL11.glEnable(2884);
		GL11.glEnable(2929);
		if (this.client.viewEntity == null) {
			this.client.viewEntity = this.client.player;
		}

		this.method_1838(f);
		LivingEntity entityliving = this.client.viewEntity;
		WorldRenderer renderglobal = this.client.worldRenderer;
		ParticleManager effectrenderer = this.client.particleManager;
		double d = entityliving.prevRenderX + (entityliving.x - entityliving.prevRenderX) * (double) f;
		double d1 = entityliving.prevRenderY + (entityliving.y - entityliving.prevRenderY) * (double) f;
		double d2 = entityliving.prevRenderZ + (entityliving.z - entityliving.prevRenderZ) * (double) f;
		WorldSource ichunkprovider = this.client.world.getCache();
		if (ichunkprovider instanceof ChunkCache) {
			ChunkCache chunkproviderloadorgenerate = (ChunkCache) ichunkprovider;
			int j = MathHelper.floor((float) ((int) d)) >> 4;
			int k = MathHelper.floor((float) ((int) d2)) >> 4;
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
			class_598.method_1973();
			if (this.client.options.viewDistance < 2) {
				this.method_1842(-1, f);
				renderglobal.renderSky(f);
			}

			GL11.glEnable(2912);
			this.method_1842(1, f);
			if (this.client.options.ao) {
				GL11.glShadeModel(7425);
			}

			class_573 frustrum = new class_573();
			frustrum.method_2006(d, d1, d2);
			this.client.worldRenderer.method_1550(frustrum, f);
			if (i == 0) {
				while (!this.client.worldRenderer.method_1549(entityliving, false) && l != 0L) {
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
			renderglobal.method_1548(entityliving, 0, (double) f);
			GL11.glShadeModel(7424);
			RenderHelper.enableLighting();
			renderglobal.method_1544(entityliving.getPosition(f), frustrum, f);
			effectrenderer.method_327(entityliving, f);
			RenderHelper.disableLighting();
			this.method_1842(0, f);
			effectrenderer.method_324(entityliving, f);
			if (this.client.hitResult != null && entityliving.isInFluid(Material.WATER) && entityliving instanceof PlayerEntity) {
				PlayerEntity entityplayer = (PlayerEntity) entityliving;
				GL11.glDisable(3008);
				if (!ForgeHooksClient.onBlockHighlight(renderglobal, entityplayer, this.client.hitResult, 0, entityplayer.inventory.getHeldItem(), f)) {
					renderglobal.method_1547(entityplayer, this.client.hitResult, 0, entityplayer.inventory.getHeldItem(), f);
					renderglobal.method_1554(entityplayer, this.client.hitResult, 0, entityplayer.inventory.getHeldItem(), f);
				}

				GL11.glEnable(3008);
			}

			GL11.glBlendFunc(770, 771);
			this.method_1842(0, f);
			GL11.glEnable(3042);
			GL11.glDisable(2884);
			GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/terrain.png"));
			if (this.client.options.fancyGraphics) {
				if (this.client.options.ao) {
					GL11.glShadeModel(7425);
				}

				GL11.glColorMask(false, false, false, false);
				int i1 = renderglobal.method_1548(entityliving, 1, (double) f);
				if (this.client.options.anaglyph3d) {
					if (field_2341 == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				if (i1 > 0) {
					renderglobal.method_1540(1, (double) f);
				}

				GL11.glShadeModel(7424);
			} else {
				renderglobal.method_1548(entityliving, 1, (double) f);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(2884);
			GL11.glDisable(3042);
			if (this.field_2331 == 1.0 && entityliving instanceof PlayerEntity && this.client.hitResult != null && !entityliving.isInFluid(Material.WATER)) {
				PlayerEntity entityplayer1 = (PlayerEntity) entityliving;
				GL11.glDisable(3008);
				if (!ForgeHooksClient.onBlockHighlight(renderglobal, entityplayer1, this.client.hitResult, 0, entityplayer1.inventory.getHeldItem(), f)) {
					renderglobal.method_1547(entityplayer1, this.client.hitResult, 0, entityplayer1.inventory.getHeldItem(), f);
					renderglobal.method_1554(entityplayer1, this.client.hitResult, 0, entityplayer1.inventory.getHeldItem(), f);
				}

				GL11.glEnable(3008);
			}

			this.method_1847(f);
			GL11.glDisable(2912);
			if (this.field_2352 == null) {
			}

			this.method_1842(0, f);
			GL11.glEnable(2912);
			renderglobal.method_1552(f);
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
