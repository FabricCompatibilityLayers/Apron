package io.github.betterthanupdates.forge.mixin;

import forge.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item {
	public BucketItemMixin(int i) {
		super(i);
	}

	@Inject(method = "use", cancellable = true,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getMaterial(III)Lnet/minecraft/block/material/Material;", ordinal = 0, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void reforged$use(ItemStack itemStack, World world, PlayerEntity playerEntity, CallbackInfoReturnable<ItemStack> cir,
							  float var4, float var5, float var6, double var7, double var9, double var11, Vec3d var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, double var21, Vec3d var23, HitResult var24,
							  int x, int y, int z) {
		ItemStack customBucket = MinecraftForge.fillCustomBucket(world, x, y, z);

		if (customBucket != null) {
			cir.setReturnValue(customBucket);
		}
	}
}
