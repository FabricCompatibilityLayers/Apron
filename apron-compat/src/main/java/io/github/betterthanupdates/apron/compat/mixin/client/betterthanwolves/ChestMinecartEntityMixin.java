package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.ChestMinecartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.mod_FCBetterThanWolves;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(ChestMinecartEntity.class)
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
	public int type;

	@Shadow
	public double pushX;

	@Shadow
	public double pushZ;

	@Shadow
	private boolean field_2280;

	@Shadow
	public int fuel;

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

		if (this.world.isClient && this.field_2282 > 0) {
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
				this.setPosition(d, d1, d3);
				this.setRotation(this.yaw, this.pitch);
			} else {
				this.setPosition(this.x, this.y, this.z);
				this.setRotation(this.yaw, this.pitch);
			}
		} else {
			this.prevX = this.x;
			this.prevY = this.y;
			this.prevZ = this.z;
			this.yVelocity -= 0.04F;
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
			if (RailBlock.isRail(l)) {
				Vec3d vec3d = this.method_1813(this.x, this.y, this.z);
				int i1 = this.world.getBlockMeta(i, j, k);
				this.y = (double)j;
				boolean flag1 = false;
				boolean flag2 = false;
				if (l == Block.GOLDEN_RAIL.id) {
					flag1 = (i1 & 8) != 0;
					flag2 = !flag1;
				}

				if (((RailBlock)Block.BY_ID[l]).method_1108()) {
					i1 &= 7;
				}

				if (i1 >= 2 && i1 <= 5) {
					this.y = (double)(j + 1);
				}

				if (i1 == 2) {
					this.xVelocity -= d5;
				}

				if (i1 == 3) {
					this.xVelocity += d5;
				}

				if (i1 == 4) {
					this.zVelocity += d5;
				}

				if (i1 == 5) {
					this.zVelocity -= d5;
				}

				int[][] ai = field_2281[i1];
				double d9 = (double)(ai[1][0] - ai[0][0]);
				double d10 = (double)(ai[1][2] - ai[0][2]);
				double d11 = Math.sqrt(d9 * d9 + d10 * d10);
				double d12 = this.xVelocity * d9 + this.zVelocity * d10;
				if (d12 < 0.0) {
					d9 = -d9;
					d10 = -d10;
				}

				double d13 = Math.sqrt(this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity);
				this.xVelocity = d13 * d9 / d11;
				this.zVelocity = d13 * d10 / d11;
				if (flag2) {
					double d16 = Math.sqrt(this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity);
					if (d16 < 0.03) {
						this.xVelocity *= 0.0;
						this.yVelocity *= 0.0;
						this.zVelocity *= 0.0;
					} else {
						this.xVelocity *= 0.5;
						this.yVelocity *= 0.0;
						this.zVelocity *= 0.5;
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
				this.setPosition(this.x, this.y + (double)this.standingEyeHeight, this.z);
				double d23 = this.xVelocity;
				double d25 = this.zVelocity;
				if (this.passenger != null) {
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
					this.setPosition(this.x, this.y + (double)ai[0][1], this.z);
				} else if (ai[1][1] != 0 && MathHelper.floor(this.x) - i == ai[1][0] && MathHelper.floor(this.z) - k == ai[1][2]) {
					this.setPosition(this.x, this.y + (double)ai[1][1], this.z);
				}

				if (this.passenger != null) {
					this.xVelocity *= 0.997F;
					this.yVelocity *= 0.0;
					this.zVelocity *= 0.997F;
				} else {
					if (this.type == 2) {
						double d27 = (double)MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
						if (d27 > 0.01) {
							flag = true;
							this.pushX /= d27;
							this.pushZ /= d27;
							double d29 = 0.04;
							this.xVelocity *= 0.8F;
							this.yVelocity *= 0.0;
							this.zVelocity *= 0.8F;
							this.xVelocity += this.pushX * d29;
							this.zVelocity += this.pushZ * d29;
						} else {
							this.xVelocity *= 0.9F;
							this.yVelocity *= 0.0;
							this.zVelocity *= 0.9F;
						}
					}

					// TODO: PATCH STARTS HERE
					if (mod_FCBetterThanWolves.fcDisableMinecartChanges) {
						this.xVelocity *= 0.96F;
						this.yVelocity *= 0.0;
						this.zVelocity *= 0.96F;
					} else {
						this.xVelocity *= 0.985;
						this.yVelocity *= 0.0;
						this.zVelocity *= 0.985;
					}
					// PATCH ENDS HERE
				}

				Vec3d vec3d1 = this.method_1813(this.x, this.y, this.z);
				if (vec3d1 != null && vec3d != null) {
					double d28 = (vec3d.y - vec3d1.y) * 0.05;
					double d14 = Math.sqrt(this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity);
					if (d14 > 0.0) {
						this.xVelocity = this.xVelocity / d14 * (d14 + d28);
						this.zVelocity = this.zVelocity / d14 * (d14 + d28);
					}

					this.setPosition(this.x, vec3d1.y, this.z);
				}

				int k1 = MathHelper.floor(this.x);
				int l1 = MathHelper.floor(this.z);
				if (k1 != i || l1 != k) {
					double d15 = Math.sqrt(this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity);
					this.xVelocity = d15 * (double)(k1 - i);
					this.zVelocity = d15 * (double)(l1 - k);
				}

				if (this.type == 2) {
					double d30 = (double)MathHelper.sqrt(this.pushX * this.pushX + this.pushZ * this.pushZ);
					if (d30 > 0.01 && this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity > 0.001) {
						this.pushX /= d30;
						this.pushZ /= d30;
						if (this.pushX * this.xVelocity + this.pushZ * this.zVelocity < 0.0) {
							this.pushX = 0.0;
							this.pushZ = 0.0;
						} else {
							this.pushX = this.xVelocity;
							this.pushZ = this.zVelocity;
						}
					}
				}

				if (flag1) {
					double d31 = Math.sqrt(this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity);
					if (d31 > 0.01) {
						double d32 = 0.06;
						this.xVelocity += this.xVelocity / d31 * d32;
						this.zVelocity += this.zVelocity / d31 * d32;
					} else if (i1 == 1) {
						if (this.world.canSuffocate(i - 1, j, k)) {
							this.xVelocity = 0.02;
						} else if (this.world.canSuffocate(i + 1, j, k)) {
							this.xVelocity = -0.02;
						}
					} else if (i1 == 0) {
						if (this.world.canSuffocate(i, j, k - 1)) {
							this.zVelocity = 0.02;
						} else if (this.world.canSuffocate(i, j, k + 1)) {
							this.zVelocity = -0.02;
						}
					}
				}
			} else {
				if (this.xVelocity < -d2) {
					this.xVelocity = -d2;
				}

				if (this.xVelocity > d2) {
					this.xVelocity = d2;
				}

				if (this.zVelocity < -d2) {
					this.zVelocity = -d2;
				}

				if (this.zVelocity > d2) {
					this.zVelocity = d2;
				}

				if (this.onGround) {
					this.xVelocity *= 0.5;
					this.yVelocity *= 0.5;
					this.zVelocity *= 0.5;
				}

				this.move(this.xVelocity, this.yVelocity, this.zVelocity);
				if (!this.onGround) {
					this.xVelocity *= 0.95F;
					this.yVelocity *= 0.95F;
					this.zVelocity *= 0.95F;
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

			this.setRotation(this.yaw, this.pitch);
			List list = this.world.getEntities(this, this.boundingBox.expand(0.2F, 0.0, 0.2F));
			if (list != null && list.size() > 0) {
				for(int j1 = 0; j1 < list.size(); ++j1) {
					Entity entity = (Entity)list.get(j1);
					if (entity != this.passenger && entity.method_1380() && entity instanceof ChestMinecartEntity) {
						entity.method_1353(this);
					}
				}
			}

			if (this.passenger != null && this.passenger.removed) {
				this.passenger = null;
			}

			if (flag && this.rand.nextInt(4) == 0) {
				--this.fuel;
				if (this.fuel < 0) {
					this.pushX = this.pushZ = 0.0;
				}

				this.world.addParticle("largesmoke", this.x, this.y + 0.8, this.z, 0.0, 0.0, 0.0);
			}
		}
	}
}
