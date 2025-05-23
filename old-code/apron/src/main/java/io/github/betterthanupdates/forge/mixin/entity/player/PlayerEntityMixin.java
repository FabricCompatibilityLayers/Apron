package io.github.betterthanupdates.forge.mixin.entity.player;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import forge.ArmorProperties;
import forge.ForgeHooks;
import forge.ISpecialArmor;
import net.minecraft.class_141;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.entity.player.ForgePlayerEntity;
import io.github.betterthanupdates.forge.item.ForgeItem;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ForgePlayerEntity {
	@Shadow
	public PlayerInventory inventory;

	@Shadow
	public abstract boolean method_943();

	private PlayerEntityMixin(World world) {
		super(world);
	}

	/**
	 * Gets the strength of the player against the block, taking
	 * into account the meta value of the block state.
	 *
	 * @param block the block id to check strength against
	 * @param meta  the meta value of the block state
	 * @return the strength of the player against the given block
	 */
	@Override
	public float getCurrentPlayerStrVsBlock(Block block, int meta) {
		float strength = 1.0F;
		ItemStack heldItem = this.inventory.getSelectedItem();

		if (heldItem != null) {
			strength = ((ForgeItem) heldItem.getItem()).getStrVsBlock(heldItem, block, meta);
		}

		if (this.isInFluid(Material.WATER)) {
			strength /= 5.0F;
		}

		if (!this.field_1623) {
			strength /= 5.0F;
		}

		return strength;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_946", cancellable = true, at = @At("HEAD"))
	private void forge$applyDamage(int i, CallbackInfo ci) {
		boolean doRegularComputation = true;
		int initialDamage = i;

		for (ItemStack stack : this.inventory.armor) {
			if (stack != null && stack.getItem() instanceof ISpecialArmor) {
				ISpecialArmor armor = (ISpecialArmor) stack.getItem();
				ArmorProperties props = armor.getProperties((PlayerEntity) (Object) this, initialDamage, i);
				i -= props.damageRemove;
				doRegularComputation = doRegularComputation && props.allowRegularComputation;
			}
		}

		if (!doRegularComputation) {
			super.method_946(i);
			ci.cancel();
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_503", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
	private void forge$breakHeldItem$Head(CallbackInfo ci, @Share("orig") LocalRef<ItemStack> ref) {
		ref.set(this.inventory.getSelectedItem());
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_503", at = @At(value = "RETURN"))
	private void forge$breakHeldItem$Return(CallbackInfo ci, @Share("orig") LocalRef<ItemStack> ref) {
		ForgeHooks.onDestroyCurrentItem((PlayerEntity) (Object) this, ref.get());
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_495", at = @At("HEAD"), cancellable = true)
	private void forge$trySleep(int i, int j, int k, CallbackInfoReturnable<class_141> cir) {
		class_141 customSleep = ForgeHooks.sleepInBedAt((PlayerEntity) (Object) this, i, j, k);

		if (customSleep != null) {
			cir.setReturnValue(customSleep);
		}
	}
}
