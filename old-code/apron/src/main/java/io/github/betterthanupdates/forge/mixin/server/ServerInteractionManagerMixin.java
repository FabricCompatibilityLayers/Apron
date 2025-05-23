package io.github.betterthanupdates.forge.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import forge.ForgeHooks;
import forge.IUseItemFirst;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_70.class)
public class ServerInteractionManagerMixin {

	@Inject(method = "method_1831", at = @At(value = "RETURN", ordinal = 0))
	private void forge$method_1831(PlayerEntity player, World arg3, ItemStack par3, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) ItemStack usedStack) {
		if ((usedStack != par3 || usedStack != null && usedStack.count != par3.count) && usedStack.count == 0) {
			ForgeHooks.onDestroyCurrentItem(player, usedStack);
		}
	}

	@Inject(method = "method_1832", at = @At("HEAD"), cancellable = true)
	private void forge$activateBlock$first(PlayerEntity entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		if (itemstack != null && itemstack.getItem() instanceof IUseItemFirst) {
			IUseItemFirst iuif = (IUseItemFirst)itemstack.getItem();
			if (iuif.onItemUseFirst(itemstack, entityplayer, world, i, j, k, l)) {
				cir.setReturnValue(true);
			}
		}
	}

	@WrapOperation(method = "method_1832", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IIII)Z"))
	private boolean forge$activateBlock$destroyItemStack(ItemStack instance, PlayerEntity player, World world, int i, int j, int k, int l, Operation<Boolean> operation) {
		boolean result = operation.call(instance, player, world, i, j, k, l);

		if (result && instance.count == 0) {
			ForgeHooks.onDestroyCurrentItem(player, instance);
		}

		return result;
	}
}
