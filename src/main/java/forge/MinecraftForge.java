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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MinecraftForge {
    public static final Logger LOGGER = LogManager.getLogger(MinecraftForge.class);

    private static LinkedList<IBucketHandler> bucketHandlers;
    
    @Deprecated
    public static void registerCustomBucketHander(final IBucketHandler handler) {
        registerCustomBucketHandler(handler);
    }
    
    public static void registerCustomBucketHandler(final IBucketHandler handler) {
        MinecraftForge.bucketHandlers.add(handler);
    }
    
    public static void registerSleepHandler(final ISleepHandler handler) {
        ForgeHooks.sleepHandlers.add(handler);
    }
    
    public static void registerDestroyToolHandler(final IDestroyToolHandler handler) {
        ForgeHooks.destroyToolHandlers.add(handler);
    }
    
    public static void registerCraftingHandler(final ICraftingHandler handler) {
        ForgeHooks.craftingHandlers.add(handler);
    }
    
    public static ItemStack fillCustomBucket(final World world, final int i, final int j, final int k) {
        for (final IBucketHandler handler : MinecraftForge.bucketHandlers) {
            final ItemStack stack = handler.fillCustomBucket(world, i, j, k);
            if (stack != null) {
                return stack;
            }
        }
        return null;
    }
    
    public static void setToolClass(final Item tool, final String tClass, final int hLevel) {
        ForgeHooks.initTools();
        ForgeHooks.toolClasses.put(tool.id, Arrays.asList(tClass, hLevel));
    }
    
    public static void setBlockHarvestLevel(final Block bl, final int md, final String tClass, final int hLevel) {
        ForgeHooks.initTools();
        final List<Object> key = Arrays.asList(bl.id, md, tClass);
        ForgeHooks.toolHarvestLevels.put(key, hLevel);
        ForgeHooks.toolEffectiveness.add(key);
    }
    
    public static void removeBlockEffectiveness(final Block bl, final int md, final String tClass) {
        ForgeHooks.initTools();
        final List<Object> key = Arrays.asList(bl.id, md, tClass);
        ForgeHooks.toolEffectiveness.remove(key);
    }
    
    public static void setBlockHarvestLevel(final Block bl, final String tClass, final int hLevel) {
        ForgeHooks.initTools();
        for (int md = 0; md < 16; ++md) {
            final List<Object> key = Arrays.asList(bl.id, md, tClass);
            ForgeHooks.toolHarvestLevels.put(key, hLevel);
            ForgeHooks.toolEffectiveness.add(key);
        }
    }
    
    public static void removeBlockEffectiveness(final Block bl, final String tClass) {
        ForgeHooks.initTools();
        for (int md = 0; md < 16; ++md) {
            final List<Object> key = Arrays.asList(bl.id, md, tClass);
            ForgeHooks.toolEffectiveness.remove(key);
        }
    }
    
    public static void addPickaxeBlockEffectiveAgainst(final Block block) {
        setBlockHarvestLevel(block, "pickaxe", 0);
    }
    
    public static void killMinecraft(final String modName, final String msg) {
        throw new RuntimeException(modName + ": " + msg);
    }
    
    public static void versionDetect(final String modName, final int major, final int minor, final int revision) {
        if (major != 1) {
            killMinecraft(modName, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
        }
        else if (minor != 0) {
            if (minor > 0) {
                killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
            }
            else {
                LOGGER.info(modName + ": MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x, may lead to unexpected behavior");
            }
        }
        else if (revision > 6) {
            killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
        }
    }
    
    public static void versionDetectStrict(final String modName, final int major, final int minor, final int revision) {
        if (major != 1) {
            killMinecraft(modName, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
        }
        else if (minor != 0) {
            if (minor > 0) {
                killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
            }
            else {
                killMinecraft(modName, "MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x");
            }
        }
        else if (revision > 6) {
            killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
        }
    }
    
    static {
        MinecraftForge.bucketHandlers = new LinkedList<>();
    }
}
