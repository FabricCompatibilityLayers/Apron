package io.github.betterthanupdates.apron.mixin.item;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.Item;

import io.github.betterthanupdates.apron.item.ItemConvertible;

@Mixin(Item.class)
public class ItemMixin implements ItemConvertible {
}
