package io.github.betterthanupdates.apron.fixes.vanilla.infsprites;

import forge.BlockTextureParticles;
import java.util.List;

import net.mine_diver.infsprites.proxy.IRenderEngineHolder;
import net.mine_diver.infsprites.proxy.transform.Shadow;
import net.mine_diver.infsprites.util.compatibility.ForgePatcher;
import net.minecraft.mod_InfSprites;
import net.minecraft.client.entity.particle.DiggingParticleEntity;
import net.minecraft.client.entity.particle.ParticleEntity;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class EffectRendererProxy extends ParticleManager implements IRenderEngineHolder {
	@Shadow(
			obfuscatedName = "field_270"
	)
	private List<ParticleEntity>[] field_270;
	@Shadow(
			obfuscatedName = "field_271"
	)
	private TextureManager field_271;
	@Shadow(
			requiredPatcher = "infsprites:forge_patcher"
	)
	private List<BlockTextureParticles> effectList;

	public EffectRendererProxy(World world, TextureManager renderengine) {
		super(world, renderengine);
	}

	@Override
	public void method_324(Entity entity, float f) {
		float f1 = MathHelper.cos(entity.yaw * 3.141593F / 180.0F);
		float f2 = MathHelper.sin(entity.yaw * 3.141593F / 180.0F);
		float f3 = -f2 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f4 = f1 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f5 = MathHelper.cos(entity.pitch * 3.141593F / 180.0F);
		ParticleEntity.field_2645 = entity.prevRenderX + (entity.x - entity.prevRenderX) * (double)f;
		ParticleEntity.field_2646 = entity.prevRenderY + (entity.y - entity.prevRenderY) * (double)f;
		ParticleEntity.field_2647 = entity.prevRenderZ + (entity.z - entity.prevRenderZ) * (double)f;

		int j;
		int k;
		ParticleEntity entityfx;
		for(int i = 0; i < 3; ++i) {
			if (this.field_270 [i].size() != 0) {
				j = 0;
				if (i == 0) {
					j = this.field_271.getTextureId("/particles.png");
				}

				if (i == 1) {
					j = this.field_271.getTextureId("/terrain.png");
				}

				if (i == 2) {
					j = this.field_271.getTextureId("/gui/items.png");
				}

				GL11.glBindTexture(3553, j);
				Tessellator tessellator = Tessellator.INSTANCE;

				for(k = 0; k < this.field_270 [i].size(); ++k) {
					entityfx = (ParticleEntity)this.field_270 [i].get(k);
					if (!net.mine_diver.infsprites.util.compatibility.ForgePatcher.installed || !(entityfx instanceof DiggingParticleEntity)) {
						if (i == 1) {
							GL11.glBindTexture(3553, net.mine_diver.infsprites.render.TextureManager.getTerrain(mod_InfSprites.getParticleTextureIndex(entityfx) >> 8));
						}

						tessellator.start();
						entityfx.method_2002(tessellator, f, f1, f5, f2, f3, f4);
						tessellator.tessellate();
					}
				}
			}
		}

		if (ForgePatcher.installed) {
			Tessellator tessellator = Tessellator.INSTANCE;

			for(j = 0; j < this.effectList.size(); ++j) {
				BlockTextureParticles blocktextureparticles = (BlockTextureParticles)this.effectList.get(j);
				GL11.glBindTexture(3553, this.field_271.getTextureId(blocktextureparticles.texture));
				tessellator.start();

				for(k = 0; k < blocktextureparticles.effects.size(); ++k) {
					entityfx = (ParticleEntity)blocktextureparticles.effects.get(k);
					if (blocktextureparticles.texture == "/terrain.png") {
						GL11.glBindTexture(3553, net.mine_diver.infsprites.render.TextureManager.getTerrain(mod_InfSprites.getParticleTextureIndex(entityfx) >> 8));
					}

					entityfx.method_2002(tessellator, f, f1, f5, f2, f3, f4);
				}

				tessellator.tessellate();
			}
		}

	}

	@Override
	public void setRenderEngine(TextureManager newRenderEngine) {
		this.field_271 = newRenderEngine;
	}
}
