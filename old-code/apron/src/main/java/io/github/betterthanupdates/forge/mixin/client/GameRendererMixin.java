package io.github.betterthanupdates.forge.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_555;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
@Mixin(class_555.class)
public abstract class GameRendererMixin {
	@WrapWithCondition(method = "method_1841", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;method_1547(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V"))
	private boolean forge$method_1547(WorldRenderer instance, PlayerEntity arg2, HitResult i, int arg3, ItemStack f, float v) {
		return !ForgeHooksClient.onBlockHighlight(instance, arg2, i, arg3, f, v);
	}

	@WrapWithCondition(method = "method_1841", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;method_1554(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V"))
	private boolean forge$method_1554(WorldRenderer instance, PlayerEntity arg2, HitResult i, int arg3, ItemStack f, float v) {
		return !ForgeHooksClient.onBlockHighlight(instance, arg2, i, arg3, f, v);
	}
}
