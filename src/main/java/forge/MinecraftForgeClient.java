/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import modloader.ModLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.item.Item;

public class MinecraftForgeClient {
	private static final ICustomItemRenderer[] customItemRenderers = new ICustomItemRenderer[Item.byId.length];

	public MinecraftForgeClient() {
	}

	public static void registerHighlightHandler(IHighlightHandler handler) {
		ForgeHooksClient.highlightHandlers.add(handler);
	}

	public static void bindTexture(String name, int sub) {
		ForgeHooksClient.bindTexture(name, sub);
	}

	public static void bindTexture(String name) {
		ForgeHooksClient.bindTexture(name, 0);
	}

	public static void unbindTexture() {
		ForgeHooksClient.unbindTexture();
	}

	public static void preloadTexture(String texture) {
		ModLoader.getMinecraftInstance().textureManager.getTextureId(texture);
	}

	public static void renderBlock(BlockRenderer rb, Block bl, int i, int j, int k) {
		ForgeHooksClient.beforeBlockRender(bl, rb);
		rb.render(bl, i, j, k);
		ForgeHooksClient.afterBlockRender(bl, rb);
	}

	public static int getRenderPass() {
		return ForgeHooksClient.renderPass;
	}

	public static void registerCustomItemRenderer(int itemID, ICustomItemRenderer renderer) {
		customItemRenderers[itemID] = renderer;
	}

	public static ICustomItemRenderer getCustomItemRenderer(int itemID) {
		return customItemRenderers[itemID];
	}
}
