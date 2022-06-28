package io.github.betterthanupdates.reforged.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerClientInteractionManager;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.reforged.item.ReforgedItem;

@Mixin(MultiplayerClientInteractionManager.class)
public class MultiplayerClientInteractionManagerMixin extends ClientInteractionManager {
	private MultiplayerClientInteractionManagerMixin(Minecraft minecraft) {
		super(minecraft);
	}

	@Inject(method = "method_1716", at = @At("HEAD"), cancellable = true)
	private void reforged$method_1716(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemstack = this.client.player.getHeldItem();

		if (itemstack != null && ((ReforgedItem) itemstack.getItem()).onBlockStartBreak(itemstack, i, j, k, this.client.player)) {
			cir.setReturnValue(false);
		}
	}
}
