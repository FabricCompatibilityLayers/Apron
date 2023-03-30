package io.github.betterthanupdates.forge.item;

import java.util.List;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ToolEffectiveness {
	public int blockId;
	public int meta;
	public String toolType;

	public ToolEffectiveness(final int blockId, final int meta, final String toolType) {
		this.blockId = blockId;
		this.meta = meta;
		this.toolType = toolType;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(final Object other) {
		int blockId;
		int meta;
		String toolClass;

		if (other instanceof ToolEffectiveness) {
			ToolEffectiveness effectiveness = (ToolEffectiveness) other;
			blockId = effectiveness.blockId;
			meta = effectiveness.meta;
			toolClass = effectiveness.toolType;
		} else if (other instanceof List) {
			List list = (List) other;
			blockId = (int) list.get(0);
			meta = (int) list.get(1);
			toolClass = (String) list.get(2);
		} else {
			return false;
		}

		return blockId == this.blockId && meta == this.meta && toolClass.equals(this.toolType);
	}
}
