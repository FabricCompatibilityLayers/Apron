package io.github.betterthanupdates.forge.mixin.client;

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
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.model.EntityModel;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer {
	private PlayerRendererMixin(EntityModel entityModel, float f) {
		super(entityModel, f);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@WrapOperation(method = "render(Lnet/minecraft/entity/player/PlayerEntity;IF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerRenderer;bindTexture(Ljava/lang/String;)V"))
	private void forge$bindTexture(PlayerRenderer instance, String s, Operation<Void> operation, @Local(ordinal = 0)ArmorItem item) {
		if (item instanceof IArmorTextureProvider) {
			operation.call(instance, ((IArmorTextureProvider) item).getArmorTextureFile());
		} else {
			operation.call(instance, s);
		}
	}
}
