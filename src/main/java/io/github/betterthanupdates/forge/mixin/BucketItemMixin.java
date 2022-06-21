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
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public ItemStack use(ItemStack stack, World world, PlayerEntity player) {
		float f = 1.0F;
		float f1 = player.prevPitch + (player.pitch - player.prevPitch) * f;
		float f2 = player.prevYaw + (player.yaw - player.prevYaw) * f;
		double d = player.prevX + (player.x - player.prevX) * (double) f;
		double d1 = player.prevY + (player.y - player.prevY) * (double) f + 1.62 - (double) player.standingEyeHeight;
		double d2 = player.prevZ + (player.z - player.prevZ) * (double) f;
		Vec3d vec3d = Vec3d.from(d, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f9 = f3 * f5;
		double d3 = 5.0;
		Vec3d vec3d1 = vec3d.translate((double) f7 * d3, (double) f6 * d3, (double) f9 * d3);
		HitResult hitResult = world.method_161(vec3d, vec3d1, this.liquidBlockId == 0);

		if (hitResult != null) {
			if (hitResult.type == HitType.field_789) {
				int x = hitResult.x;
				int y = hitResult.y;
				int z = hitResult.z;

				if (!world.method_171(player, x, y, z)) {
					return stack;
				}

				if (this.liquidBlockId == 0) {
					ItemStack customBucket = MinecraftForge.fillCustomBucket(world, x, y, z);

					if (customBucket != null) {
						return customBucket;
					}

					if (world.getMaterial(x, y, z) == Material.WATER && world.getBlockMeta(x, y, z) == 0) {
						world.setBlock(x, y, z, 0);
						return new ItemStack(Item.WATER_BUCKET);
					}

					if (world.getMaterial(x, y, z) == Material.LAVA && world.getBlockMeta(x, y, z) == 0) {
						world.setBlock(x, y, z, 0);
						return new ItemStack(Item.LAVA_BUCKET);
					}
				} else {
					if (this.liquidBlockId < 0) {
						return new ItemStack(Item.BUCKET);
					}

					if (hitResult.field_1987 == 0) {
						--y;
					}

					if (hitResult.field_1987 == 1) {
						++y;
					}

					if (hitResult.field_1987 == 2) {
						--z;
					}

					if (hitResult.field_1987 == 3) {
						++z;
					}

					if (hitResult.field_1987 == 4) {
						--x;
					}

					if (hitResult.field_1987 == 5) {
						++x;
					}

					if (world.isAir(x, y, z) || !world.getMaterial(x, y, z).isSolid()) {
						if (world.dimension.evaporatesWater && this.liquidBlockId == Block.FLOWING_WATER.id) {
							world.playSound(d + 0.5, d1 + 0.5, d2 + 0.5, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

							for (int l = 0; l < 8; ++l) {
								world.addParticle("largesmoke", (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0, 0.0, 0.0);
							}
						} else {
							world.placeBlockWithMetaData(x, y, z, this.liquidBlockId, 0);
						}

						return new ItemStack(Item.BUCKET);
					}
				}
			} else if (this.liquidBlockId == 0 && hitResult.field_1989 instanceof CowEntity) {
				return new ItemStack(Item.MILK);
			}
		}

		return stack;
	}
}
