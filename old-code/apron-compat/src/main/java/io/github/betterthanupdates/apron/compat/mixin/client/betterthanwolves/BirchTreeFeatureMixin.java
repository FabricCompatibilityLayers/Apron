package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.BirchTreeFeature;

@Mixin(BirchTreeFeature.class)
public class BirchTreeFeatureMixin {
	/**
	 * @author BTW
	 * @reason difficult to convert
	 */
	@Overwrite
	public boolean generate(World world, Random random, int i, int j, int k) {
		int l = random.nextInt(3) + 5;
		boolean flag = true;
		if (j >= 1 && j + l + 1 <= 128) {
			for(int i1 = j; i1 <= j + 1 + l; ++i1) {
				byte byte0 = 1;
				if (i1 == j) {
					byte0 = 0;
				}

				if (i1 >= j + 1 + l - 2) {
					byte0 = 2;
				}

				for(int i2 = i - byte0; i2 <= i + byte0 && flag; ++i2) {
					for(int l2 = k - byte0; l2 <= k + byte0 && flag; ++l2) {
						if (i1 >= 0 && i1 < 128) {
							int j3 = world.getBlockId(i2, i1, l2);
							if (!world.method_234(i2, i1, l2) && j3 != Block.LEAVES.id) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			} else {
				int j1 = world.getBlockId(i, j - 1, k);
				if ((j1 == Block.GRASS_BLOCK.id || j1 == Block.DIRT.id || FCUtilsMisc.CanPlantGrowOnBlock(world, i, j - 1, k, Block.SAPLING)) && j < 128 - l - 1) {
					if (j1 == Block.GRASS_BLOCK.id) {
						world.method_200(i, j - 1, k, Block.DIRT.id);
					}

					for(int k1 = j - 3 + l; k1 <= j + l; ++k1) {
						int j2 = k1 - (j + l);
						int i3 = 1 - j2 / 2;

						for(int k3 = i - i3; k3 <= i + i3; ++k3) {
							int l3 = k3 - i;

							for(int i4 = k - i3; i4 <= k + i3; ++i4) {
								int j4 = i4 - k;
								if ((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0)
										&& !Block.BLOCKS_OPAQUE[world.getBlockId(k3, k1, i4)]) {
									world.method_154(k3, k1, i4, Block.LEAVES.id, 2);
								}
							}
						}
					}

					for(int l1 = 0; l1 < l; ++l1) {
						int k2 = world.getBlockId(i, j + l1, k);
						if (world.method_234(i, j + l1, k) || k2 == Block.LEAVES.id) {
							world.method_154(i, j + l1, k, Block.LOG.id, 2);
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
