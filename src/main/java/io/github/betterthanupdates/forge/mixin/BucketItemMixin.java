package io.github.betterthanupdates.forge.mixin;

import forge.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.animal.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item {
	@Shadow
	private int liquidBlockId;

	public BucketItemMixin(int i) {
		super(i);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public ItemStack use(ItemStack itemstack, World world, PlayerEntity entityplayer) {
		float f = 1.0F;
		float f1 = entityplayer.prevPitch + (entityplayer.pitch - entityplayer.prevPitch) * f;
		float f2 = entityplayer.prevYaw + (entityplayer.yaw - entityplayer.prevYaw) * f;
		double d = entityplayer.prevX + (entityplayer.x - entityplayer.prevX) * (double) f;
		double d1 = entityplayer.prevY + (entityplayer.y - entityplayer.prevY) * (double) f + 1.62 - (double) entityplayer.standingEyeHeight;
		double d2 = entityplayer.prevZ + (entityplayer.z - entityplayer.prevZ) * (double) f;
		Vec3d vec3d = Vec3d.from(d, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f9 = f3 * f5;
		double d3 = 5.0;
		Vec3d vec3d1 = vec3d.translate((double) f7 * d3, (double) f6 * d3, (double) f9 * d3);
		HitResult movingobjectposition = world.method_161(vec3d, vec3d1, this.liquidBlockId == 0);
		if (movingobjectposition != null) {
			if (movingobjectposition.type == HitType.field_789) {
				int i = movingobjectposition.x;
				int j = movingobjectposition.y;
				int k = movingobjectposition.z;
				if (!world.method_171(entityplayer, i, j, k)) {
					return itemstack;
				}

				if (this.liquidBlockId == 0) {
					ItemStack customBucket = MinecraftForge.fillCustomBucket(world, i, j, k);
					if (customBucket != null) {
						return customBucket;
					}

					if (world.getMaterial(i, j, k) == Material.WATER && world.getBlockMeta(i, j, k) == 0) {
						world.setBlock(i, j, k, 0);
						return new ItemStack(Item.WATER_BUCKET);
					}

					if (world.getMaterial(i, j, k) == Material.LAVA && world.getBlockMeta(i, j, k) == 0) {
						world.setBlock(i, j, k, 0);
						return new ItemStack(Item.LAVA_BUCKET);
					}
				} else {
					if (this.liquidBlockId < 0) {
						return new ItemStack(Item.BUCKET);
					}

					if (movingobjectposition.field_1987 == 0) {
						--j;
					}

					if (movingobjectposition.field_1987 == 1) {
						++j;
					}

					if (movingobjectposition.field_1987 == 2) {
						--k;
					}

					if (movingobjectposition.field_1987 == 3) {
						++k;
					}

					if (movingobjectposition.field_1987 == 4) {
						--i;
					}

					if (movingobjectposition.field_1987 == 5) {
						++i;
					}

					if (world.isAir(i, j, k) || !world.getMaterial(i, j, k).isSolid()) {
						if (world.dimension.evaporatesWater && this.liquidBlockId == Block.FLOWING_WATER.id) {
							world.playSound(d + 0.5, d1 + 0.5, d2 + 0.5, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

							for (int l = 0; l < 8; ++l) {
								world.addParticle("largesmoke", (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
							}
						} else {
							world.placeBlockWithMetaData(i, j, k, this.liquidBlockId, 0);
						}

						return new ItemStack(Item.BUCKET);
					}
				}
			} else if (this.liquidBlockId == 0 && movingobjectposition.field_1989 instanceof CowEntity) {
				return new ItemStack(Item.MILK);
			}

		}
		return itemstack;
	}
}
