package io.github.betterthanupdates.modloader.mixin.nostation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.entity.BlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;

import io.github.betterthanupdates.reforged.recipe.ReforgedSmeltingRecipeRegistry;

@Mixin(FurnaceBlockEntity.class)
public abstract class VanillaFurnaceBlockEntityMixin extends BlockEntity implements Inventory {
	@Shadow
	protected abstract boolean canAcceptRecipeOutput();

	@Shadow
	private ItemStack[] inventory;

	/**
	 * @author Risugami
	 * @author Kleadron
	 * @reason
	 * TODO(halotroop2288): rewrite as an {@link Inject} Mixin
	 */
	@Environment(EnvType.CLIENT)
	@Overwrite
	public void craftRecipe() {
		if (this.canAcceptRecipeOutput()) {
			ItemStack itemstack = ((ReforgedSmeltingRecipeRegistry) SmeltingRecipeRegistry.getInstance()).getSmeltingResult(this.inventory[0]);

			if (this.inventory[2] == null) {
				this.inventory[2] = itemstack.copy();
			} else if (this.inventory[2].isDamageAndIDIdentical(itemstack)) {
				this.inventory[2].count += itemstack.count;
			}

			if (this.inventory[0].getItem().hasContainerItemType()) {
				this.inventory[0] = new ItemStack(this.inventory[0].getItem().getContainerItemType());
			} else {
				--this.inventory[0].count;
			}

			if (this.inventory[0].count <= 0) {
				this.inventory[0] = null;
			}
		}
	}
}
