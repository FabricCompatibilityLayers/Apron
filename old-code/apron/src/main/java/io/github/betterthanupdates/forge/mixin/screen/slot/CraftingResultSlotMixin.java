package io.github.betterthanupdates.forge.mixin.screen.slot;

import forge.ForgeHooks;
import modloader.ModLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin extends Slot {
	@Shadow
	private PlayerEntity player;

	@Shadow
	@Final
	private Inventory field_2366;

	public CraftingResultSlotMixin(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge + ModLoader hooks
	 */
	@Inject(method = "onCrafted", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;size()I", ordinal = 0, shift = At.Shift.BEFORE))
	private void forge$onCrafted(ItemStack itemStack, CallbackInfo ci) {
		ModLoader.TakenFromCrafting(this.player, itemStack);
		ForgeHooks.onTakenFromCrafting(this.player, itemStack, this.field_2366);
	}
}
