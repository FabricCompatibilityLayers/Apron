package modloader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

@Environment(EnvType.CLIENT)
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
