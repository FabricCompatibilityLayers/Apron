package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {
	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 0))
	private void reforged$render$1(ItemEntity entityitem, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
		ItemStack itemstack = entityitem.stack;
		ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
	}

	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 1))
	private void reforged$render$2(ItemEntity entityitem, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
		ItemStack itemstack = entityitem.stack;
		ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
	}

	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 2))
	private void reforged$render$3(ItemEntity entityitem, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
		ItemStack itemstack = entityitem.stack;
		ForgeHooksClient.overrideTexture(Item.byId[itemstack.itemId]);
	}

	@Inject(method = "renderItemOnGui", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", shift = At.Shift.AFTER, ordinal = 0))
	private void reforged$renderItemOnGui$1(TextRenderer fontrenderer, TextureManager renderengine, int i, int j, int k, int l, int i1, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BY_ID[i]);
	}

	@Inject(method = "renderItemOnGui", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", shift = At.Shift.AFTER, ordinal = 1))
	private void reforged$renderItemOnGui$2(TextRenderer fontrenderer, TextureManager renderengine, int i, int j, int k, int l, int i1, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BY_ID[i]);
	}

	@Inject(method = "renderItemOnGui", at = @At(value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", shift = At.Shift.AFTER, ordinal = 2))
	private void reforged$renderItemOnGui$3(TextRenderer fontrenderer, TextureManager renderengine, int i, int j, int k, int l, int i1, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Item.byId[i]);
	}
}
