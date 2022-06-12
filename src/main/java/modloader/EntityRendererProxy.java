package modloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

public class EntityRendererProxy extends GameRenderer {
    private final Minecraft game;

    public EntityRendererProxy(Minecraft client) {
        super(client);
        this.game = client;
    }

    @Override
    public void method_1844(float f1) {
        super.method_1844(f1);
        ModLoader.OnTick(this.game);
    }
}
