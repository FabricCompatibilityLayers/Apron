/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import modloader.ModLoader;
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
	static HashMap<List<?>, Tessellator> tessellators = new HashMap<>();
	static HashMap<String, Integer> textures = new HashMap<>();
	static boolean inWorld = false;
	static HashSet<List<?>> renderTextureTest = new HashSet<>();
	static ArrayList<List<Integer>> renderTextureList = new ArrayList<>();
	static int renderPass = -1;

	public ForgeHooksClient() {
	}

	public static boolean onBlockHighlight(WorldEventRenderer renderglobal, PlayerEntity player, HitResult mop, int i, ItemStack itemstack, float f) {
		for(forge.IHighlightHandler handler : highlightHandlers) {
			if (handler.onBlockHighlight(renderglobal, player, mop, i, itemstack, f)) {
				return true;
			}
		}

		return false;
	}

	public static boolean canRenderInPass(Block block, int pass) {
		if (block instanceof IMultipassRender) {
			IMultipassRender impr = (IMultipassRender)block;
			return impr.canRenderInPass(pass);
		} else {
			return pass == block.getRenderPass();
		}
	}

	protected static void bindTessellator(int tex, int sub) {
		List<Integer> key = Arrays.asList(tex, sub);
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
			t.setOffset(
					ForgeClientReflection.Tessellator$firstInstance.xOffset,
					ForgeClientReflection.Tessellator$firstInstance.yOffset,
					ForgeClientReflection.Tessellator$firstInstance.zOffset);
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
			Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;
			GL11.glBindTexture(3553, n);
		} else {
			bindTessellator(n, sub);
		}
	}

	protected static void unbindTexture() {
		Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;
		if (!inWorld) {
			GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
		}
	}

	public static void beforeRenderPass(int pass) {
		renderPass = pass;
		Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;
		ForgeClientReflection.Tessellator$renderingWorldRenderer = true;
		GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
		renderTextureTest.clear();
		renderTextureList.clear();
		inWorld = true;
	}

	public static void afterRenderPass(int pass) {
		renderPass = -1;
		inWorld = false;

		for(List<Integer> l : renderTextureList) {
			Integer[] tn = l.toArray(new Integer[0]);
			GL11.glBindTexture(3553, tn[0]);
			Tessellator t = tessellators.get(l);
			t.tessellate();
		}

		GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
		Tessellator.INSTANCE = ForgeClientReflection.Tessellator$firstInstance;
		ForgeClientReflection.Tessellator$renderingWorldRenderer = false;
	}

	public static void beforeBlockRender(Block block, BlockRenderer renderblocks) {
		if (block instanceof ITextureProvider && renderblocks.textureOverride == -1) {
			ITextureProvider itp = (ITextureProvider)block;
			bindTexture(itp.getTextureFile(), 0);
		}

	}

	public static void afterBlockRender(Block block, BlockRenderer renderblocks) {
		if (block instanceof ITextureProvider && renderblocks.textureOverride == -1) {
			unbindTexture();
		}

	}

	public static void overrideTexture(Object o) {
		if (o instanceof ITextureProvider) {
			GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId(((ITextureProvider)o).getTextureFile()));
		}

	}
}
