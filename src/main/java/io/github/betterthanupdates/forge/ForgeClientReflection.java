package io.github.betterthanupdates.forge;

import net.minecraft.client.render.Tessellator;

public class ForgeClientReflection {
	public static boolean Tessellator$renderingWorldRenderer = false;
	public static Tessellator Tessellator$firstInstance;

	public static boolean BlockRenderer$cfgGrassFix = true;
	public static float[][] BlockRenderer$redstoneColors = new float[16][];

	public static void BlockRenderer$setRedstoneColors(float[][] af) {
		if (af.length != 16) {
			throw new IllegalArgumentException("Must be 16 colors.");
		} else {
			for (int i = 0; i < af.length; ++i) {
				if (af[i].length != 3) {
					throw new IllegalArgumentException("Must be 3 channels in a color.");
				}
			}

			BlockRenderer$redstoneColors = af;
		}
	}
}
