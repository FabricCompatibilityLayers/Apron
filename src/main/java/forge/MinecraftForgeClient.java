/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.item.Item;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Legacy
public class MinecraftForgeClient {
	@Legacy
	private static ICustomItemRenderer[] customItemRenderers = new ICustomItemRenderer[Item.byId.length];

	@Legacy
	public MinecraftForgeClient() {
	}

	@Legacy
	public static void registerHighlightHandler(IHighlightHandler handler) {
		ForgeHooksClient.highlightHandlers.add(handler);
	}

	@Legacy
	public static void bindTexture(String name, int sub) {
		ForgeHooksClient.bindTexture(name, sub);
	}

	@Legacy
	public static void bindTexture(String name) {
		ForgeHooksClient.bindTexture(name, 0);
	}

	@Legacy
	public static void unbindTexture() {
		ForgeHooksClient.unbindTexture();
	}

	@Legacy
	public static void preloadTexture(String texture) {
		ModLoader.getMinecraftInstance().textureManager.getTextureId(texture);
	}

	@Legacy
	public static void renderBlock(BlockRenderer rb, Block bl, int i, int j, int k) {
		ForgeHooksClient.beforeBlockRender(bl, rb);
		rb.render(bl, i, j, k);
		ForgeHooksClient.afterBlockRender(bl, rb);
	}

	@Legacy
	public static int getRenderPass() {
		return ForgeHooksClient.renderPass;
	}

	@Legacy
	public static void registerCustomItemRenderer(int itemID, ICustomItemRenderer renderer) {
		customItemRenderers[itemID] = renderer;
	}

	@Legacy
	public static ICustomItemRenderer getCustomItemRenderer(int itemID) {
		return customItemRenderers[itemID];
	}
}
