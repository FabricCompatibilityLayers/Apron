package io.github.betterthanupdates.modloader.mixin.block.entity.nostation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeManager;
import io.github.betterthanupdates.reforged.recipe.ReforgedSmeltingRecipeManager;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity implements Inventory {
	@Shadow
	protected abstract boolean method_1283();

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
	public void method_1282() {
		if (this.method_1283()) {
			ItemStack itemstack = ((ReforgedSmeltingRecipeManager) SmeltingRecipeManager.getInstance()).getSmeltingResult(this.inventory[0]);

			if (this.inventory[2] == null) {
				this.inventory[2] = itemstack.copy();
			} else if (this.inventory[2].isItemEqual(itemstack)) {
				this.inventory[2].count += itemstack.count;
			}

			if (this.inventory[0].getItem().hasCraftingReturnItem()) {
				this.inventory[0] = new ItemStack(this.inventory[0].getItem().getCraftingReturnItem());
			} else {
				--this.inventory[0].count;
			}

			if (this.inventory[0].count <= 0) {
				this.inventory[0] = null;
			}
		}
	}
}
