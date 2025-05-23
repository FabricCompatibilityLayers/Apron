package io.github.betterthanupdates.modloader.mixin.client.entity;

import modloader.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
	@Shadow
	public ItemStack stack;

	public ItemEntityMixin(World arg) {
		super(arg);
	}

	@Inject(method = "onPlayerInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V"))
	private void modloader$onPlayerCollision(PlayerEntity par1, CallbackInfo ci) {
		ModLoader.OnItemPickup(par1, this.stack);
	}
}
