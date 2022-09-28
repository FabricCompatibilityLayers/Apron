package io.github.betterthanupdates.stapi.mixin.vanilla;

import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import io.github.betterthanupdates.stapi.StationAPIHelper;

@Mixin(Item.class)
public class ItemMixin {
	@Shadow
	@Final
	public int id;

	@ModifyVariable(method = "<init>", at = @At(value = "CONSTANT", ordinal = 0, args = "intValue=256"), ordinal = 0, argsOnly = true)
	private int apron_stapi_assignItemId(int id) {
		return ((Item) (Object) this) instanceof BlockItem ? id : StationAPIHelper.assignItemId(id);
	}

	@Inject(method = "setTranslationKey", at = @At("RETURN"))
	private void apron_stapi_registerItem(String key, CallbackInfoReturnable<Item> cir) {
		if (StationAPIHelper.ITEMS.containsKey(this.id) && !ItemRegistry.INSTANCE.getKey((Item) (Object) this).isPresent()) {
			String id = key;

			if (ItemRegistry.INSTANCE.containsId(StationAPIHelper.ITEMS.get(this.id).id(id))) {
				int i = 0;
				String alternativeId = id + i;

				while (ItemRegistry.INSTANCE.containsId(StationAPIHelper.ITEMS.get(this.id).id(alternativeId))) {
					i++;
					alternativeId = id + i;
				}

				id = alternativeId;
			}

			Registry.register(ItemRegistry.INSTANCE, StationAPIHelper.ITEMS.get(this.id).id(id), (Item) (Object) this);
		}
	}
}
