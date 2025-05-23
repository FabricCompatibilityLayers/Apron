package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mod_FCBetterThanWolves;
import net.minecraft.world.World;

@Mixin(HoeItem.class)
public class HoeItemMixin {
	@Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIII)Z"))
	private void btw$useOnBlock(ItemStack itemstack, PlayerEntity entityplayer, World world, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 4) int i1) {
		if (i1 == Block.GRASS_BLOCK.id) {
			int iPercentageChanceOfHempSeedDrop = 0;
			int iHoeID = itemstack.itemId;
			if (iHoeID == Item.WOODEN_HOE.id) {
				iPercentageChanceOfHempSeedDrop = 1;
			} else if (iHoeID == Item.STONE_HOE.id) {
				iPercentageChanceOfHempSeedDrop = 2;
			} else if (iHoeID == Item.IRON_HOE.id) {
				iPercentageChanceOfHempSeedDrop = 4;
			} else if (iHoeID == Item.DIAMOND_HOE.id) {
				iPercentageChanceOfHempSeedDrop = 8;
			} else if (iHoeID == Item.GOLDEN_HOE.id) {
				iPercentageChanceOfHempSeedDrop = 16;
			}

			if (world.field_214.nextInt(100) < iPercentageChanceOfHempSeedDrop) {
				float f = 0.7F;
				float f1 = world.field_214.nextFloat() * f + (1.0F - f) * 0.5F;
				float f2 = 1.2F;
				float f3 = world.field_214.nextFloat() * f + (1.0F - f) * 0.5F;
				ItemEntity entityitem = new ItemEntity(
						world, (double)((float)i + f1), (double)((float)j + f2), (double)((float)k + f3), new ItemStack(mod_FCBetterThanWolves.fcHempSeeds)
				);
				entityitem.pickupDelay = 10;
				world.method_210(entityitem);
			}
		}
	}
}
