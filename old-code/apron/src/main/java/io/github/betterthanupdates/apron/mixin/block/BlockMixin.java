package io.github.betterthanupdates.apron.mixin.block;

import io.github.betterthanupdates.stapi.StAPIBlock;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;

import io.github.betterthanupdates.apron.item.ItemConvertible;

@Mixin(Block.class)
public class BlockMixin implements ItemConvertible, StAPIBlock {
}
