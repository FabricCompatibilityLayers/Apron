/**
 * Minecraft Forge Public Licence
 *
 * ==============================
 *
 *
 * Version 1.1
 *
 *
 * 0. Definitions
 *
 * --------------
 *
 *
 * Minecraft: Denotes a copy of the Minecraft game licensed by Mojang AB
 *
 *
 * User: Anybody that interact with the software in one of the following ways:
 *
 *    - play
 *
 *    - decompile
 *
 *    - recompile or compile
 *
 *    - modify
 *
 *
 * Minecraft Forge: The Minecraft Forge code, in source form, class file form, as
 *
 * obtained in a standalone fashion or as part of a wider distribution.
 *
 *
 * Dependency: Code required to have Minecraft Forge working properly. That can
 *
 * include dependencies required to compile the code as well as modifications in
 *
 * the Minecraft sources that are required to have Minecraft Forge working.
 *
 *
 * 1. Scope
 *
 * --------
 *
 *
 * The present license is granted to any user of Minecraft Forge. As a
 *
 * prerequisite, a user of Minecraft Forge must own a legally aquired copy of
 *
 * Minecraft
 *
 *
 * 2. Play rights
 *
 * --------------
 *
 *
 * The user of Minecraft Forge is allowed to install the software on a client or
 *
 * a server and to play it without restriction.
 *
 *
 * 3. Modification rights
 *
 * ----------------------
 *
 *
 * The user has the right to decompile the source code, look at either the
 *
 * decompiled version or the original source code, and to modify it.
 *
 *
 * 4. Derivation rights
 *
 * --------------------
 *
 *
 * The user has the rights to derive code from Minecraft Forge, that is to say to
 *
 * write code that either extends Minecraft Forge class and interfaces,
 *
 * instantiate the objects declared or calls the functions. This code is known as
 *
 * "derived" code, and can be licensed with conditions different from Minecraft
 *
 * Forge.
 *
 *
 *
 * 5. Distribution rights
 *
 * ----------------------
 *
 *
 * The user of Minecraft Forge is allowed to redistribute Minecraft Forge in
 *
 * partially, in totality, or included in a distribution. When distributing
 *
 * binaries or class files, the user must provide means to obtain the sources of
 *
 * the distributed version of Minecraft Forge at no costs. This includes the
 *
 * files as well as any dependency that the code may rely on, including patches to
 *
 * minecraft original sources.
 *
 *
 * Modification of Minecraft Forge as well as dependencies, including patches to
 *
 * minecraft original sources, has to remain under the terms of the present
 *
 * license.
 */
package forge;

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
        for(forge.IHighlightHandler handler : highlightHandlers) {
            if (handler.onBlockHighlight(renderGlobal, player, mop, i, itemstack, f)) {
                return true;
            }
        }

        return false;
    }

    public static boolean canRenderInPass(Block block, int pass) {
        if (block instanceof IMultipassRender) {
            IMultipassRender iMultipassRender = (IMultipassRender)block;
            return iMultipassRender.canRenderInPass(pass);
        } else {
            return pass == block.getRenderPass();
        }
    }

    protected static void bindTessellator(int tex, int sub) {
        List key = Arrays.asList(tex, sub);
        Tessellator t;
        if (!tessellators.containsKey(key)) {
            t = new Tessellator();
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
        Tessellator.renderingWorldRenderer = true;
        GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
        renderTextureTest.clear();
        renderTextureList.clear();
        inWorld = true;
    }

    public static void afterRenderPass(int pass) {
        renderPass = -1;
        inWorld = false;

        for(List l : renderTextureList) {
            Integer[] tn = (Integer[])l.toArray();
            GL11.glBindTexture(3553, tn[0]);
            Tessellator t = tessellators.get(l);
            t.draw();
        }

        GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
        Tessellator.renderingWorldRenderer = false;
    }

    public static void beforeBlockRender(Block block, BlockRenderer renderBlocks) {
        if (block instanceof ITextureProvider && renderBlocks.textureOverride == -1) {
            ITextureProvider itp = (ITextureProvider)block;
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
            GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId(((ITextureProvider)o).getTextureFile()));
        }
    }

    public static void renderCustomItem(ICustomItemRenderer customRenderer, BlockRenderer renderBlocks, int itemID, int meta, float f) {
        Tessellator tessellator = Tessellator.INSTANCE;
        if (renderBlocks.field_81) {
            int j = 16777215;
            float f1 = (float)(j >> 16 & 0xFF) / 255.0F;
            float f3 = (float)(j >> 8 & 0xFF) / 255.0F;
            float f5 = (float)(j & 0xFF) / 255.0F;
            GL11.glColor4f(f1 * f, f3 * f, f5 * f, 1.0F);
        }

        customRenderer.renderInventory(renderBlocks, itemID, meta);
    }
}
