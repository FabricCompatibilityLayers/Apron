package io.github.betterthanupdates.stapi.mixin.client.forge;

import forge.ForgeHooksClient;
import forge.ICustomItemRenderer;
import forge.MinecraftForgeClient;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicOverlayRenderer;
import net.modificationstation.stationapi.mixin.arsenic.client.class_556Accessor;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(ArsenicOverlayRenderer.class)
public class ArsenicOverlayRendererMixin {
	@Shadow
	@Final
	private class_556Accessor access;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "renderItem3D",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderer;method_48(Lnet/minecraft/block/Block;IF)V", ordinal = 0))
	private void forge$render$1(LivingEntity entityliving, ItemStack itemstack, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "renderItem3D", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
			target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;"))
	private void forge$render$2(LivingEntity arg2, ItemStack itemStack, CallbackInfo ci) {
		if (itemStack.itemId < Block.BY_ID.length) {
			ForgeHooksClient.overrideTexture(Block.BY_ID[itemStack.itemId]);
		} else {
			ForgeHooksClient.overrideTexture(Item.byId[itemStack.itemId]);
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "renderItem3D", cancellable = true,
			at = @At(value = "INVOKE", ordinal = 0, target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", remap = false, shift = At.Shift.AFTER))
	private void forge$render$3(LivingEntity entityliving, ItemStack itemStack, CallbackInfo ci) {
		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(itemStack.itemId);

		if (customRenderer != null) {
			GL11.glBindTexture(3553, this.access.stationapi$getField_2401().textureManager.getTextureId("/terrain.png"));
			ForgeHooksClient.overrideTexture(itemStack.getItem());
			ForgeHooksClient.renderCustomItem(customRenderer, this.access.stationapi$getField_2405(), itemStack.itemId, itemStack.getMeta(), entityliving.getBrightnessAtEyes(1.0F));
			GL11.glPopMatrix();
			ci.cancel();
		}
	}
}
