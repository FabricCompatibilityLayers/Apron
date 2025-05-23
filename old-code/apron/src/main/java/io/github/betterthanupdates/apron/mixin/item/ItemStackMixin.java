package io.github.betterthanupdates.apron.mixin.item;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.apron.item.ItemConvertible;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemConvertible {
}
