package io.github.betterthanupdates.apron.stapi.mixin.ami;

import net.glasslauncher.mods.alwaysmoreitems.util.StackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(StackHelper.class)
public class StackHelperMixin {
	@Inject(method = "getSubtypes(Lnet/minecraft/item/Item;I)Ljava/util/List;", cancellable = true, at = @At("HEAD"))
	private void apron$ignoreDummyItems(Item item, int stackSize, CallbackInfoReturnable<List<ItemStack>> cir) {
		Identifier identifier = net.modificationstation.stationapi.api.registry.ItemRegistry.INSTANCE.getId(item);

		if (identifier != null && identifier.getNamespace().toString().startsWith("mod_") && identifier.getPath().endsWith("_")) {
			cir.setReturnValue(new ArrayList<>());
		}
	}
}
