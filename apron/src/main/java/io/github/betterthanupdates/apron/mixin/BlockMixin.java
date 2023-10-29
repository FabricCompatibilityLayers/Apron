package io.github.betterthanupdates.apron.mixin;

import io.github.betterthanupdates.stapi.StAPIBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import io.github.betterthanupdates.apron.item.ItemConvertible;

@Mixin(Block.class)
public class BlockMixin implements ItemConvertible, StAPIBlock {
}
