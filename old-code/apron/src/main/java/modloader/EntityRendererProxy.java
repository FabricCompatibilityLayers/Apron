package modloader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.class_555;
import net.minecraft.client.Minecraft;

import io.github.betterthanupdates.Legacy;

@Legacy
@Environment(EnvType.CLIENT)
public class EntityRendererProxy extends class_555 {
	private final Minecraft game;

	public EntityRendererProxy(Minecraft client) {
		super(client);
		this.game = client;
	}

	@Override
	public void method_1844(float delta) {
		super.method_1844(delta);
		ModLoader.OnTick(this.game);
	}
}
