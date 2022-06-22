package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import reforged.Reforged;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldEventRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	private Minecraft client;

	/**
	 * @return ShockAhPI-modified reach
	 * @author ShockAh
	 * @reason implement ShockAhPI function
	 */
	@ModifyConstant(method = "method_1838", constant = @Constant(doubleValue = 3.0d))
	private double shockahpi$modifyReach(double constant) {
		return Reforged.reachGetEntityPlayer(this.client.player);
	}

	@Redirect(method = "method_1841", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldEventRenderer;method_1547(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V"))
	private void reforged$method_1547(WorldEventRenderer instance, PlayerEntity arg2, HitResult i, int arg3, ItemStack f, float v) {
		if (!ForgeHooksClient.onBlockHighlight(instance, arg2, i, arg3, f, v)) {
			instance.method_1547(arg2, i, arg3, f, v);
		}
	}

	@Redirect(method = "method_1841", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldEventRenderer;method_1554(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V"))
	private void reforged$method_1554(WorldEventRenderer instance, PlayerEntity arg2, HitResult i, int arg3, ItemStack f, float v) {
		if (!ForgeHooksClient.onBlockHighlight(instance, arg2, i, arg3, f, v)) {
			instance.method_1554(arg2, i, arg3, f, v);
		}
	}
}
