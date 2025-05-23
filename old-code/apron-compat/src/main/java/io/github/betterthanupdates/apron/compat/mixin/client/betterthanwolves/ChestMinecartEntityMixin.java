package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.mod_FCBetterThanWolves;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(MinecartEntity.class)
public abstract class ChestMinecartEntityMixin extends Entity {
	@Shadow
	public int field_2273;

	@Shadow
	public int field_2272;

	@Shadow
	private int field_2282;

	@Shadow
	private double field_2283;

	@Shadow
	private double field_2284;

	@Shadow
	private double field_2285;

	@Shadow
	private double field_2286;

	@Shadow
	private double field_2287;

	@Shadow
	public abstract Vec3d method_1813(double d, double e, double f);

	@Shadow
	@Final
	private static int[][][] field_2281;

	@Shadow
	public int field_2275;

	@Shadow
	public double field_2277;

	@Shadow
	public double field_2278;

	@Shadow
	private boolean field_2280;

	@Shadow
	public int field_2276;

	public ChestMinecartEntityMixin(World arg) {
		super(arg);
	}

	/**
	 * @author BTW
	 * @reason currently difficult to convert
	 */
	@Overwrite
	public void tick() {
		if (this.field_2273 > 0) {
			--this.field_2273;
		}

		if (this.field_2272 > 0) {
			--this.field_2272;
		}

		if (this.world.isRemote && this.field_2282 > 0) {
			if (this.field_2282 > 0) {
				double d = this.x + (this.field_2283 - this.x) / (double)this.field_2282;
				double d1 = this.y + (this.field_2284 - this.y) / (double)this.field_2282;
				double d3 = this.z + (this.field_2285 - this.z) / (double)this.field_2282;
				double d4 = this.field_2286 - (double)this.yaw;

				while(d4 < -180.0) {
					d4 += 360.0;
				}

				while(d4 >= 180.0) {
					d4 -= 360.0;
				}

				this.yaw = (float)((double)this.yaw + d4 / (double)this.field_2282);
				this.pitch = (float)((double)this.pitch + (this.field_2287 - (double)this.pitch) / (double)this.field_2282);
				--this.field_2282;
				this.method_1340(d, d1, d3);
				this.method_1342(this.yaw, this.pitch);
			} else {
				this.method_1340(this.x, this.y, this.z);
				this.method_1342(this.yaw, this.pitch);
			}
		} else {
			this.prevX = this.x;
			this.prevY = this.y;
			this.prevZ = this.z;
			this.velocityY -= 0.04F;
			int i = MathHelper.floor(this.x);
			int j = MathHelper.floor(this.y);
			int k = MathHelper.floor(this.z);
			if (RailBlock.method_1109(this.world, i, j - 1, k)) {
				--j;
			}

			double d2 = 0.4;
			boolean flag = false;
			double d5 = 0.0078125;
			int l = this.world.getBlockId(i, j, k);
			if (RailBlock.method_1107(l)) {
				Vec3d vec3d = this.method_1813(this.x, this.y, this.z);
				int i1 = this.world.getBlockMeta(i, j, k);
				this.y = (double)j;
				boolean flag1 = false;
				boolean flag2 = false;
				if (l == Block.POWERED_RAIL.id) {
					flag1 = (i1 & 8) != 0;
					flag2 = !flag1;
				}

				if (((RailBlock)Block.BLOCKS[l]).method_1108()) {
					i1 &= 7;
				}

				if (i1 >= 2 && i1 <= 5) {
					this.y = (double)(j + 1);
				}

				if (i1 == 2) {
					this.velocityX -= d5;
				}

				if (i1 == 3) {
					this.velocityX += d5;
				}

				if (i1 == 4) {
					this.velocityZ += d5;
				}

				if (i1 == 5) {
					this.velocityZ -= d5;
				}

				int[][] ai = field_2281[i1];
				double d9 = (double)(ai[1][0] - ai[0][0]);
				double d10 = (double)(ai[1][2] - ai[0][2]);
				double d11 = Math.sqrt(d9 * d9 + d10 * d10);
				double d12 = this.velocityX * d9 + this.velocityZ * d10;
				if (d12 < 0.0) {
					d9 = -d9;
					d10 = -d10;
				}

				double d13 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
				this.velocityX = d13 * d9 / d11;
				this.velocityZ = d13 * d10 / d11;
				if (flag2) {
					double d16 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
					if (d16 < 0.03) {
						this.velocityX *= 0.0;
						this.velocityY *= 0.0;
						this.velocityZ *= 0.0;
					} else {
						this.velocityX *= 0.5;
						this.velocityY *= 0.0;
						this.velocityZ *= 0.5;
					}
				}

				double d17 = 0.0;
				double d18 = (double)i + 0.5 + (double)ai[0][0] * 0.5;
				double d19 = (double)k + 0.5 + (double)ai[0][2] * 0.5;
				double d20 = (double)i + 0.5 + (double)ai[1][0] * 0.5;
				double d21 = (double)k + 0.5 + (double)ai[1][2] * 0.5;
				d9 = d20 - d18;
				d10 = d21 - d19;
				if (d9 == 0.0) {
					this.x = (double)i + 0.5;
					d17 = this.z - (double)k;
				} else if (d10 == 0.0) {
					this.z = (double)k + 0.5;
					d17 = this.x - (double)i;
				} else {
					double d22 = this.x - d18;
					double d24 = this.z - d19;
					double d26 = (d22 * d9 + d24 * d10) * 2.0;
					d17 = d26;
				}

				this.x = d18 + d9 * d17;
				this.z = d19 + d10 * d17;
				this.method_1340(this.x, this.y + (double)this.eyeHeight, this.z);
				double d23 = this.velocityX;
				double d25 = this.velocityZ;
				if (this.field_1594 != null) {
					d23 *= 0.75;
					d25 *= 0.75;
				}

				if (d23 < -d2) {
					d23 = -d2;
				}

				if (d23 > d2) {
					d23 = d2;
				}

				if (d25 < -d2) {
					d25 = -d2;
				}

				if (d25 > d2) {
					d25 = d2;
				}

				this.move(d23, 0.0, d25);
				if (ai[0][1] != 0 && MathHelper.floor(this.x) - i == ai[0][0] && MathHelper.floor(this.z) - k == ai[0][2]) {
					this.method_1340(this.x, this.y + (double)ai[0][1], this.z);
				} else if (ai[1][1] != 0 && MathHelper.floor(this.x) - i == ai[1][0] && MathHelper.floor(this.z) - k == ai[1][2]) {
					this.method_1340(this.x, this.y + (double)ai[1][1], this.z);
				}

				if (this.field_1594 != null) {
					this.velocityX *= 0.997F;
					this.velocityY *= 0.0;
					this.velocityZ *= 0.997F;
				} else {
					if (this.field_2275 == 2) {
						double d27 = (double)MathHelper.sqrt(this.field_2277 * this.field_2277 + this.field_2278 * this.field_2278);
						if (d27 > 0.01) {
							flag = true;
							this.field_2277 /= d27;
							this.field_2278 /= d27;
							double d29 = 0.04;
							this.velocityX *= 0.8F;
							this.velocityY *= 0.0;
							this.velocityZ *= 0.8F;
							this.velocityX += this.field_2277 * d29;
							this.velocityZ += this.field_2278 * d29;
						} else {
							this.velocityX *= 0.9F;
							this.velocityY *= 0.0;
							this.velocityZ *= 0.9F;
						}
					}

					// TODO: PATCH STARTS HERE
					if (mod_FCBetterThanWolves.fcDisableMinecartChanges) {
						this.velocityX *= 0.96F;
						this.velocityY *= 0.0;
						this.velocityZ *= 0.96F;
					} else {
						this.velocityX *= 0.985;
						this.velocityY *= 0.0;
						this.velocityZ *= 0.985;
					}
					// PATCH ENDS HERE
				}

				Vec3d vec3d1 = this.method_1813(this.x, this.y, this.z);
				if (vec3d1 != null && vec3d != null) {
					double d28 = (vec3d.y - vec3d1.y) * 0.05;
					double d14 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
					if (d14 > 0.0) {
						this.velocityX = this.velocityX / d14 * (d14 + d28);
						this.velocityZ = this.velocityZ / d14 * (d14 + d28);
					}

					this.method_1340(this.x, vec3d1.y, this.z);
				}

				int k1 = MathHelper.floor(this.x);
				int l1 = MathHelper.floor(this.z);
				if (k1 != i || l1 != k) {
					double d15 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
					this.velocityX = d15 * (double)(k1 - i);
					this.velocityZ = d15 * (double)(l1 - k);
				}

				if (this.field_2275 == 2) {
					double d30 = (double)MathHelper.sqrt(this.field_2277 * this.field_2277 + this.field_2278 * this.field_2278);
					if (d30 > 0.01 && this.velocityX * this.velocityX + this.velocityZ * this.velocityZ > 0.001) {
						this.field_2277 /= d30;
						this.field_2278 /= d30;
						if (this.field_2277 * this.velocityX + this.field_2278 * this.velocityZ < 0.0) {
							this.field_2277 = 0.0;
							this.field_2278 = 0.0;
						} else {
							this.field_2277 = this.velocityX;
							this.field_2278 = this.velocityZ;
						}
					}
				}

				if (flag1) {
					double d31 = Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
					if (d31 > 0.01) {
						double d32 = 0.06;
						this.velocityX += this.velocityX / d31 * d32;
						this.velocityZ += this.velocityZ / d31 * d32;
					} else if (i1 == 1) {
						if (this.world.method_1780(i - 1, j, k)) {
							this.velocityX = 0.02;
						} else if (this.world.method_1780(i + 1, j, k)) {
							this.velocityX = -0.02;
						}
					} else if (i1 == 0) {
						if (this.world.method_1780(i, j, k - 1)) {
							this.velocityZ = 0.02;
						} else if (this.world.method_1780(i, j, k + 1)) {
							this.velocityZ = -0.02;
						}
					}
				}
			} else {
				if (this.velocityX < -d2) {
					this.velocityX = -d2;
				}

				if (this.velocityX > d2) {
					this.velocityX = d2;
				}

				if (this.velocityZ < -d2) {
					this.velocityZ = -d2;
				}

				if (this.velocityZ > d2) {
					this.velocityZ = d2;
				}

				if (this.field_1623) {
					this.velocityX *= 0.5;
					this.velocityY *= 0.5;
					this.velocityZ *= 0.5;
				}

				this.move(this.velocityX, this.velocityY, this.velocityZ);
				if (!this.field_1623) {
					this.velocityX *= 0.95F;
					this.velocityY *= 0.95F;
					this.velocityZ *= 0.95F;
				}
			}

			this.pitch = 0.0F;
			double d6 = this.prevX - this.x;
			double d7 = this.prevZ - this.z;
			if (d6 * d6 + d7 * d7 > 0.001) {
				this.yaw = (float)(Math.atan2(d7, d6) * 180.0 / Math.PI);
				if (this.field_2280) {
					this.yaw += 180.0F;
				}
			}

			double d8 = (double)(this.yaw - this.prevYaw);

			while(d8 >= 180.0) {
				d8 -= 360.0;
			}

			while(d8 < -180.0) {
				d8 += 360.0;
			}

			if (d8 < -170.0 || d8 >= 170.0) {
				this.yaw += 180.0F;
				this.field_2280 = !this.field_2280;
			}

			this.method_1342(this.yaw, this.pitch);
			List list = this.world.getEntities(this, this.boundingBox.expand(0.2F, 0.0, 0.2F));
			if (list != null && list.size() > 0) {
				for(int j1 = 0; j1 < list.size(); ++j1) {
					Entity entity = (Entity)list.get(j1);
					if (entity != this.field_1594 && entity.method_1380() && entity instanceof MinecartEntity) {
						entity.method_1353(this);
					}
				}
			}

			if (this.field_1594 != null && this.field_1594.dead) {
				this.field_1594 = null;
			}

			if (flag && this.random.nextInt(4) == 0) {
				--this.field_2276;
				if (this.field_2276 < 0) {
					this.field_2277 = this.field_2278 = 0.0;
				}

				this.world.addParticle("largesmoke", this.x, this.y + 0.8, this.z, 0.0, 0.0, 0.0);
			}
		}
	}
}
