package io.github.betterthanupdates.forge.mixin.client.nostation;

import forge.ForgeHooksClient;
import forge.ICustomItemRenderer;
import forge.MinecraftForgeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_556;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(class_556.class)
public class HeldItemRendererMixin {
	@Shadow
	private Minecraft field_2401;

	@Shadow
	private BlockRenderManager field_2405;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1862",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;render(Lnet/minecraft/block/Block;IF)V", ordinal = 0))
	private void forge$render$1(LivingEntity entityliving, ItemStack itemstack, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BLOCKS[itemstack.itemId]);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1862", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
			target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;"))
	private void forge$render$2(LivingEntity arg2, ItemStack itemStack, CallbackInfo ci) {
		if (itemStack.itemId < 256) {
			ForgeHooksClient.overrideTexture(Block.BLOCKS[itemStack.itemId]);
		} else {
			ForgeHooksClient.overrideTexture(Item.ITEMS[itemStack.itemId]);
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1862", cancellable = true,
			at = @At(value = "INVOKE", ordinal = 0, target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", remap = false, shift = At.Shift.AFTER))
	private void forge$render$3(LivingEntity entityliving, ItemStack itemStack, CallbackInfo ci) {
		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(itemStack.itemId);

		if (customRenderer != null) {
			GL11.glBindTexture(3553, this.field_2401.textureManager.getTextureId("/terrain.png"));
			ForgeHooksClient.overrideTexture(itemStack.getItem());
			ForgeHooksClient.renderCustomItem(customRenderer, this.field_2405, itemStack.itemId, itemStack.getDamage(), entityliving.method_1394(1.0F));
			GL11.glPopMatrix();
			ci.cancel();
		}
	}
}
