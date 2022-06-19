package io.github.betterthanupdates.shockahpi;

import net.minecraft.item.ToolMaterial;

public class ShockAhPIToolItem {
	public static float getToolPower(ToolMaterial material) {
		if (material == ToolMaterial.DIAMOND) {
			return 80.0F;
		} else if (material == ToolMaterial.IRON) {
			return 60.0F;
		} else {
			return material == ToolMaterial.STONE ? 40.0F : 20.0F;
		}
	}
}
