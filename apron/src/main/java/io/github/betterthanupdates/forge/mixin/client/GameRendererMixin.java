package io.github.betterthanupdates.forge.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldEventRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@WrapOperation(method = "method_1841", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldEventRenderer;method_1547(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V"))
	private void forge$method_1547(WorldEventRenderer instance, PlayerEntity arg2, HitResult i, int arg3, ItemStack f, float v, Operation<Void> operation) {
		if (!ForgeHooksClient.onBlockHighlight(instance, arg2, i, arg3, f, v)) {
			operation.call(instance, arg2, i, arg3, f, v);
		}
	}

	@WrapOperation(method = "method_1841", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldEventRenderer;method_1554(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V"))
	private void forge$method_1554(WorldEventRenderer instance, PlayerEntity arg2, HitResult i, int arg3, ItemStack f, float v, Operation<Void> operation) {
		if (!ForgeHooksClient.onBlockHighlight(instance, arg2, i, arg3, f, v)) {
			operation.call(instance, arg2, i, arg3, f, v);
		}
	}
}
