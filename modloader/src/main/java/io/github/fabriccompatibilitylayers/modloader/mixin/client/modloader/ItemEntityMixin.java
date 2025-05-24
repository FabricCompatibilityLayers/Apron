package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import modloader.ModLoader;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Shadow
	public ItemStack stack;

	@Inject(method = "onPlayerInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V"))
	private void modloader$OnItemPickup(PlayerEntity paramgs, CallbackInfo ci) {
		ModLoader.OnItemPickup(paramgs, this.stack);
	}
}
