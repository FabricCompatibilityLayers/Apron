package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.render.HeldItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@Inject(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0, shift = At.Shift.AFTER))
	private void reforged$render$1(LivingEntity entityliving, ItemStack itemstack, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
	}

	@Inject(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1, shift = At.Shift.AFTER))
	private void reforged$render$2(LivingEntity entityliving, ItemStack itemstack, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
	}

	@Inject(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 2, shift = At.Shift.AFTER))
	private void reforged$render$3(LivingEntity entityliving, ItemStack itemstack, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Item.byId[itemstack.itemId]);
	}
}
