/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldEventRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

import io.github.betterthanupdates.apron.impl.client.ApronClientImpl;
import io.github.betterthanupdates.forge.ForgeClientReflection;
import io.github.betterthanupdates.forge.client.render.ForgeTessellator;

@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
@Environment(EnvType.CLIENT)
public class ForgeHooksClient {
	static LinkedList<IHighlightHandler> highlightHandlers = new LinkedList<>();
	static HashMap<Pair<Integer, Integer>, Tessellator> tessellators = new HashMap<>();
	static HashMap<String, Integer> textures = new HashMap<>();
	static boolean inWorld = false;
	/**
	 * Set of tex/sub pairs.
	 */
	static HashSet<Pair<Integer, Integer>> renderTextureTest = new HashSet<>();
	/**
	 * List of tex/sub pairs.
	 */
	static ArrayList<Pair<Integer, Integer>> renderTextureList = new ArrayList<>();
	static Tessellator defaultTessellator = null;
	static int renderPass = -1;

	// Apron
	@NotNull
	private static final ApronClientImpl BAPI = ApronClientImpl.instance;
	@NotNull
	private static final TextureManager textureManager = Objects.requireNonNull(BAPI.getTextureManager());

	public ForgeHooksClient() {
	}

	public static boolean onBlockHighlight(WorldEventRenderer renderGlobal, PlayerEntity player, HitResult mop, int i, ItemStack itemstack, float f) {
		for (IHighlightHandler handler : highlightHandlers) {
			if (handler.onBlockHighlight(renderGlobal, player, mop, i, itemstack, f)) {
				return true;
			}
		}

		return false;
	}

	public static boolean canRenderInPass(Block block, int pass) {
		if (block instanceof IMultipassRender) {
			IMultipassRender iMultipassRender = (IMultipassRender) block;
			return iMultipassRender.canRenderInPass(pass);
		} else {
			return pass == block.getRenderPass();
		}
	}

	protected static void bindTessellator(int tex, int sub) {
		Pair<Integer, Integer> key = Pair.of(tex, sub);
		Tessellator t;

		if (!tessellators.containsKey(key)) {
			t = new Tessellator(2097152);
			tessellators.put(key, t);
		} else {
			t = tessellators.get(key);
		}

		if (inWorld && !renderTextureTest.contains(key)) {
			renderTextureTest.add(key);
			renderTextureList.add(key);
			t.start();
			t.setOffset(defaultTessellator.xOffset, defaultTessellator.yOffset, defaultTessellator.zOffset);
		}

		Tessellator.INSTANCE = t;
	}

	protected static void bindTexture(String name, int sub) {
		int textureId;

		if (textures.containsKey(name)) {
			textureId = textures.get(name);
		} else {
			textureId = textureManager.getTextureId(name);
			textures.put(name, textureId);
		}

		if (!inWorld) {
			if (((ForgeTessellator) Tessellator.INSTANCE).isTessellating()) {
				int mode = Tessellator.INSTANCE.drawingMode;
				Tessellator.INSTANCE.tessellate();
				Tessellator.INSTANCE.start(mode);
			}

			GL11.glBindTexture(3553, textureId);
		} else {
			bindTessellator(textureId, sub);
		}
	}

	protected static void unbindTexture() {
		if (inWorld) {
			Tessellator.INSTANCE = defaultTessellator;
		} else {
			if (((ForgeTessellator) Tessellator.INSTANCE).isTessellating()) {
				int mode = Tessellator.INSTANCE.drawingMode;
				Tessellator.INSTANCE.tessellate();
				Tessellator.INSTANCE.start(mode);
			}

			GL11.glBindTexture(3553, textureManager.getTextureId("/terrain.png"));
		}
	}

	public static void beforeRenderPass(int pass) {
		renderPass = pass;
		defaultTessellator = Tessellator.INSTANCE;
		ForgeClientReflection.Tessellator$renderingWorldRenderer = true;
		GL11.glBindTexture(3553, textureManager.getTextureId("/terrain.png"));
		renderTextureTest.clear();
		renderTextureList.clear();
		inWorld = true;
	}

	public static void afterRenderPass(int pass) {
		renderPass = -1;
		inWorld = false;

		for (Pair<Integer, Integer> l : renderTextureList) {
			GL11.glBindTexture(3553, l.first());
			Tessellator tessellator = tessellators.get(l);
			tessellator.tessellate();
		}

		GL11.glBindTexture(3553, textureManager.getTextureId("/terrain.png"));
		ForgeClientReflection.Tessellator$renderingWorldRenderer = false;
	}

	public static void beforeBlockRender(Block block, BlockRenderer renderBlocks) {
		if (block instanceof ITextureProvider && renderBlocks.textureOverride == -1) {
			ITextureProvider itp = (ITextureProvider) block;
			bindTexture(itp.getTextureFile(), 0);
		}
	}

	public static void afterBlockRender(Block block, BlockRenderer renderBlocks) {
		if (block instanceof ITextureProvider && renderBlocks.textureOverride == -1) {
			unbindTexture();
		}
	}

	public static void overrideTexture(Object o) {
		if (o instanceof ITextureProvider) {
			GL11.glBindTexture(3553, textureManager.getTextureId(((ITextureProvider) o).getTextureFile()));
		}
	}

	public static void renderCustomItem(ICustomItemRenderer customRenderer, BlockRenderer renderBlocks, int itemID, int meta, float f) {
		if (renderBlocks.field_81) {
			int j = 16777215;
			float f1 = (float) (j >> 16 & 0xFF) / 255.0F;
			float f3 = (float) (j >> 8 & 0xFF) / 255.0F;
			float f5 = (float) (j & 0xFF) / 255.0F;
			GL11.glColor4f(f1 * f, f3 * f, f5 * f, 1.0F);
		}

		customRenderer.renderInventory(renderBlocks, itemID, meta);
	}
}
