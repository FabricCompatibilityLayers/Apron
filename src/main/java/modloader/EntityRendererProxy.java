package modloader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

import io.github.betterthanupdates.Legacy;

@Legacy
@Environment(EnvType.CLIENT)
public class EntityRendererProxy extends GameRenderer {
	private final Minecraft game;

	public EntityRendererProxy(Minecraft client) {
		super(client);
		this.game = client;
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);
		ModLoader.OnTick(this.game);
	}
}
