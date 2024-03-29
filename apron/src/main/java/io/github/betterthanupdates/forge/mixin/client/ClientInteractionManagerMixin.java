package io.github.betterthanupdates.forge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import forge.ForgeHooks;
import forge.IUseItemFirst;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(ClientInteractionManager.class)
public class ClientInteractionManagerMixin {
	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1712", at = @At(value = "RETURN", ordinal = 0))
	private void forge$method_1712(PlayerEntity player, World world, ItemStack itemstack,
								   CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) ItemStack itemstack1) {
		ForgeHooks.onDestroyCurrentItem(player, itemstack1);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "useItemOnBlock", at = @At("HEAD"), cancellable = true)
	private void forge$IUseItemFirst(PlayerEntity player, World world, ItemStack stack, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && stack.getItem() instanceof IUseItemFirst) {
			IUseItemFirst iuif = (IUseItemFirst) stack.getItem();

			if (iuif.onItemUseFirst(stack, player, world, x, y, z, side)) {
				cir.setReturnValue(true);
			}
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@WrapOperation(method = "useItemOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IIII)Z"))
	private boolean forge$onDestroyCurrentItem(ItemStack instance, PlayerEntity arg2, World world, int j, int k, int l, int i, Operation<Boolean> operation) {
		if (!operation.call(instance, arg2, world, j, k, l, i)) {
			return false;
		} else {
			if (instance.count == 0) {
				ForgeHooks.onDestroyCurrentItem(arg2, instance);
			}

			return true;
		}
	}
}
