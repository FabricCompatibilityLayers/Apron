package io.github.betterthanupdates.forge.mixin.modloader;

import modloader.ModLoader;
import net.minecraft.container.slot.FurnaceOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceOutputSlot.class)
public class FurnaceOutputSlotMixin {

    @Shadow private PlayerEntity player;

    @Inject(method = "onCrafted", at = @At(value = "INVOKE", target = "Lnet/minecraft/container/slot/Slot;onCrafted(Lnet/minecraft/item/ItemStack;)V"))
    private void modloader$onCrafted(ItemStack par1, CallbackInfo ci) {
        ModLoader.TakenFromFurnace(this.player, par1);
    }
}
