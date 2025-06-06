package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(FurnaceBlockEntity.class)
public class FurnaceBlockEntityMixin {
	@Shadow
	private ItemStack[] inventory;

	@Definition(id = "count", field = "Lnet/minecraft/item/ItemStack;count:I")
	@Expression("?.count = ?.count - 1")
	@WrapOperation(method = {"tick", "craftRecipe"}, at = @At("MIXINEXTRAS:EXPRESSION"))
	private void modloader$TakeReturningItemIntoAccount(ItemStack instance, int value, Operation<Void> original) {
		if (instance.getItem().hasCraftingReturnItem()) {
			this.inventory[1] = new ItemStack(instance.getItem().getCraftingReturnItem());
		} else {
			original.call(instance, value);
		}
	}

	@Definition(id = "count", field = "Lnet/minecraft/item/ItemStack;count:I")
	@Expression("?.count = ?.count + 1")
	@WrapOperation(method = "craftRecipe", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void modloader$FixItemCount(ItemStack instance, int value, Operation<Void> original,
										@Local ItemStack localiz) {
		original.call(instance, instance.count + localiz.count);
	}
}
