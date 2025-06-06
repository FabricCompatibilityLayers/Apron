package io.github.fabriccompatibilitylayers.modloader.mixin.common;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import modloader.ModLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {
	@Shadow
	private PlayerEntity player;

	@Definition(id = "i", local = @Local(type = int.class, ordinal = 0))
	@Expression("i = 0")
	@Inject(method = "onTakeItem", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void modloader$TakenFromCrafting(ItemStack paramiz, CallbackInfo ci) {
		ModLoader.TakenFromCrafting(this.player, paramiz);
	}
}
