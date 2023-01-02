package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.infsprites;

import io.github.betterthanupdates.apron.fixes.vanilla.infsprites.EffectRendererProxy;
import io.github.betterthanupdates.apron.fixes.vanilla.infsprites.ItemRendererProxy;
import io.github.betterthanupdates.apron.fixes.vanilla.infsprites.RenderEngineProxy;
import io.github.betterthanupdates.apron.fixes.vanilla.infsprites.RenderGlobalProxy;
import modloader.BaseMod;
import modloader.ModLoader;
import net.mine_diver.infsprites.Core;
import net.mine_diver.infsprites.api.INamespaceProvider;
import net.mine_diver.infsprites.api.IRenderHook;
import net.mine_diver.infsprites.api.Namespace;
import net.mine_diver.infsprites.api.Patchers;
import io.github.betterthanupdates.apron.fixes.vanilla.infsprites.RenderItemProxy;
import io.github.betterthanupdates.apron.fixes.vanilla.infsprites.TexturePackListProxy;
import net.mine_diver.infsprites.proxy.IRenderEngineHolder;
import net.mine_diver.infsprites.proxy.transform.ProxyTransformer;
import net.mine_diver.infsprites.render.WorldRendererListener;
import net.mine_diver.infsprites.util.RenderManager;
import net.mine_diver.infsprites.util.compatibility.AetherPatcher;
import net.mine_diver.infsprites.util.compatibility.ForgePatcher;
import net.mine_diver.infsprites.util.compatibility.HowManyItemsPatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.TexturePackManager;
import net.minecraft.client.gui.InGameHud;
import net.minecraft.client.gui.screen.container.ContainerScreen;
import net.minecraft.client.render.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.mod_InfSprites;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.utils.Reflection;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

import static net.mine_diver.infsprites.Core.log;

@Pseudo
@Mixin(Core.class)
public abstract class CoreMixin {
	@Shadow(remap = false)
	@Final
	public static DateTimeFormatter dtf;

	@Shadow(remap = false)
	@Final
	private static Field USED_TERRAIN_SPRITES_FIELD;

	@Shadow(remap = false)
	@Final
	private static Field TERRAIN_SPRITES_LEFT_FIELD;

	@Shadow(remap = false)
	@Final
	private static Field USED_ITEM_SPRITES_FIELD;

	@Shadow(remap = false)
	@Final
	private static Field ITEM_SPRITES_LEFT_FIELD;

	@Shadow(remap = false)
	private BaseMod MLCore;

	@Shadow(remap = false)
	private Namespace namespace;

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "usedTerrainSprites"))
	private static String fixUTS(String constant) {
		return "USED_TERRAIN_SPRITES";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "terrainSpritesLeft"))
	private static String fixTSL(String constant) {
		return "terrainSpritesLeft";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "usedItemSprites"))
	private static String fixUIS(String constant) {
		return "USED_ITEM_SPRITES";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "itemSpritesLeft"))
	private static String fixISL(String constant) {
		return "itemSpritesLeft";
	}

	/**
	 * @author ClickNin
	 * @reason Fix field name, use my modified proxy
	 */
	@Overwrite(remap = false)
	public <T extends BaseMod & INamespaceProvider> void preInit(T mod) {
		log("InfSprites 2.0 - Patched by Apron for compatibility :)");
		log("Pre initialization...");
		log("Resizing ModLoader texture arrays...");

		try {
			boolean[] usedTerrainSprites = (boolean[])((boolean[])USED_TERRAIN_SPRITES_FIELD.get((Object)null));
			USED_TERRAIN_SPRITES_FIELD.set((Object)null, Arrays.copyOf(usedTerrainSprites, usedTerrainSprites.length + mod_InfSprites.additionalTerrainTextures));
			TERRAIN_SPRITES_LEFT_FIELD.setInt((Object)null, TERRAIN_SPRITES_LEFT_FIELD.getInt((Object)null) + mod_InfSprites.additionalTerrainTextures);
			boolean[] usedItemSprites = (boolean[])((boolean[])USED_ITEM_SPRITES_FIELD.get((Object)null));
			USED_ITEM_SPRITES_FIELD.set((Object)null, Arrays.copyOf(usedItemSprites, usedItemSprites.length + mod_InfSprites.additionalItemTextures));
			ITEM_SPRITES_LEFT_FIELD.setInt((Object)null, ITEM_SPRITES_LEFT_FIELD.getInt((Object)null) + mod_InfSprites.additionalItemTextures);
		} catch (IllegalAccessException | IllegalArgumentException var6) {
			throw new RuntimeException(var6);
		}

		log("Resized!");
		if (this.MLCore == null) {
			this.MLCore = mod;
		}

		ModLoader.SetInGUIHook(this.MLCore, true, false);
		this.namespace = Namespace.of(mod);
		log("Registering default render hooks...");
		IRenderHook.renderHooks.add(new WorldRendererListener());
		log("Registered!");
		log("Registering default compatibility patchers...");
		//CubicChunks isn't compatible with Apron
		//Therefore this isn't required
		//Patchers.register(this.namespace.id("cubicchunks_patcher"), new CubicChunksPatcher());
		Patchers.register(this.namespace.id("forge_patcher"), new ForgePatcher());
		Patchers.register(this.namespace.id("howmanyitems_patcher"), new HowManyItemsPatcher());
		//IDResolver isn't compatible with Apron
		//Therefore this isn't required
		//Patchers.register(this.namespace.id("idresolver_patcher"), new IDResolverPatcher());
		Patchers.register(this.namespace.id("aether_patcher"), new AetherPatcher());
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		log("Registered!");

		try {
			log("Initializing TexturePackList proxy...");
			TexturePackManager TPLProxy = (TexturePackManager) ProxyTransformer.createProxy(TexturePackListProxy.class, minecraft.texturePackManager, true);
			minecraft.texturePackManager = TPLProxy;
			Reflection.publicField(Reflection.findField(TextureManager.class, new String[]{"field_1256", "textures"})).set(minecraft.textureManager, TPLProxy);
			log("Initialized!");
			log("Initializing RenderItem proxy in GuiContainer...");
			Field itemRendererField = Reflection.publicField(Reflection.findField(ContainerScreen.class, new String[]{"field_1155", "itemRenderer"}));
			itemRendererField.set((Object)null, ProxyTransformer.createProxy(RenderItemProxy.class, itemRendererField.get((Object)null), true));
			log("Initialized!");
			log("Initializing RenderItem proxy in GuiIngame...");
			itemRendererField = Reflection.publicField(Reflection.findField(InGameHud.class, new String[]{"field_2544", "itemRenderer"}));
			itemRendererField.set((Object)null, ProxyTransformer.createProxy(RenderItemProxy.class, itemRendererField.get((Object)null), true));
			log("Initialized!");
		} catch (Exception var5) {
			throw new RuntimeException(var5);
		}

		log("Pre initialization done!");
	}

	/**
	 * @author ClickNin
	 * @reason Use my own modified field
	 */
	@Overwrite(remap = false)
	public void addRenderer(Map<Class<? extends Entity>, EntityRenderer> renders) {
		RenderManager.renders = renders;
		log("Initializing RenderItem proxy for EntityItem render...");
		renders.replace(ItemEntity.class, ProxyTransformer.createProxy(RenderItemProxy.class, renders.get(ItemEntity.class), true));
		log("Initialized!");
		Patchers.patchAll();
	}

	/**
	 * @author ClickNin
	 * @reason Use my own modified field
	 */
	@Overwrite(remap = false)
	public void postInit(Minecraft minecraft) {
		log("Post initialization...");
		log("Initializing RenderGlobal proxy");
		minecraft.worldRenderer = ProxyTransformer.createProxy(RenderGlobalProxy.class, minecraft.worldRenderer, true);
		log("Initialized!");
		log("Initializing EffectRenderer proxy...");
		minecraft.particleManager = ProxyTransformer.createProxy(EffectRendererProxy.class, minecraft.particleManager, true);
		log("Initialized!");
		log("Initializing ItemRenderer proxy in RenderManager...");
		EntityRenderDispatcher.INSTANCE.heldItemRenderer = ProxyTransformer.createProxy(ItemRendererProxy.class, EntityRenderDispatcher.INSTANCE.heldItemRenderer, true);
		log("Initialized!");
		log("Initializing ItemRenderer proxy in EntityRenderer...");
		minecraft.gameRenderer.heldItemRenderer = ProxyTransformer.createProxy(ItemRendererProxy.class, minecraft.gameRenderer.heldItemRenderer, true);
		log("Initialized!");
		log("Initializing RenderEngine proxy...");
		TextureManager renderEngineProxy = ProxyTransformer.createProxy(RenderEngineProxy.class, minecraft.textureManager, true);
		minecraft.textureManager = renderEngineProxy;
		((IRenderEngineHolder) minecraft.particleManager).setRenderEngine(renderEngineProxy);
		((IRenderEngineHolder) minecraft.worldRenderer).setRenderEngine(renderEngineProxy);
		BlockEntityRenderDispatcher.INSTANCE.textureManager = EntityRenderDispatcher.INSTANCE.textureManager = renderEngineProxy;
		log("Initialized!");
		log("Post initialization done!");
	}

	/**
	 * @author ClickNin
	 * @reason Having some fun
	 */
	@Overwrite(remap = false)
	public void init() {
		//log("Initialization...");
		//log("Initialization done!");
		log("Initializin- oh wait, this method does not actually do anything lol");
		log("Initialization done! (Nothing is being done there, by the way).");
	}
}
