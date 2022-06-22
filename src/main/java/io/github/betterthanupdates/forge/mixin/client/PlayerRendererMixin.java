package io.github.betterthanupdates.forge.mixin.client;

import forge.IArmorTextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer {
	private PlayerRendererMixin(EntityModel entityModel, float f) {
		super(entityModel, f);
	}

	Item cachedItem;

	@Inject(method = "render(Lnet/minecraft/entity/player/PlayerEntity;IF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private void reforged$render(PlayerEntity entityplayer, int i, float f, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemstack = entityplayer.inventory.getArmorItem(3 - i);

		if (itemstack != null) {
			this.cachedItem = itemstack.getItem();
		}
	}

	@Redirect(method = "render(Lnet/minecraft/entity/player/PlayerEntity;IF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerRenderer;bindTexture(Ljava/lang/String;)V"))
	private void reforged$bindTexture(PlayerRenderer instance, String s) {
		if (this.cachedItem != null && this.cachedItem instanceof IArmorTextureProvider) {
			instance.bindTexture(((IArmorTextureProvider) this.cachedItem).getArmorTextureFile());
		} else {
			instance.bindTexture(s);
		}
	}
}
