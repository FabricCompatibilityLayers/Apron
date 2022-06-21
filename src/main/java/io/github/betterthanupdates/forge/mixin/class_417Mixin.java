package io.github.betterthanupdates.forge.mixin;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.class_417;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Mixin(class_417.class)
public class class_417Mixin {
	@Shadow
	public int field_1677;

	@Shadow
	public int field_1678;

	@Shadow
	public int field_1679;

	@Shadow
	public int field_1674;

	@Shadow
	public int field_1675;

	@Shadow
	public int field_1676;

	@Shadow
	@Final
	public LightType field_1673;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void method_1402(World world) {
		int i = this.field_1677 - this.field_1674 + 1;
		int j = this.field_1678 - this.field_1675 + 1;
		int k = this.field_1679 - this.field_1676 + 1;
		int l = i * j * k;

		if (l > 32768) {
			Logger.get("Minecraft").debug("Light too large, skipping!");
		} else {
			int i1 = 0;
			int j1 = 0;
			boolean flag = false;
			boolean flag1 = false;

			for (int k1 = this.field_1674; k1 <= this.field_1677; ++k1) {
				for (int l1 = this.field_1676; l1 <= this.field_1679; ++l1) {
					int i2 = k1 >> 4;
					int j2 = l1 >> 4;
					boolean flag2;

					if (flag && i2 == i1 && j2 == j1) {
						flag2 = flag1;
					} else {
						flag2 = world.method_153(k1, 0, l1, 1);

						if (flag2) {
							Chunk chunk = world.getChunkFromCache(k1 >> 4, l1 >> 4);

							if (chunk.method_886()) {
								flag2 = false;
							}
						}

						flag1 = flag2;
						i1 = i2;
						j1 = j2;
					}

					if (flag2) {
						if (this.field_1675 < 0) {
							this.field_1675 = 0;
						}

						if (this.field_1678 >= 128) {
							this.field_1678 = 127;
						}

						for (int k2 = this.field_1675; k2 <= this.field_1678; ++k2) {
							int l2 = world.method_164(this.field_1673, k1, k2, l1);
							int i3 = 0;
							int j3 = world.getBlockId(k1, k2, l1);
							int k3 = Block.LIGHT_OPACITY[j3];

							if (k3 == 0) {
								k3 = 1;
							}

							int l3 = 0;

							if (this.field_1673 == LightType.field_2757) {
								if (world.isAboveGround(k1, k2, l1)) {
									l3 = 15;
								}
							} else if (this.field_1673 == LightType.field_2758) {
								l3 = j3 == 0 ? 0 : ((ForgeBlock) Block.BY_ID[j3]).getLightValue(world, k1, k2, l1);
							}

							if (k3 >= 15 && l3 == 0) {
								i3 = 0;
							} else {
								int i4 = world.method_164(this.field_1673, k1 - 1, k2, l1);
								int k4 = world.method_164(this.field_1673, k1 + 1, k2, l1);
								int l4 = world.method_164(this.field_1673, k1, k2 - 1, l1);
								int i5 = world.method_164(this.field_1673, k1, k2 + 1, l1);
								int j5 = world.method_164(this.field_1673, k1, k2, l1 - 1);
								int k5 = world.method_164(this.field_1673, k1, k2, l1 + 1);
								i3 = i4;

								if (k4 > i4) {
									i3 = k4;
								}

								if (l4 > i3) {
									i3 = l4;
								}

								if (i5 > i3) {
									i3 = i5;
								}

								if (j5 > i3) {
									i3 = j5;
								}

								if (k5 > i3) {
									i3 = k5;
								}

								i3 -= k3;

								if (i3 < 0) {
									i3 = 0;
								}

								if (l3 > i3) {
									i3 = l3;
								}
							}

							if (l2 != i3) {
								world.method_205(this.field_1673, k1, k2, l1, i3);
								int j4 = i3 - 1;

								if (j4 < 0) {
									j4 = 0;
								}

								world.method_165(this.field_1673, k1 - 1, k2, l1, j4);
								world.method_165(this.field_1673, k1, k2 - 1, l1, j4);
								world.method_165(this.field_1673, k1, k2, l1 - 1, j4);

								if (k1 + 1 >= this.field_1677) {
									world.method_165(this.field_1673, k1 + 1, k2, l1, j4);
								}

								if (k2 + 1 >= this.field_1678) {
									world.method_165(this.field_1673, k1, k2 + 1, l1, j4);
								}

								if (l1 + 1 >= this.field_1679) {
									world.method_165(this.field_1673, k1, k2, l1 + 1, j4);
								}
							}
						}
					}
				}
			}
		}
	}
}
