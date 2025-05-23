package io.github.betterthanupdates.forge.mixin.client.render.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import forge.IArmorTextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer {
	private PlayerEntityRendererMixin(EntityModel entityModel, float f) {
		super(entityModel, f);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@WrapOperation(method = "method_825(Lnet/minecraft/entity/player/PlayerEntity;IF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;bindTexture(Ljava/lang/String;)V"))
	private void forge$bindTexture(PlayerEntityRenderer instance, String s, Operation<Void> operation, @Local(ordinal = 0)ArmorItem item) {
		if (item instanceof IArmorTextureProvider) {
			operation.call(instance, ((IArmorTextureProvider) item).getArmorTextureFile());
		} else {
			operation.call(instance, s);
		}
	}
}
