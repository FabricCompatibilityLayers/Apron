package io.github.betterthanupdates.apron.stapi.mixin;

import forge.ITextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.SpritesheetInstance;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Environment(EnvType.CLIENT)
	@Inject(method = "getHeldItemTexture", at = @At("RETURN"), cancellable = true)
	private void apron$fixItemTextureIndex(ItemStack par1, CallbackInfoReturnable<Integer> cir) {
		Item item = par1.getItem();

		int result = cir.getReturnValue();

		if (item instanceof ITextureProvider textureProvider) {
			SpritesheetInstance itemSpritesheet = ApronStAPICompat.SPRITESHEET_MAP.get(textureProvider.getTextureFile());

			if (itemSpritesheet != null) {
				if (item instanceof BlockItem) {
					if (itemSpritesheet.BLOCKS.containsKey(result)) {
						result = itemSpritesheet.BLOCKS.get(result);
					}
				} else if (itemSpritesheet.ITEMS.containsKey(result)) {
					result = itemSpritesheet.ITEMS.get(result);
				}
			}
		} else {
			if (item instanceof BlockItem) {
				if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(result)) {
					result = ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(result);
				}
			} else if (ApronStAPICompat.INDEX_TO_FIXED_ITEM.containsKey(result)) {
				result = ApronStAPICompat.INDEX_TO_FIXED_ITEM.get(result);
			}
		}

		cir.setReturnValue(result);
	}
}
