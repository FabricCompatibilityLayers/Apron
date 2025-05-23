package io.github.betterthanupdates.reforged.mixin.block.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeManager;
import io.github.betterthanupdates.reforged.recipe.ReforgedSmeltingRecipeManager;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity implements Inventory {
	@Shadow
	private ItemStack[] inventory;

	@Redirect(method = "method_1283", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/SmeltingRecipeManager;craft(I)Lnet/minecraft/item/ItemStack;"))
	private ItemStack reforged$canAcceptRecipeOutput(SmeltingRecipeManager instance, int i) {
		return ((ReforgedSmeltingRecipeManager) instance).getSmeltingResult(this.inventory[0]);
	}
}
