package io.github.betterthanupdates.forge.mixin.server.server.player;

import com.llamalad7.mixinextras.sugar.Local;
import forge.ForgeHooks;
import forge.IUseItemFirst;
import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.player.ServerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerInteractionManager.class)
public class ServerInteractionManagerMixin {
	@Shadow
	private ServerWorld world;

	@Shadow
	private int field_2318;

	@Shadow
	private int field_2319;

	@Shadow
	private int field_2320;

	@Redirect(method = "method_1828", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float forge$method_1828$blockStrength(Block instance, PlayerEntity playerEntity) {
		return ((ForgeBlock) instance).blockStrength(this.world, playerEntity, this.field_2318, this.field_2319, this.field_2320);
	}

	@Redirect(method = {"method_1830", "method_1829"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F", ordinal = 0))
	private float forge$blockStrength(Block instance, PlayerEntity playerEntity, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return ((ForgeBlock) instance).blockStrength(this.world, playerEntity, i, j, k);
	}

	@Redirect(method = "method_1834", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canRemoveBlock(Lnet/minecraft/block/Block;)Z"))
	private boolean forge$canHarvestBlock(PlayerEntity instance, Block block, @Local(ordinal = 4) int meta) {
		return ((ForgeBlock) block).canHarvestBlock(instance, meta);
	}

	@Inject(method = "method_1831", at = @At(value = "RETURN", ordinal = 0))
	private void forge$method_1831(PlayerEntity player, World arg3, ItemStack par3, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) ItemStack usedStack) {
		if ((usedStack != par3 || usedStack != null && usedStack.count != par3.count) && usedStack.count == 0) {
			ForgeHooks.onDestroyCurrentItem(player, usedStack);
		}
	}

	@Inject(method = "activateBlock", at = @At("HEAD"), cancellable = true)
	private void forge$activateBlock$first(PlayerEntity entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		if (itemstack != null && itemstack.getItem() instanceof IUseItemFirst) {
			IUseItemFirst iuif = (IUseItemFirst)itemstack.getItem();
			if (iuif.onItemUseFirst(itemstack, entityplayer, world, i, j, k, l)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Redirect(method = "activateBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IIII)Z"))
	private boolean forge$activateBlock$destroyItemStack(ItemStack instance, PlayerEntity player, World world, int i, int j, int k, int l) {
		if (!instance.useOnBlock(player, world, i, j, k, l)) {
			return false;
		} else {
			if (instance.count == 0) {
				ForgeHooks.onDestroyCurrentItem(player, instance);
			}

			return true;
		}
	}
}
