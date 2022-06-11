package forge;

import net.minecraft.client.render.block.BlockRenderer;

public interface ICustomItemRenderer {
    void renderInventory(BlockRenderer arg, int i, int j);
}
