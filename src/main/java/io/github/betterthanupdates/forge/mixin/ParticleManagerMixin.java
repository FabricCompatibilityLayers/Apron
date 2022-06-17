package io.github.betterthanupdates.forge.mixin;

import forge.BlockTextureParticles;
import forge.ITextureProvider;
import io.github.betterthanupdates.forge.ForgeParticleManager;
import net.minecraft.block.Block;
import net.minecraft.client.entity.particle.DiggingParticleEntity;
import net.minecraft.client.entity.particle.ParticleEntity;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin implements ForgeParticleManager {

	@Shadow
	private List[] field_270;
	@Shadow
	private TextureManager textureManager;
	@Shadow
	protected World world;
	@Shadow
	private Random rand;

	@Shadow
	public abstract void addParticle(ParticleEntity arg);

	@Unique
	private List<BlockTextureParticles> effectList = new ArrayList<>();

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_320() {
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < this.field_270[i].size(); ++j) {
				ParticleEntity entityfx = (ParticleEntity) this.field_270[i].get(j);
				entityfx.tick();
				if (entityfx.removed) {
					this.field_270[i].remove(j--);
				}
			}
		}

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

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_324(Entity entity, float f) {
		float f1 = MathHelper.cos(entity.yaw * 3.141593F / 180.0F);
		float f2 = MathHelper.sin(entity.yaw * 3.141593F / 180.0F);
		float f3 = -f2 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f4 = f1 * MathHelper.sin(entity.pitch * 3.141593F / 180.0F);
		float f5 = MathHelper.cos(entity.pitch * 3.141593F / 180.0F);
		ParticleEntity.field_2645 = entity.prevRenderX + (entity.x - entity.prevRenderX) * (double) f;
		ParticleEntity.field_2646 = entity.prevRenderY + (entity.y - entity.prevRenderY) * (double) f;
		ParticleEntity.field_2647 = entity.prevRenderZ + (entity.z - entity.prevRenderZ) * (double) f;

		for (int i = 0; i < 3; ++i) {
			if (this.field_270[i].size() != 0) {
				int j = 0;
				if (i == 0) {
					j = this.textureManager.getTextureId("/particles.png");
				}

				if (i == 1) {
					j = this.textureManager.getTextureId("/terrain.png");
				}

				if (i == 2) {
					j = this.textureManager.getTextureId("/gui/items.png");
				}

				GL11.glBindTexture(3553, j);
				Tessellator tessellator = Tessellator.INSTANCE;
				tessellator.start();

				for (int k = 0; k < this.field_270[i].size(); ++k) {
					ParticleEntity entityfx = (ParticleEntity) this.field_270[i].get(k);
					if (!(entityfx instanceof DiggingParticleEntity)) {
						entityfx.method_2002(tessellator, f, f1, f5, f2, f3, f4);
					}
				}

				tessellator.draw();
			}
		}

		Tessellator tessellator = Tessellator.INSTANCE;

		for (int x = 0; x < this.effectList.size(); ++x) {
			BlockTextureParticles entry = this.effectList.get(x);
			GL11.glBindTexture(3553, this.textureManager.getTextureId(entry.texture));
			tessellator.start();

			for (int y = 0; y < entry.effects.size(); ++y) {
				ParticleEntity entityfx = entry.effects.get(y);
				entityfx.method_2002(tessellator, f, f1, f5, f2, f3, f4);
			}

			tessellator.draw();
		}

	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_323(World world) {
		this.world = world;

		for (int i = 0; i < 4; ++i) {
			this.field_270[i].clear();
		}

		for (BlockTextureParticles entry : this.effectList) {
			entry.effects.clear();
		}

		this.effectList.clear();
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void addBlockBreakParticles(int i, int j, int k, int l, int i1) {
		if (l != 0) {
			Block block = Block.BY_ID[l];
			int j1 = 4;

			for (int k1 = 0; k1 < j1; ++k1) {
				for (int l1 = 0; l1 < j1; ++l1) {
					for (int i2 = 0; i2 < j1; ++i2) {
						double d = (double) i + ((double) k1 + 0.5) / (double) j1;
						double d1 = (double) j + ((double) l1 + 0.5) / (double) j1;
						double d2 = (double) k + ((double) i2 + 0.5) / (double) j1;
						int j2 = this.rand.nextInt(6);
						DiggingParticleEntity dig_effect = new DiggingParticleEntity(
								this.world, d, d1, d2, d - (double) i - 0.5, d1 - (double) j - 0.5, d2 - (double) k - 0.5, block, j2, i1
						);
						dig_effect.multiplyColor(i, j, k);
						this.addDigParticleEffect(dig_effect, block);
					}
				}
			}

		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void addBlockClickParticle(int i, int j, int k, int l) {
		int i1 = this.world.getBlockId(i, j, k);
		if (i1 != 0) {
			Block block = Block.BY_ID[i1];
			float f = 0.1F;
			double d = (double) i + this.rand.nextDouble() * (block.maxX - block.minX - (double) (f * 2.0F)) + (double) f + block.minX;
			double d1 = (double) j + this.rand.nextDouble() * (block.maxY - block.minY - (double) (f * 2.0F)) + (double) f + block.minY;
			double d2 = (double) k + this.rand.nextDouble() * (block.maxZ - block.minZ - (double) (f * 2.0F)) + (double) f + block.minZ;
			if (l == 0) {
				d1 = (double) j + block.minY - (double) f;
			}

			if (l == 1) {
				d1 = (double) j + block.maxY + (double) f;
			}

			if (l == 2) {
				d2 = (double) k + block.minZ - (double) f;
			}

			if (l == 3) {
				d2 = (double) k + block.maxZ + (double) f;
			}

			if (l == 4) {
				d = (double) i + block.minX - (double) f;
			}

			if (l == 5) {
				d = (double) i + block.maxX + (double) f;
			}

			DiggingParticleEntity dig_effect = new DiggingParticleEntity(this.world, d, d1, d2, 0.0, 0.0, 0.0, block, l, this.world.getBlockMeta(i, j, k));
			dig_effect.multiplyColor(i, j, k);
			dig_effect.method_2000(0.2F);
			dig_effect.method_2001(0.6F);
			this.addDigParticleEffect(dig_effect, block);
		}
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
