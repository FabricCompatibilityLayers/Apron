package io.github.betterthanupdates.forge.mixin;

import forge.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.stat.Stats;

import io.github.betterthanupdates.forge.ReforgedShearsItem;

@Mixin(ShearsItem.class)
public class ShearsItemMixin extends Item implements ReforgedShearsItem {
	public ShearsItemMixin(int i) {
		super(i);
	}

	/**
	 * @author Reforged author
	 * @reason
	 */
	@Overwrite
	public boolean postMine(ItemStack itemstack, int i, int j, int k, int l, LivingEntity livingEntity) {
		if (i == Block.COBWEB.id || i == Block.LEAVES.id || Block.BY_ID[i] instanceof IShearable) {
			itemstack.applyDamage(1, livingEntity);
		}

		return super.postMine(itemstack, i, j, k, l, livingEntity);
	}

	@Override
	public void interactWithEntity(ItemStack itemstack, LivingEntity entity) {
		if (!entity.world.isClient) {
			if (entity instanceof IShearable) {
				IShearable target = (IShearable) entity;

				if (target.isShearable(itemstack, entity.world, (int) entity.x, (int) entity.y, (int) entity.z)) {
					for (ItemStack stack : target.onSheared(itemstack, entity.world, (int) entity.x, (int) entity.y, (int) entity.z)) {
						ItemEntity ent = entity.dropItem(stack, 1.0F);
						ent.yVelocity += (double) (entity.rand.nextFloat() * 0.05F);
						ent.xVelocity += (double) ((entity.rand.nextFloat() - entity.rand.nextFloat()) * 0.1F);
						ent.zVelocity += (double) ((entity.rand.nextFloat() - entity.rand.nextFloat()) * 0.1F);
					}

					itemstack.applyDamage(1, entity);
				}
			}
		}
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, PlayerEntity player) {
		if (!player.world.isClient) {
			int id = player.world.getBlockId(X, Y, Z);

			if (Block.BY_ID[id] != null && Block.BY_ID[id] instanceof IShearable) {
				IShearable target = (IShearable) Block.BY_ID[id];

				if (target.isShearable(itemstack, player.world, X, Y, Z)) {
					for (ItemStack stack : target.onSheared(itemstack, player.world, X, Y, Z)) {
						float f = 0.7F;
						double d = (double) (player.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
						double d1 = (double) (player.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
						double d2 = (double) (player.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
						ItemEntity entityitem = new ItemEntity(player.world, (double) X + d, (double) Y + d1, (double) Z + d2, stack);
						entityitem.pickupDelay = 10;
						player.world.spawnEntity(entityitem);
					}

					itemstack.applyDamage(1, player);
					player.increaseStat(Stats.mineBlock[id], 1);
				}
			}
		}

		return false;
	}
}
