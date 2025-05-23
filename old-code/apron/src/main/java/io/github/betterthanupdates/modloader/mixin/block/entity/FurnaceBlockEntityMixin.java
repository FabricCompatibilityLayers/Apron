package io.github.betterthanupdates.modloader.mixin.block.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import modloader.ModLoader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity implements Inventory {
	@Shadow
	private ItemStack[] inventory;

	@WrapWithCondition(method = "method_1076", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;count:I", opcode = Opcodes.PUTFIELD))
	private boolean modloader$tick(ItemStack instance, int value) {
		if (this.inventory[1].getItem().hasCraftingReturnItem()) {
			this.inventory[1] = new ItemStack(this.inventory[1].getItem().getCraftingReturnItem());
			return false;
		}

		return true;
	}

	@ModifyReturnValue(method = "getFuelTime", at = @At("RETURN"))
	private int modloader$getFuelTime(int original, @Local(ordinal = 0, argsOnly = true) ItemStack par1) {
		if (par1 != null && original == 0) {
			int j = par1.getItem().id;
			return ModLoader.AddAllFuel(j);
		}

		return original;
	}
}
