package reforged;

import java.util.LinkedList;

import net.minecraft.client.render.WorldRenderer;

public class ReforgedHooksClient {

	public static LinkedList<IRenderWorldLastHandler> renderWorldLastHandlers = new LinkedList<>();

	public static void onRenderWorldLast(WorldRenderer wr, float f) {
		for (IRenderWorldLastHandler handler : renderWorldLastHandlers) {
			handler.onRenderWorldLast(wr, f);
		}
	}

}
