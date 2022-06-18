package io.github.betterthanupdates.forge.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import forge.ISpecialResistance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Shadow
	public float power;

	@Shadow
	private World world;

	@Shadow
	public double x;

	@Shadow
	public double y;

	@Shadow
	public double z;

	@Shadow
	public Entity cause;

	@Shadow
	public Set damagedBlocks;

	@Shadow
	public boolean causeFires;

	@Shadow
	private Random random;

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void kaboomPhase1() {
		float f = this.power;
		int i = 16;

		for (int j = 0; j < i; ++j) {
			for (int l = 0; l < i; ++l) {
				for (int j1 = 0; j1 < i; ++j1) {
					if (j == 0 || j == i - 1 || l == 0 || l == i - 1 || j1 == 0 || j1 == i - 1) {
						double d = (double) ((float) j / ((float) i - 1.0F) * 2.0F - 1.0F);
						double d1 = (double) ((float) l / ((float) i - 1.0F) * 2.0F - 1.0F);
						double d2 = (double) ((float) j1 / ((float) i - 1.0F) * 2.0F - 1.0F);
						double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
						d /= d3;
						d1 /= d3;
						d2 /= d3;
						float f1 = this.power * (0.7F + this.world.rand.nextFloat() * 0.6F);
						double d5 = this.x;
						double d7 = this.y;
						double d9 = this.z;

						for (float f2 = 0.3F; !(f1 <= 0.0F); f1 -= f2 * 0.75F) {
							int j4 = MathHelper.floor(d5);
							int k4 = MathHelper.floor(d7);
							int l4 = MathHelper.floor(d9);
							int i5 = this.world.getBlockId(j4, k4, l4);

							if (i5 > 0) {
								if (Block.BY_ID[i5] instanceof ISpecialResistance) {
									ISpecialResistance isr = (ISpecialResistance) Block.BY_ID[i5];
									f1 -= (isr.getSpecialExplosionResistance(this.world, j4, k4, l4, this.x, this.y, this.z, this.cause) + 0.3F) * f2;
								} else {
									f1 -= (Block.BY_ID[i5].getBlastResistance(this.cause) + 0.3F) * f2;
								}
							}

							if (f1 > 0.0F) {
								this.damagedBlocks.add(new BlockPos(j4, k4, l4));
							}

							d5 += d * (double) f2;
							d7 += d1 * (double) f2;
							d9 += d2 * (double) f2;
						}
					}
				}
			}
		}

		this.power *= 2.0F;
		int k = MathHelper.floor(this.x - (double) this.power - 1.0);
		int i1 = MathHelper.floor(this.x + (double) this.power + 1.0);
		int k1 = MathHelper.floor(this.y - (double) this.power - 1.0);
		int l1 = MathHelper.floor(this.y + (double) this.power + 1.0);
		int i2 = MathHelper.floor(this.z - (double) this.power - 1.0);
		int j2 = MathHelper.floor(this.z + (double) this.power + 1.0);
		List list = this.world.getEntities(this.cause, Box.createAndAddToList((double) k, (double) k1, (double) i2, (double) i1, (double) l1, (double) j2));
		Vec3d vec3d = Vec3d.from(this.x, this.y, this.z);

		for (int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = (Entity) list.get(k2);
			double d4 = entity.distanceTo(this.x, this.y, this.z) / (double) this.power;

			if (d4 <= 1.0) {
				double d6 = entity.x - this.x;
				double d8 = entity.y - this.y;
				double d10 = entity.z - this.z;
				double d11 = (double) MathHelper.sqrt(d6 * d6 + d8 * d8 + d10 * d10);
				d6 /= d11;
				d8 /= d11;
				d10 /= d11;
				double d12 = (double) this.world.method_163(vec3d, entity.boundingBox);
				double d13 = (1.0 - d4) * d12;
				entity.damage(this.cause, (int) ((d13 * d13 + d13) / 2.0 * 8.0 * (double) this.power + 1.0));
				entity.xVelocity += d6 * d13;
				entity.yVelocity += d8 * d13;
				entity.zVelocity += d10 * d13;
			}
		}

		this.power = f;
		ArrayList arraylist = new ArrayList();
		arraylist.addAll(this.damagedBlocks);

		if (this.causeFires) {
			for (int l2 = arraylist.size() - 1; l2 >= 0; --l2) {
				BlockPos chunkposition = (BlockPos) arraylist.get(l2);
				int i3 = chunkposition.x;
				int j3 = chunkposition.y;
				int k3 = chunkposition.z;
				int l3 = this.world.getBlockId(i3, j3, k3);
				int i4 = this.world.getBlockId(i3, j3 - 1, k3);

				if (l3 == 0 && Block.FULL_OPAQUE[i4] && this.random.nextInt(3) == 0) {
					this.world.setBlock(i3, j3, k3, Block.FIRE.id);
				}
			}
		}
	}
}
