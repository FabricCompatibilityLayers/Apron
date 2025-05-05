package reforged;

import net.minecraft.client.render.WorldRenderer;

public interface IRenderWorldLastHandler {
	/** Called after rendering all the 3D data of the world.  This is
	 * called before the user's tool is rendered, but otherwise after all
	 * 3D content.  It is called twice in anaglyph mode.  This is intended
	 * for rendering visual effect overlays into the world.
	 */
	void onRenderWorldLast(WorldRenderer wr, float f);
}
