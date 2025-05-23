package io.github.betterthanupdates.reforged.mixin.item;

import forge.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.stat.Stats;

import io.github.betterthanupdates.reforged.item.ReforgedItem;

@Mixin(ShearsItem.class)
public class ShearsItemMixin extends Item implements ReforgedItem {
	public ShearsItemMixin(int i) {
		super(i);
	}

	/**
	 * @author Kleadron
	 * @reason implement Reforged function
	 */
	@Inject(method = "postMine", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;postMine(Lnet/minecraft/item/ItemStack;IIIILnet/minecraft/entity/LivingEntity;)Z"))
	private void reforged$postMine(ItemStack itemstack, int i, int j, int k, int l, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
		if (i != Block.COBWEB.id && i != Block.LEAVES.id // Don't apply damage if it has already been applied before.
				&& Block.BLOCKS[i] instanceof IShearable) {
			itemstack.damage(1, livingEntity);
		}
	}

	@Override
	public void useOnEntity(ItemStack itemstack, LivingEntity entity) {
		if (!entity.world.isRemote) {
			if (entity instanceof IShearable) {
				IShearable target = (IShearable) entity;

				if (target.isShearable(itemstack, entity.world, (int) entity.x, (int) entity.y, (int) entity.z)) {
					for (ItemStack stack : target.onSheared(itemstack, entity.world, (int) entity.x, (int) entity.y, (int) entity.z)) {
						ItemEntity ent = entity.method_1327(stack, 1.0F);
						ent.velocityY += (double) (entity.random.nextFloat() * 0.05F);
						ent.velocityX += (double) ((entity.random.nextFloat() - entity.random.nextFloat()) * 0.1F);
						ent.velocityZ += (double) ((entity.random.nextFloat() - entity.random.nextFloat()) * 0.1F);
					}

					itemstack.damage(1, entity);
				}
			}
		}
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, PlayerEntity player) {
		if (!player.world.isRemote) {
			int id = player.world.getBlockId(x, y, z);

			if (Block.BLOCKS[id] != null && Block.BLOCKS[id] instanceof IShearable) {
				IShearable target = (IShearable) Block.BLOCKS[id];

				if (target.isShearable(itemstack, player.world, x, y, z)) {
					for (ItemStack stack : target.onSheared(itemstack, player.world, x, y, z)) {
						float f = 0.7F;
						double d = (double) (player.random.nextFloat() * f) + (double) (1.0F - f) * 0.5;
						double d1 = (double) (player.random.nextFloat() * f) + (double) (1.0F - f) * 0.5;
						double d2 = (double) (player.random.nextFloat() * f) + (double) (1.0F - f) * 0.5;
						ItemEntity entityitem = new ItemEntity(player.world, (double) x + d, (double) y + d1, (double) z + d2, stack);
						entityitem.pickupDelay = 10;
						player.world.method_210(entityitem);
					}

					itemstack.damage(1, player);
					player.increaseStat(Stats.MINE_BLOCK[id], 1);
				}
			}
		}

		return false;
	}
}
