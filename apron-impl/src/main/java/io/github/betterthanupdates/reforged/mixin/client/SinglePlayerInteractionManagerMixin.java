package io.github.betterthanupdates.reforged.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.reforged.item.ReforgedItem;

@Mixin(SingleplayerInteractionManager.class)
public class SinglePlayerInteractionManagerMixin extends ClientInteractionManager {
	public SinglePlayerInteractionManagerMixin(Minecraft minecraft) {
		super(minecraft);
	}

	@Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
	private void reforged$method_1716(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemstack = this.client.player.getHeldItem();

		if (itemstack != null && ((ReforgedItem) itemstack.getItem()).onBlockStartBreak(itemstack, i, j, k, this.client.player)) {
			cir.setReturnValue(false);
		}
	}
}
