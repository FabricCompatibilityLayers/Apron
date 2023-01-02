package io.github.betterthanupdates.apron.fixes.vanilla.infsprites;

import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import modloader.ModLoader;
import net.mine_diver.infsprites.proxy.IRenderEngineHolder;
import net.mine_diver.infsprites.proxy.RenderBlocksProxy;
import net.mine_diver.infsprites.proxy.transform.IHasCor;
import net.mine_diver.infsprites.proxy.transform.ProxyTransformer;
import net.mine_diver.infsprites.proxy.transform.Shadow;
import net.minecraft.class_66;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldEventRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.EntityOppositeComparator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class RenderGlobalProxy extends WorldEventRenderer implements IHasCor, IRenderEngineHolder {
	@Shadow(
			obfuscatedName = "field_1805"
	)
	private World field_1805;
	@Shadow(
			obfuscatedName = "field_1806"
	)
	private TextureManager field_1806;
	@Shadow(
			obfuscatedName = "field_1807"
	)
	private List<class_66> field_1807;
	@Shadow(
			obfuscatedName = "field_1808"
	)
	private class_66[] field_1808;
	@Shadow(
			obfuscatedName = "field_1809"
	)
	private class_66[] field_1809;
	@Shadow(
			obfuscatedName = "field_1810"
	)
	private int field_1810;
	@Shadow(
			obfuscatedName = "field_1811"
	)
	private int field_1811;
	@Shadow(
			obfuscatedName = "field_1812"
	)
	private int field_1812;
	@Shadow(
			obfuscatedName = "field_1813"
	)
	private int field_1813;
	@Shadow(
			obfuscatedName = "field_1814"
	)
	private Minecraft field_1814;
	@Shadow(
			obfuscatedName = "field_1815"
	)
	private BlockRenderer field_1815;
	@Shadow(
			obfuscatedName = "field_1816"
	)
	private IntBuffer field_1816;
	@Shadow(
			obfuscatedName = "field_1817"
	)
	private boolean field_1817;
	@Shadow(
			obfuscatedName = "field_1776"
	)
	private int field_1776;
	@Shadow(
			obfuscatedName = "field_1777"
	)
	private int field_1777;
	@Shadow(
			obfuscatedName = "field_1778"
	)
	private int field_1778;
	@Shadow(
			obfuscatedName = "field_1779"
	)
	private int field_1779;
	@Shadow(
			obfuscatedName = "field_1780"
	)
	private int field_1780;
	@Shadow(
			obfuscatedName = "field_1781"
	)
	private int field_1781;
	@Shadow(
			obfuscatedName = "field_1782"
	)
	private int field_1782;
	@Shadow(
			obfuscatedName = "field_1783"
	)
	private int field_1783;

	//@Shadow(
	//		obfuscatedName = "method_1149"
	//)
	//public abstract void method_1149(int i, int j, int k);

	public RenderGlobalProxy(Minecraft minecraft, TextureManager textureManager) {
		super(minecraft, textureManager);
	}

	@Override
	public void cor() {
		try {
			Field field = ModLoader.class.getDeclaredField("texturesAdded");
			field.setAccessible(true);
			field.set((Object)null, true);
			field = ModLoader.class.getDeclaredField("texPack");
			field.setAccessible(true);
			field.set((Object)null, ModLoader.getMinecraftInstance().options.activeTexturePack);
			ModLoader.RegisterAllTextureOverrides(ModLoader.getMinecraftInstance().textureManager);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var2) {
			throw new RuntimeException(var2);
		}
	}

	@Override
	public void method_1546(World world) {
		super.method_1546(world);
		this.field_1815 = new RenderBlocksProxy(world);
	}

	@Override
	public void method_1537() {
		Block.LEAVES.updateTexture(this.field_1814.options.fancyGraphics);
		this.field_1782 = this.field_1814.options.viewDistance;
		int j;
		if (this.field_1809 != null) {
			for(j = 0; j < this.field_1809.length; ++j) {
				this.field_1809[j].method_302();
			}
		}

		j = 64 << 3 - this.field_1782;
		//if (CubicChunksPatcher.installed) {
			// Cubic chunks isn't gonna work in Apron anyway
			// I dont want to deal with pain
			//if (this.client.options.use74sFarViewMod) {
			//	j = j * 4 + 256;
			//}

			//this.field_1810 = this.field_1811 = this.field_1812 = (j >> 4) + 1;
		//} else {
			if (j > 400) {
				j = 400;
			}

			this.field_1810 = j / 16 + 1;
			this.field_1811 = 8;
			this.field_1812 = j / 16 + 1;
		//}

		this.field_1809 = new class_66[this.field_1810 * this.field_1811 * this.field_1812];
		this.field_1808 = new class_66[this.field_1810 * this.field_1811 * this.field_1812];
		int k = 0;
		int l = 0;
		this.field_1776 = 0;
		this.field_1777 = 0;
		this.field_1778 = 0;
		this.field_1779 = this.field_1810;
		this.field_1780 = this.field_1811;
		this.field_1781 = this.field_1812;

		int j1;
		for(j1 = 0; j1 < this.field_1807.size(); ++j1) {
			((class_66)this.field_1807.get(j1)).field_249 = false;
		}

		this.field_1807.clear();
		this.blockEntities.clear();

		for(j1 = 0; j1 < this.field_1810; ++j1) {
			for(int k1 = 0; k1 < this.field_1811; ++k1) {
				for(int l1 = 0; l1 < this.field_1812; ++l1) {
					class_66 worldRenderer = this.field_1809[(l1 * this.field_1811 + k1) * this.field_1810 + j1] = (class_66)ProxyTransformer.createProxy(WorldRendererProxy.class, new class_66(this.field_1805, this.blockEntities, j1 * 16, k1 * 16, l1 * 16, 16, this.field_1813 + k), false);
					if (this.field_1817) {
						worldRenderer.field_254 = this.field_1816.get(l);
					}

					worldRenderer.field_253 = false;
					worldRenderer.field_252 = true;
					worldRenderer.field_243 = true;
					worldRenderer.field_251 = l++;
					worldRenderer.method_305();
					this.field_1808[(l1 * this.field_1811 + k1) * this.field_1810 + j1] = worldRenderer;
					this.field_1807.add(worldRenderer);
					k += 3;
				}
			}
		}

		if (this.field_1805 != null) {
			LivingEntity entityliving = this.field_1814.viewEntity;
			if (entityliving != null) {
				this.updateBlock(MathHelper.floor(entityliving.x), MathHelper.floor(entityliving.y), MathHelper.floor(entityliving.z));
				Arrays.sort(this.field_1808, new EntityOppositeComparator(entityliving));
			}
		}

		this.field_1783 = 2;
	}

	@Override
	public void setRenderEngine(TextureManager newtextureManager) {
		this.field_1806 = newtextureManager;
	}
}
