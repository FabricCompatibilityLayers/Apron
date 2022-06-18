/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.util.Objects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.item.Item;

import io.github.betterthanupdates.babricated.api.BabricatedApi;
import io.github.betterthanupdates.babricated.impl.client.ClientUtil;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class MinecraftForgeClient {
	private static final ICustomItemRenderer[] customItemRenderers = new ICustomItemRenderer[Item.byId.length];

	// Babricated
	private static final ClientUtil BAPI = (ClientUtil) BabricatedApi.getInstance();

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
		Objects.requireNonNull(BAPI.getTextureManager()).getTextureId(texture);
	}

	public static void renderBlock(BlockRenderer blockRenderer, Block block, int x, int y, int z) {
		ForgeHooksClient.beforeBlockRender(block, blockRenderer);
		blockRenderer.render(block, x, y, z);
		ForgeHooksClient.afterBlockRender(block, blockRenderer);
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
