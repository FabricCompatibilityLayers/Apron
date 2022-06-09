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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

// TODO do something with this hooks
public class ForgeHooksClient {
    static LinkedList<IHighlightHandler> highlightHandlers;
    static HashMap<List<Object>, Tessellator> tessellators;
    static HashMap<String, Integer> textures;
    static boolean inWorld;
    static HashSet<List<Object>> renderTextureTest;
    static ArrayList<List<Object>> renderTextureList;
    static int renderPass;
    
    public static boolean onBlockHighlight(final WorldRenderer renderglobal, final PlayerEntity player, final HitResult mop, final int i, final ItemStack itemstack, final float f) {
        for (final IHighlightHandler handler : ForgeHooksClient.highlightHandlers) {
            if (handler.onBlockHighlight(renderglobal, player, mop, i, itemstack, f)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canRenderInPass(final Block block, final int pass) {
        if (block instanceof IMultipassRender) {
            final IMultipassRender impr = (IMultipassRender)block;
            return impr.canRenderInPass(pass);
        }
        return pass == block.getRenderPass();
    }
    
    // TODO do something with tesselators
    protected static void bindTessellator(final int tex, final int sub) {
        /*final List key = Arrays.asList((Object[])new Integer[] { tex, sub });
        Tessellator t;
        if (!ForgeHooksClient.tessellators.containsKey(key)) {
            t = new Tessellator();
            ForgeHooksClient.tessellators.put(key, t);
        }
        else {
            t = (Tessellator)ForgeHooksClient.tessellators.get(key);
        }
        if (ForgeHooksClient.inWorld && !ForgeHooksClient.renderTextureTest.contains(key)) {
            ForgeHooksClient.renderTextureTest.add(key);
            ForgeHooksClient.renderTextureList.add(key);
            t.start();
            t.setOffset(Tessellator.firstInstance.xOffset, Tessellator.firstInstance.yOffset, Tessellator.firstInstance.zOffset);
        }
        Tessellator.INSTANCE = t;*/
    }
    
    // TODO manage texture binding
    protected static void bindTexture(final String name, final int sub) {
        /*int n;
        if (!ForgeHooksClient.textures.containsKey(name)) {
            n = ModLoader.getMinecraftInstance().textureManager.getTextureId(name);
            ForgeHooksClient.textures.put(name, n);
        }
        else {
            n = (int)ForgeHooksClient.textures.get(name);
        }
        if (!ForgeHooksClient.inWorld) {
            Tessellator.INSTANCE = Tessellator.firstInstance;
            GL11.glBindTexture(3553, n);
            return;
        }
        bindTessellator(n, sub);*/
    }
    
    // TODO manage textures unbinding
    protected static void unbindTexture() {
        /*Tessellator.INSTANCE = Tessellator.firstInstance;
        if (!ForgeHooksClient.inWorld) {
            GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
        }*/
    }
    
    public static void beforeRenderPass(final int pass) {
        /*ForgeHooksClient.renderPass = pass;
        Tessellator.INSTANCE = Tessellator.firstInstance;
        Tessellator.renderingWorldRenderer = true;
        GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
        ForgeHooksClient.renderTextureTest.clear();
        ForgeHooksClient.renderTextureList.clear();
        ForgeHooksClient.inWorld = true;*/
    }
    
    public static void afterRenderPass(final int pass) {
        /*ForgeHooksClient.renderPass = -1;
        ForgeHooksClient.inWorld = false;
        for (final List l : ForgeHooksClient.renderTextureList) {
            final Integer[] tn = (Integer[])l.toArray();
            GL11.glBindTexture(3553, (int)tn[0]);
            final Tessellator t = (Tessellator)ForgeHooksClient.tessellators.get(l);
            t.draw();
        }
        GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId("/terrain.png"));
        Tessellator.INSTANCE = Tessellator.firstInstance;
        Tessellator.renderingWorldRenderer = false;*/
    }
    
    public static void beforeBlockRender(final Block block, final BlockRenderer renderblocks) {
        /*if (block instanceof ITextureProvider && renderblocks.textureOverride == -1) {
            final ITextureProvider itp = (ITextureProvider)block;
            bindTexture(itp.getTextureFile(), 0);
        }*/
    }
    
    public static void afterBlockRender(final Block block, final BlockRenderer renderblocks) {
        /*if (block instanceof ITextureProvider && renderblocks.textureOverride == -1) {
            unbindTexture();
        }*/
    }
    
    public static void overrideTexture(final Object o) {
        if (o instanceof ITextureProvider) {
            GL11.glBindTexture(3553, ModLoader.getMinecraftInstance().textureManager.getTextureId(((ITextureProvider)o).getTextureFile()));
        }
    }
    
    static {
        ForgeHooksClient.highlightHandlers = new LinkedList<>();
        ForgeHooksClient.tessellators = new HashMap<>();
        ForgeHooksClient.textures = new HashMap<>();
        ForgeHooksClient.inWorld = false;
        ForgeHooksClient.renderTextureTest = new HashSet<>();
        ForgeHooksClient.renderTextureList = new ArrayList<>();
        ForgeHooksClient.renderPass = -1;
    }
}
