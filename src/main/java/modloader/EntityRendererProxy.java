package modloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

@Deprecated
public class EntityRendererProxy extends GameRenderer {
    public EntityRendererProxy(Minecraft client) {
        super(client);
    }
}
