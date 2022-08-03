package io.github.betterthanupdates.reforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.BlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity implements Inventory {
//	@Shadow
//	private ItemStack[] inventory;
//
//	@Redirect(method = "canAcceptRecipeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/SmeltingRecipeRegistry;getResult(I)Lnet/minecraft/item/ItemStack;"))
//	private ItemStack reforged$canAcceptRecipeOutput(SmeltingRecipeRegistry instance, int i) {
//		return ((ReforgedSmeltingRecipeRegistry) instance).getSmeltingResult(this.inventory[0]);
//	}
}
