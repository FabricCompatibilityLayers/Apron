package io.github.betterthanupdates.apron.stapi.compat.mixin.client.portalgun;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mod_PortalGun;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(mod_PortalGun.class)
@Environment(EnvType.CLIENT)
public class mod_PortalGunMixin {
	@Shadow
	public static Block PortalCube;

	@WrapOperation(method = "<init>", at = {
			@At(value = "NEW", target = "(Lnet/minecraft/item/Item;II)Lnet/minecraft/item/ItemStack;", ordinal = 0),
			@At(value = "NEW", target = "(Lnet/minecraft/item/Item;II)Lnet/minecraft/item/ItemStack;", ordinal = 1)
	})
	private ItemStack apron$fixItemStack(Item count, int damage, int i, Operation<ItemStack> original) {
		var originalId = ApronStAPICompat.getModContent().BLOCKS.autoToOriginal.get(PortalCube.id);
		var newItem = ApronStAPICompat.getModContent().ITEMS.originalToInstance.get(originalId);

		return new ItemStack(newItem, damage, i);
	}
}
