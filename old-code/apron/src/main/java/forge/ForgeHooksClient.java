/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import io.github.betterthanupdates.apron.impl.IntegerPair;
import itemspriteapi.IItemTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;
import io.github.betterthanupdates.apron.impl.client.ApronClientImpl;
import io.github.betterthanupdates.forge.ForgeClientReflection;

@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
@Environment(EnvType.CLIENT)
@Legacy
public class ForgeHooksClient {
	static LinkedList<IHighlightHandler> highlightHandlers = new LinkedList<>();
	static HashMap<IntegerPair, Tessellator> tessellators = new HashMap<>();
	static HashMap<String, Integer> textures = new HashMap<>();
	static boolean inWorld = false;
	/**
	 * Set of tex/sub pairs.
	 */
	static HashSet<IntegerPair> renderTextureTest = new HashSet<>();
	/**
	 * List of tex/sub pairs.
	 */
	static ArrayList<IntegerPair> renderTextureList = new ArrayList<>();
	static int renderPass = -1;

	public ForgeHooksClient() {
	}

	public static boolean onBlockHighlight(WorldRenderer worldEventRenderer, PlayerEntity player, HitResult hitResult, int i, ItemStack itemStack, float f) {
		for (IHighlightHandler handler : highlightHandlers) {
			if (handler.onBlockHighlight(worldEventRenderer, player, hitResult, i, itemStack, f)) {
				return true;
			}
		}

		return false;
	}

	public static boolean canRenderInPass(Block block, int pass) {
		if (block instanceof IMultipassRender) {
			IMultipassRender multipassRender = (IMultipassRender) block;
			return multipassRender.canRenderInPass(pass);
		} else {
			return pass == block.getRenderLayer();
		}
	}

	protected static void bindTessellator(int textureId, int sub) {
		IntegerPair key = new IntegerPair(textureId, sub);
		Tessellator tessellator;

		if (!tessellators.containsKey(key)) {
			tessellator = new Tessellator(2097152);
			tessellators.put(key, tessellator);
		} else {
			tessellator = tessellators.get(key);
		}

		if (inWorld && !renderTextureTest.contains(key)) {
			renderTextureTest.add(key);
			renderTextureList.add(key);
			tessellator.startQuads();
			tessellator.translate(
					ForgeClientReflection.Tessellator$firstInstance.xOffset,
					ForgeClientReflection.Tessellator$firstInstance.yOffset,
					ForgeClientReflection.Tessellator$firstInstance.zOffset);
		}

		Tessellator.INSTANCE = tessellator;
	}

	protected static void bindTexture(String name, int sub) {
		int textureId;

		if (!textures.containsKey(name)) {
			textureId = ((ApronClientImpl) ApronApi.getInstance()).getTextureManager().getTextureId(name);
			textures.put(name, textureId);
		} else {
			textureId = textures.get(name);
		}

		if (!inWorld) {
			Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;
			GL11.glBindTexture(3553, textureId);
		} else {
			bindTessellator(textureId, sub);
		}
	}

	protected static void unbindTexture() {
		Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;

		if (!inWorld) {
			GL11.glBindTexture(3553, ((ApronClientImpl) ApronApi.getInstance()).getTextureManager().getTextureId("/terrain.png"));
		}
	}

	public static void beforeRenderPass(int pass) {
		renderPass = pass;
		Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;
		ForgeClientReflection.Tessellator$renderingWorldRenderer = true;
		GL11.glBindTexture(3553, ((ApronClientImpl) ApronApi.getInstance()).getTextureManager().getTextureId("/terrain.png"));
		renderTextureTest.clear();
		renderTextureList.clear();
		inWorld = true;
	}

	public static void afterRenderPass(int pass) {
		renderPass = -1;
		inWorld = false;

		for (IntegerPair l : renderTextureList) {
			GL11.glBindTexture(3553, l.first());
			Tessellator tessellator = tessellators.get(l);
			tessellator.draw();
		}

		GL11.glBindTexture(3553, ((ApronClientImpl) ApronApi.getInstance()).getTextureManager().getTextureId("/terrain.png"));
		Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;
		ForgeClientReflection.Tessellator$renderingWorldRenderer = false;
	}

	public static void beforeBlockRender(Block block, BlockRenderManager blockRenderer) {
		if (block instanceof ITextureProvider && blockRenderer.textureOverride == -1) {
			ITextureProvider textureProvider = (ITextureProvider) block;
			bindTexture(textureProvider.getTextureFile(), 0);
		}
	}

	public static void afterBlockRender(Block block, BlockRenderManager blockRenderer) {
		if (block instanceof ITextureProvider && blockRenderer.textureOverride == -1) {
			unbindTexture();
		}
	}

	public static void overrideTexture(Object o) {
		String textureFile = "";

		if (o instanceof ITextureProvider) { // Forge hook
			textureFile = ((ITextureProvider) o).getTextureFile();
		} else if (o instanceof IItemTexture) { // ItemSpriteAPI hook
			textureFile = ((IItemTexture) o).getTextureFile();
		}

		if (!textureFile.isEmpty()) {
			GL11.glBindTexture(3553, ((ApronClientImpl) ApronApi.getInstance()).getTextureManager().getTextureId(textureFile));
		}
	}

	// Not included in Reforged for unknown reasons
	public static void renderCustomItem(ICustomItemRenderer customRenderer, BlockRenderManager blockRenderer, int itemID, int meta, float f) {
		if (blockRenderer.inventoryColorEnabled) {
			int j = 16777215;
			float f1 = (float) (j >> 16 & 0xFF) / 255.0F;
			float f3 = (float) (j >> 8 & 0xFF) / 255.0F;
			float f5 = (float) (j & 0xFF) / 255.0F;
			GL11.glColor4f(f1 * f, f3 * f, f5 * f, 1.0F);
		}

		customRenderer.renderInventory(blockRenderer, itemID, meta);
	}
}
