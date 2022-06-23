package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooks;
import modloader.ModLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.container.slot.CraftingResultSlot;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin extends Slot {
	@Shadow
	private PlayerEntity player;

	@Shadow
	@Final
	private Inventory craftingInventory;

	public CraftingResultSlotMixin(Inventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "onCrafted", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getInventorySize()I", ordinal = 0, shift = At.Shift.BEFORE))
	private void reforged$onCrafted(ItemStack itemStack, CallbackInfo ci) {
		ModLoader.TakenFromCrafting(this.player, itemStack);
		ForgeHooks.onTakenFromCrafting(this.player, itemStack, this.craftingInventory);
	}
}
