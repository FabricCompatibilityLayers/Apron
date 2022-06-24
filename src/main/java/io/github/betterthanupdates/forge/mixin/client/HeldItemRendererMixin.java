package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.objectweb.asm.Opcodes;
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
	@Inject(method = "render",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderer;method_48(Lnet/minecraft/block/Block;IF)V", ordinal = 0))
	private void reforged$render$1(LivingEntity entityliving, ItemStack itemstack, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
	}

	@Inject(method = "render", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
			target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;"))
	private void reforged$render$2(LivingEntity arg2, ItemStack itemStack, CallbackInfo ci) {
		if (itemStack.itemId < 256) ForgeHooksClient.overrideTexture(Block.BY_ID[itemStack.itemId]);
		else {
			ForgeHooksClient.overrideTexture(Item.byId[itemStack.itemId]);
		}
	}
}
