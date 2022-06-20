package io.github.betterthanupdates.modloader.mixin;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.BlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity implements Inventory {
	@Shadow
	public int burnTime;

	@Shadow
	protected abstract boolean canAcceptRecipeOutput();

	@Shadow
	public int fuelTime;

	@Shadow
	protected abstract int getFuelTime(ItemStack arg);

	@Shadow
	private ItemStack[] inventory;

	@Shadow
	public abstract boolean isBurning();

	@Shadow
	public int cookTime;

	/**
	 * @author Risugami
	 * @reason idk
	 * TODO(halotroop2288): rewrite as an {@link Inject} Mixin
	 */
	@Environment(EnvType.CLIENT)
	@Overwrite
	public void tick() {
		int j = this.burnTime > 0 ? 1 : 0;
		int k = 0;

		if (this.burnTime > 0) {
			--this.burnTime;
		}

		if (!this.world.isClient) {
			if (this.burnTime == 0 && this.canAcceptRecipeOutput()) {
				this.fuelTime = this.burnTime = this.getFuelTime(this.inventory[1]);

				if (this.burnTime > 0) {
					k = 1;

					if (this.inventory[1] != null) {
						if (this.inventory[1].getItem().hasContainerItemType()) {
							this.inventory[1] = new ItemStack(this.inventory[1].getItem().getContainerItemType());
						} else {
							--this.inventory[1].count;
						}

						if (this.inventory[1].count == 0) {
							this.inventory[1] = null;
						}
					}
				}
			}

			if (this.isBurning() && this.canAcceptRecipeOutput()) {
				if (++this.cookTime == 200) {
					this.cookTime = 0;
					this.craftRecipe();
					k = 1;
				}
			} else {
				this.cookTime = 0;
			}

			if (j != (this.burnTime > 0 ? 1 : 0)) {
				k = 1;
				FurnaceBlock.updateFurnaceState(this.burnTime > 0, this.world, this.x, this.y, this.z);
			}
		}

		if (k != 0) {
			this.markDirty();
		}
	}

	/**
	 * @author Risugami
	 * @reason idk
	 * TODO(halotroop2288): rewrite as an {@link Inject} Mixin
	 */
	@Environment(EnvType.CLIENT)
	@Overwrite
	public void craftRecipe() {
		if (this.canAcceptRecipeOutput()) {
			ItemStack result = SmeltingRecipeRegistry.getInstance().getResult(this.inventory[0].getItem().id);

			if (this.inventory[2] == null) {
				this.inventory[2] = result.copy();
			} else if (this.inventory[2].itemId == result.itemId) {
				this.inventory[2].count += result.count;
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

	@Inject(method = "getFuelTime", cancellable = true, at = @At("RETURN"))
	private void modloader$getFuelTime(ItemStack par1, CallbackInfoReturnable<Integer> cir) {
		if (par1 != null && cir.getReturnValue() == 0) {
			int j = par1.getItem().id;
			cir.setReturnValue(ModLoader.AddAllFuel(j));
		}
	}
}
