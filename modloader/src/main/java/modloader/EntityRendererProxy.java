package modloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

public class EntityRendererProxy extends GameRenderer {
	private Minecraft game;

	public EntityRendererProxy(Minecraft minecraft) {
		super(minecraft);
		this.game = minecraft;
	}

	public void onFrameUpdate(float f1) {
		super.onFrameUpdate(f1);
		ModLoader.OnTick(this.game);
	}
}
