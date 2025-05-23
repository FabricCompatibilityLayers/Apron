package io.github.betterthanupdates.forge.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import forge.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item {
	public BucketItemMixin(int i) {
		super(i);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "use", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1779(III)Lnet/minecraft/block/Material;", ordinal = 0))
	private void reforged$use(ItemStack itemStack, World world, PlayerEntity playerEntity, CallbackInfoReturnable<ItemStack> cir,
							  @Local(ordinal = 0) HitResult hitResult) {
		ItemStack customBucket = MinecraftForge.fillCustomBucket(world, hitResult.blockX, hitResult.blockY, hitResult.blockZ);

		if (customBucket != null) {
			cir.setReturnValue(customBucket);
		}
	}
}
