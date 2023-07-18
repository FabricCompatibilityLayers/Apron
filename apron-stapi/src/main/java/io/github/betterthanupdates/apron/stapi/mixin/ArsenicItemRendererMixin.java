package io.github.betterthanupdates.apron.stapi.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import forge.ITextureProvider;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.SpritesheetInstance;

@Mixin(ArsenicItemRenderer.class)
public class ArsenicItemRendererMixin {
	@Redirect(method = "renderVanilla", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItemTexture()I"))
	private int apron$fixItemTextureIndex(ItemStack instance) {
		int oldIndex = instance.getItemTexture();

		Item item = instance.getItem();

		if (item instanceof ITextureProvider textureProvider) {
			SpritesheetInstance itemSpritesheet = ApronStAPICompat.SPRITESHEET_MAP.get(textureProvider.getTextureFile());

			if (itemSpritesheet != null) {
				if (item instanceof BlockItem) {
					if (itemSpritesheet.BLOCKS.containsKey(oldIndex)) {
						return itemSpritesheet.BLOCKS.get(oldIndex);
					}
				} else if (itemSpritesheet.ITEMS.containsKey(oldIndex)) {
					return itemSpritesheet.ITEMS.get(oldIndex);
				}
			}
		} else {
			if (item instanceof BlockItem) {
				if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(oldIndex)) {
					return ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(oldIndex);
				}
			} else if (ApronStAPICompat.INDEX_TO_FIXED_ITEM.containsKey(oldIndex)) {
				return ApronStAPICompat.INDEX_TO_FIXED_ITEM.get(oldIndex);
			}
		}

		return oldIndex;
	}

	@ModifyVariable(method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V",
			argsOnly = true, at = @At("HEAD"), ordinal = 2)
	private int apron$fixItemTextureIndex(int old, @Local(argsOnly = true, ordinal = 0) int id) {
		Item item = Item.byId[id];

		if (item instanceof ITextureProvider textureProvider) {
			SpritesheetInstance itemSpritesheet = ApronStAPICompat.SPRITESHEET_MAP.get(textureProvider.getTextureFile());

			if (itemSpritesheet != null) {
				if (item instanceof BlockItem) {
					if (itemSpritesheet.BLOCKS.containsKey(old)) {
						return itemSpritesheet.BLOCKS.get(old);
					}
				} else if (itemSpritesheet.ITEMS.containsKey(old)) {
					return itemSpritesheet.ITEMS.get(old);
				}
			}
		} else {
			if (item instanceof BlockItem) {
				if (ApronStAPICompat.INDEX_TO_FIXED_BLOCK.containsKey(old)) {
					return ApronStAPICompat.INDEX_TO_FIXED_BLOCK.get(old);
				}
			} else if (ApronStAPICompat.INDEX_TO_FIXED_ITEM.containsKey(old)) {
				return ApronStAPICompat.INDEX_TO_FIXED_ITEM.get(old);
			}
		}

		return old;
	}
}
