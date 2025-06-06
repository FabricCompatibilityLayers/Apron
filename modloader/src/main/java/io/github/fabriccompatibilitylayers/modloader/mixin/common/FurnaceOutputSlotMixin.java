package io.github.fabriccompatibilitylayers.modloader.mixin.common;

import modloader.ModLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceOutputSlot.class)
public class FurnaceOutputSlotMixin {
	@Shadow
	private PlayerEntity player;

	@Inject(method = "onTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;onTakeItem(Lnet/minecraft/item/ItemStack;)V"))
	private void modloader$TakenFromFurnace(ItemStack paramiz, CallbackInfo ci) {
		ModLoader.TakenFromFurnace(this.player, paramiz);
	}
}
