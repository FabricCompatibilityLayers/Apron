package io.github.betterthanupdates.shockahpi.mixin.client;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import shockahpi.SAPI;

import net.minecraft.entity.block.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.feature.DungeonFeature;
import net.minecraft.world.feature.Feature;

@Mixin(DungeonFeature.class)
public abstract class DungeonFeatureMixin extends Feature {

	/**
	 * @author SAPI
	 * @reason
	 */
	@Inject(method = "generate", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getBlockEntity(III)Lnet/minecraft/entity/BlockEntity;", ordinal = 0, shift = At.Shift.BY, by = 2), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void generate(World world, Random random, int i, int j, int k, CallbackInfoReturnable<Boolean> cir,
						 int l1, int l2, int l3, int l4, int l5, int l6, int l7, int l8, int l9, int l10, ChestBlockEntity chestBlockEntity) {
		for (int i6 = 0; i6 < Math.min(19, SAPI.dungeonGetAmountOfGuaranteed()); ++i6) {
			ItemStack stack = SAPI.dungeonGetGuaranteed(i6).getStack();

			if (stack != null) {
				chestBlockEntity.setInventoryItem(random.nextInt(chestBlockEntity.getInventorySize()), stack);
			}
		}
	}

	/**
	 * @author SAPI
	 * @reason
	 */
	@Inject(method = "getRandomChestItem", at = @At("HEAD"), cancellable = true)
	private void getRandomChestItem(Random paramRandom, CallbackInfoReturnable<ItemStack> cir) {
		cir.setReturnValue(SAPI.dungeonGetRandomItem());
	}

	/**
	 * @author SAPI
	 * @reason
	 */
	@Inject(method = "getRandomEntity", at = @At("HEAD"), cancellable = true)
	private void getRandomEntity(Random paramRandom, CallbackInfoReturnable<String> cir) {
		cir.setReturnValue(SAPI.dungeonGetRandomMob());
	}
}
