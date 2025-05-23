package io.github.betterthanupdates.modloader.mixin.screen.slot;

import modloader.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceOutputSlot;

@Mixin(FurnaceOutputSlot.class)
public abstract class FurnaceOutputSlotMixin {
	@Shadow
	private PlayerEntity player;

	@Inject(method = "onCrafted", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;onCrafted(Lnet/minecraft/item/ItemStack;)V"))
	private void modloader$onCrafted(ItemStack par1, CallbackInfo ci) {
		ModLoader.TakenFromFurnace(this.player, par1);
	}
}
