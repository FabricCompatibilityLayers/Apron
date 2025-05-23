/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.item.Item;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;
import io.github.betterthanupdates.apron.impl.client.ApronClientImpl;
import io.github.betterthanupdates.stapi.StAPIMinecraft;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Legacy
public class MinecraftForgeClient {
	private static final ICustomItemRenderer[] CUSTOM_ITEM_RENDERERS = new ICustomItemRenderer[Item.ITEMS.length];

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
		if (FabricLoader.getInstance().isModLoaded("stationapi")) {
			((StAPIMinecraft) ApronApi.getInstance().getGame()).apron$stapi$preloadTexture(texture);
		} else {
			((ApronClientImpl) ApronApi.getInstance()).getTextureManager().getTextureId(texture);
		}
	}

	public static void renderBlock(BlockRenderManager blockRenderer, Block block, int x, int y, int z) {
		ForgeHooksClient.beforeBlockRender(block, blockRenderer);
		blockRenderer.render(block, x, y, z);
		ForgeHooksClient.afterBlockRender(block, blockRenderer);
	}

	public static int getRenderPass() {
		return ForgeHooksClient.renderPass;
	}

	public static void registerCustomItemRenderer(int itemID, ICustomItemRenderer renderer) {
		CUSTOM_ITEM_RENDERERS[itemID] = renderer;
	}

	public static ICustomItemRenderer getCustomItemRenderer(int itemID) {
		return CUSTOM_ITEM_RENDERERS[itemID];
	}
}
