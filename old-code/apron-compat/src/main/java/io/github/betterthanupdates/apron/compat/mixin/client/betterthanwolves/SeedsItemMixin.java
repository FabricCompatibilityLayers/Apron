package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.item.SeedsItem;
import net.minecraft.world.World;

@Mixin(SeedsItem.class)
public class SeedsItemMixin {
	@Shadow
	private int cropBlockId;

	@ModifyExpressionValue(method = "useOnBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Block;id:I", ordinal = 0))
	private int btw$useOnBlock(int value, @Local World world, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j,
							   @Local(ordinal = 2) int k, @Local(ordinal = 4) int i1) {
		if (FCUtilsMisc.CanPlantGrowOnBlock(world, i, j, k, Block.BLOCKS[this.cropBlockId])) {
			return i1;
		}

		return value;
	}
}
