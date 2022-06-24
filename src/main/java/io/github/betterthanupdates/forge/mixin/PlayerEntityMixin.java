package io.github.betterthanupdates.forge.mixin;

import forge.ArmorProperties;
import forge.ForgeHooks;
import forge.ISpecialArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SleepStatus;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.entity.player.ForgePlayerEntity;
import io.github.betterthanupdates.forge.item.ForgeItem;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ForgePlayerEntity {
	@Shadow
	public PlayerInventory inventory;

	@Shadow
	public abstract boolean isLyingOnBed();

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
		ItemStack heldItem = this.inventory.getHeldItem();

		if (heldItem != null) {
			strength = ((ForgeItem) heldItem.getItem()).getStrVsBlock(heldItem, block, meta);
		}

		if (this.isInFluid(Material.WATER)) {
			strength /= 5.0F;
		}

		if (!this.onGround) {
			strength /= 5.0F;
		}

		return strength;
	}

	@Inject(method = "applyDamage", cancellable = true, at = @At("HEAD"))
	private void reforged$applyDamage(int i, CallbackInfo ci) {
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
			super.applyDamage(i);
			ci.cancel();
		}
	}

	ItemStack orig;

	@Inject(method = "breakHeldItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/PlayerInventory;setInventoryItem(ILnet/minecraft/item/ItemStack;)V"))
	private void reforged$breakHeldItem$Head(CallbackInfo ci) {
		this.orig = this.inventory.getHeldItem();
	}

	@Inject(method = "breakHeldItem", at = @At(value = "RETURN"))
	private void reforged$breakHeldItem$Return(CallbackInfo ci) {
		ForgeHooks.onDestroyCurrentItem((PlayerEntity) (Object) this, this.orig);
	}

	@Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
	private void reforged$trySleep(int i, int j, int k, CallbackInfoReturnable<SleepStatus> cir) {
		SleepStatus customSleep = ForgeHooks.sleepInBedAt((PlayerEntity) (Object) this, i, j, k);

		if (customSleep != null) {
			cir.setReturnValue(customSleep);
		}
	}
}
