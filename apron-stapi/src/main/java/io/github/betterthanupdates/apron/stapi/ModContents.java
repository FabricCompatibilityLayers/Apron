package io.github.betterthanupdates.apron.stapi;

import io.github.betterthanupdates.apron.stapi.client.ModTexturesRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ModContents {
	public final ModContentRegistry<Item> ITEMS = new ModContentRegistry<>();
	public final ModContentRegistry<Block> BLOCKS = new ModContentRegistry<>();

	public final ModTexturesRegistry TERRAIN = new ModTexturesRegistry("block");
	public final ModTexturesRegistry GUI_ITEMS = new ModTexturesRegistry("item");
}
