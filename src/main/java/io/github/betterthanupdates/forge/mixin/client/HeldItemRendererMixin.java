package io.github.betterthanupdates.forge.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.HeldItemRenderer;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
//	@Shadow
//	private Minecraft client;
//
//	@Shadow
//	private BlockRenderer blockRenderer;
//
//	/**
//	 * @author Eloraam
//	 * @reason implement Forge hooks
//	 */
//	@Inject(method = "render",
//			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderer;method_48(Lnet/minecraft/block/Block;IF)V", ordinal = 0))
//	private void forge$render$1(LivingEntity entityliving, ItemStack itemstack, CallbackInfo ci) {
//		ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
//	}
//
//	/**
//	 * @author Eloraam
//	 * @reason implement Forge hooks
//	 */
//	@Inject(method = "render", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
//			target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;"))
//	private void forge$render$2(LivingEntity arg2, ItemStack itemStack, CallbackInfo ci) {
//		if (itemStack.itemId < 256) {
//			ForgeHooksClient.overrideTexture(Block.BY_ID[itemStack.itemId]);
//		} else {
//			ForgeHooksClient.overrideTexture(Item.byId[itemStack.itemId]);
//		}
//	}
//
//	/**
//	 * @author Eloraam
//	 * @reason implement Forge hooks
//	 */
//	@Inject(method = "render", cancellable = true,
//			at = @At(value = "INVOKE", ordinal = 0, target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", remap = false, shift = At.Shift.AFTER))
//	private void forge$render$3(LivingEntity entityliving, ItemStack itemStack, CallbackInfo ci) {
//		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(itemStack.itemId);
//
//		if (customRenderer != null) {
//			GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/terrain.png"));
//			ForgeHooksClient.overrideTexture(itemStack.getItem());
//			ForgeHooksClient.renderCustomItem(customRenderer, this.blockRenderer, itemStack.itemId, itemStack.getMeta(), entityliving.getBrightnessAtEyes(1.0F));
//			GL11.glPopMatrix();
//			ci.cancel();
//		}
//	}
}
