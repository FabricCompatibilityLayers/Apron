package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooks;
import forge.IUseItemFirst;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(ClientInteractionManager.class)
public class ClientInteractionManagerMixin {
	@Inject(method = "method_1712", at = @At("RETURN"))
	private void reforged$method_1712(PlayerEntity entityplayer, World world, ItemStack itemstack, CallbackInfoReturnable<Boolean> cir) {
		int i = itemstack.count;
		ItemStack itemstack1 = itemstack.use(world, entityplayer);

		if (itemstack1 != itemstack || itemstack1 != null && itemstack1.count != i) {
			if (itemstack1.count == 0) {
				ForgeHooks.onDestroyCurrentItem(entityplayer, itemstack1);
			}
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement forge hooks
	 */
	@Overwrite
	public boolean useItemOnBlock(PlayerEntity player, World world, ItemStack stack, int x, int y, int z, int side) {
		int i1 = world.getBlockId(x, y, z);

		if (i1 > 0 && Block.BY_ID[i1].canUse(world, x, y, z, player)) {
			return true;
		} else if (stack == null) {
			return false;
		} else if (!stack.useOnBlock(player, world, x, y, z, side)) {
			return false;
		} else {
			if (stack.count == 0) {
				ForgeHooks.onDestroyCurrentItem(player, stack);
			}

			return true;
		}
	}

	@Inject(method = "useItemOnBlock", at = @At("HEAD"), cancellable = true)
	private void reforged$IUseItemFirst(PlayerEntity player, World world, ItemStack stack, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && stack.getItem() instanceof IUseItemFirst) {
			IUseItemFirst iuif = (IUseItemFirst) stack.getItem();

			if (iuif.onItemUseFirst(stack, player, world, x, y, z, side)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Redirect(method = "useItemOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IIII)Z"))
	private boolean reforged$onDestroyCurrentItem(ItemStack instance, PlayerEntity arg2, World world, int j, int k, int l, int i) {
		if (!instance.useOnBlock(arg2, world, j, k, l, i)) {
			return false;
		} else {
			if (instance.count == 0) {
				ForgeHooks.onDestroyCurrentItem(arg2, instance);
			}

			return true;
		}
	}
}
