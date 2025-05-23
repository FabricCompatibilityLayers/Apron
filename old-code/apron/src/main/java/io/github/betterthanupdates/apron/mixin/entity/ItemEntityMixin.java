package io.github.betterthanupdates.apron.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.ItemEntity;

import io.github.betterthanupdates.apron.item.ItemConvertible;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements ItemConvertible {
}
