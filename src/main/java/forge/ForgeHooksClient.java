/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import io.github.betterthanupdates.forge.ForgeClientReflection;
import modloader.ModLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ForgeHooksClient {
	static LinkedList<IHighlightHandler> highlightHandlers = new LinkedList<>();
	static HashMap<List, Tessellator> tessellators = new HashMap<>();
	static HashMap<String, Integer> textures = new HashMap<>();
	static boolean inWorld = false;
	static HashSet<List> renderTextureTest = new HashSet<>();
	static ArrayList<List> renderTextureList = new ArrayList<>();
	static Tessellator defaultTessellator = null;
	static int renderPass = -1;

	public ForgeHooksClient() {
	}

	public static boolean onBlockHighlight(WorldRenderer renderGlobal, PlayerEntity player, HitResult mop, int i, ItemStack itemstack, float f) {
		for (forge.IHighlightHandler handler : highlightHandlers) {
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
		List key = Arrays.asList(tex, sub);
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
		int n;
		if (!textures.containsKey(name)) {
			n = ModLoader.getMinecraftInstance().textureManager.getTextureId(name);
			textures.put(name, n);
		} else {
			n = textures.get(name);
		}

		if (!inWorld) {
			if (Tessellator.INSTANCE.drawing) {
				int mode = Tessellator.INSTANCE.drawingMode;
				Tessellator.INSTANCE.draw();
				Tessellator.INSTANCE.start(mode);
			}

			GL11.glBindTexture(3553, n);
		} else {
			bindTessellator(n, sub);
		}
	}

	protected static void unbindTexture() {
		if (inWorld) {
			Tessellator.INSTANCE = defaultTessellator;
		} else {
			if (Tessellator.INSTANCE.drawing) {
				int mode = Tessellator.INSTANCE.drawingMode;
				Tessellator.INSTANCE.draw();
				Tessellator.INSTANCE.start(mode);
			}

			GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
		}
	}

	public static void beforeRenderPass(int pass) {
		renderPass = pass;
		defaultTessellator = Tessellator.INSTANCE;
		ForgeClientReflection.Tessellator$renderingWorldRenderer = true;
		GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
		renderTextureTest.clear();
		renderTextureList.clear();
		inWorld = true;
	}

	public static void afterRenderPass(int pass) {
		renderPass = -1;
		inWorld = false;

		for (List l : renderTextureList) {
			Integer[] tn = (Integer[]) l.toArray();
			GL11.glBindTexture(3553, tn[0]);
			Tessellator t = tessellators.get(l);
			t.draw();
		}

		GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
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
			GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId(((ITextureProvider) o).getTextureFile()));
		}
	}

	public static void renderCustomItem(ICustomItemRenderer customRenderer, BlockRenderer renderBlocks, int itemID, int meta, float f) {
		Tessellator tessellator = Tessellator.INSTANCE;
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
