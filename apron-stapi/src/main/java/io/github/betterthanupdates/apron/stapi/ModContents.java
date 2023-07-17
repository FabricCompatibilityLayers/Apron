package io.github.betterthanupdates.apron.stapi;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ModContents {
	public final ModContentRegistry<Item> ITEMS = new ModContentRegistry<>();
	public final ModContentRegistry<Block> BLOCKS = new ModContentRegistry<>();

	public final ModTexturesRegistry TERRAIN = new ModTexturesRegistry();
	public final ModTexturesRegistry GUI_ITEMS = new ModTexturesRegistry();
}
