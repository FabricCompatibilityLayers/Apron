package io.github.betterthanupdates.forge.item;

import java.util.List;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ForgeTool {
	public String toolType;
	public int harvestLevel;

	public ForgeTool(final String toolType, final int harvestLevel) {
		this.toolType = toolType;
		this.harvestLevel = harvestLevel;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(final Object other) {
		String toolClass;
		int harvestLevel;

		if (other instanceof ForgeTool) {
			ForgeTool tool = (ForgeTool) other;
			toolClass = tool.toolType;
			harvestLevel = tool.harvestLevel;
		} else if (other instanceof List) {
			List list = (List) other;
			toolClass = (String) list.get(0);
			harvestLevel = (int) list.get(1);
		} else {
			return false;
		}

		return this.toolType.equals(toolClass) && this.harvestLevel == harvestLevel;
	}
}
