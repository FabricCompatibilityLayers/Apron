package io.github.betterthanupdates.shockahpi.mixin.client;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shockahpi.SAPI;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.block.ChestBlockEntity;
import net.minecraft.entity.block.MobSpawnerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.feature.DungeonFeature;
import net.minecraft.world.feature.Feature;

@Mixin(DungeonFeature.class)
public abstract class DungeonFeatureMixin extends Feature {
	/**
	 * @author SAPI
	 * @reason
	 */
	@Overwrite
	public boolean generate(World paramfd, Random paramRandom, int paramInt1, int paramInt2, int paramInt3) {
		int i = 3;
		int j = paramRandom.nextInt(2) + 2;
		int k = paramRandom.nextInt(2) + 2;
		int m = 0;

		for(int n = paramInt1 - j - 1; n <= paramInt1 + j + 1; ++n) {
			for(int i1 = paramInt2 - 1; i1 <= paramInt2 + i + 1; ++i1) {
				for(int i2 = paramInt3 - k - 1; i2 <= paramInt3 + k + 1; ++i2) {
					Material localln = paramfd.getMaterial(n, i1, i2);
					if (i1 == paramInt2 - 1 && !localln.isSolid()) {
						return false;
					}

					if (i1 == paramInt2 + i + 1 && !localln.isSolid()) {
						return false;
					}

					if ((n == paramInt1 - j - 1 || n == paramInt1 + j + 1 || i2 == paramInt3 - k - 1 || i2 == paramInt3 + k + 1)
							&& i1 == paramInt2
							&& paramfd.isAir(n, i1, i2)
							&& paramfd.isAir(n, i1 + 1, i2)) {
						++m;
					}
				}
			}
		}

		if (m >= 1 && m <= 5) {
			for(int n = paramInt1 - j - 1; n <= paramInt1 + j + 1; ++n) {
				for(int i1 = paramInt2 + i; i1 >= paramInt2 - 1; --i1) {
					for(int i2 = paramInt3 - k - 1; i2 <= paramInt3 + k + 1; ++i2) {
						if (n != paramInt1 - j - 1
								&& i1 != paramInt2 - 1
								&& i2 != paramInt3 - k - 1
								&& n != paramInt1 + j + 1
								&& i1 != paramInt2 + i + 1
								&& i2 != paramInt3 + k + 1) {
							paramfd.setBlock(n, i1, i2, 0);
						} else if (i1 >= 0 && !paramfd.getMaterial(n, i1 - 1, i2).isSolid()) {
							paramfd.setBlock(n, i1, i2, 0);
						} else if (paramfd.getMaterial(n, i1, i2).isSolid()) {
							if (i1 == paramInt2 - 1 && paramRandom.nextInt(4) != 0) {
								paramfd.setBlock(n, i1, i2, Block.MOSSY_COBBLESTONE.id);
							} else {
								paramfd.setBlock(n, i1, i2, Block.COBBLESTONE.id);
							}
						}
					}
				}
			}

			for(int n = 0; n < 2; ++n) {
				for(int i1 = 0; i1 < 3; ++i1) {
					int i2 = paramInt1 + paramRandom.nextInt(j * 2 + 1) - j;
					int i4 = paramInt3 + paramRandom.nextInt(k * 2 + 1) - k;
					if (paramfd.isAir(i2, paramInt2, i4)) {
						int i5 = 0;
						if (paramfd.getMaterial(i2 - 1, paramInt2, i4).isSolid()) {
							++i5;
						}

						if (paramfd.getMaterial(i2 + 1, paramInt2, i4).isSolid()) {
							++i5;
						}

						if (paramfd.getMaterial(i2, paramInt2, i4 - 1).isSolid()) {
							++i5;
						}

						if (paramfd.getMaterial(i2, paramInt2, i4 + 1).isSolid()) {
							++i5;
						}

						if (i5 == 1) {
							paramfd.setBlock(i2, paramInt2, i4, Block.CHEST.id);
							ChestBlockEntity localjs = (ChestBlockEntity)paramfd.getBlockEntity(i2, paramInt2, i4);

							for(int i6 = 0; i6 < 8; ++i6) {
								ItemStack localiz = this.getRandomChestItem(paramRandom);
								if (localiz != null) {
									localjs.setInventoryItem(paramRandom.nextInt(localjs.getInventorySize()), localiz);
								}
							}

							for(int i6 = 0; i6 < Math.min(19, SAPI.dungeonGetAmountOfGuaranteed()); ++i6) {
								ItemStack stack = SAPI.dungeonGetGuaranteed(i6).getStack();
								if (stack != null) {
									localjs.setInventoryItem(paramRandom.nextInt(localjs.getInventorySize()), stack);
								}
							}
							break;
						}
					}
				}
			}

			paramfd.setBlock(paramInt1, paramInt2, paramInt3, Block.MOB_SPAWNER.id);
			MobSpawnerEntity localcy = (MobSpawnerEntity)paramfd.getBlockEntity(paramInt1, paramInt2, paramInt3);
			localcy.setEntityId(this.getRandomEntity(paramRandom));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author SAPI
	 * @reason
	 */
	@Overwrite
	private ItemStack getRandomChestItem(Random paramRandom) {
		return SAPI.dungeonGetRandomItem();
	}

	/**
	 * @author SAPI
	 * @reason
	 */
	@Overwrite
	private String getRandomEntity(Random paramRandom) {
		return SAPI.dungeonGetRandomMob();
	}
}
