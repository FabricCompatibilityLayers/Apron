package modloader;

import io.github.betterthanupdates.Legacy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

@Legacy
@Environment(EnvType.CLIENT)
public class EntityRendererProxy extends GameRenderer {
	@Legacy
	private final Minecraft game;

	@Legacy
	public EntityRendererProxy(Minecraft client) {
		super(client);
		this.game = client;
	}

	@Legacy
	@Override
	public void tick(float delta) {
		super.tick(delta);
		ModLoader.OnTick(this.game);
	}
}
