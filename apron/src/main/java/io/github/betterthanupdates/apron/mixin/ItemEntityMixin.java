package io.github.betterthanupdates.apron.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.apron.item.ItemConvertible;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements ItemConvertible {
}
