package io.github.betterthanupdates.apron.fixes.vanilla.infsprites;

import java.util.Iterator;
import java.util.Random;

import net.mine_diver.infsprites.proxy.RenderBlocksProxy;
import net.mine_diver.infsprites.proxy.transform.IHasCor;
import net.mine_diver.infsprites.proxy.transform.Shadow;
import net.mine_diver.infsprites.render.TextureManager;
import net.mine_diver.infsprites.api.IRenderHook;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderItemProxy extends ItemRenderer implements IHasCor {
	@Shadow(
			obfuscatedName = "field_1708"
	)
	private BlockRenderer field_1708;
	@Shadow(
			obfuscatedName = "field_1709"
	)
	private Random field_1709;

	@Override
	public void cor() {
		this.field_1708 = new RenderBlocksProxy();
	}

	public void method_1484(ItemEntity entityitem, double d, double d1, double d2, float f, float f1) {
		this.field_1709.setSeed(187L);
		ItemStack itemstack = entityitem.stack;
		GL11.glPushMatrix();
		float f2 = MathHelper.sin(((float)entityitem.age + f1) / 10.0F + entityitem.field_567) * 0.1F + 0.1F;
		float f3 = (((float)entityitem.age + f1) / 20.0F + entityitem.field_567) * 57.29578F;
		byte byte0 = 1;
		if (entityitem.stack.count > 1) {
			byte0 = 2;
		}

		if (entityitem.stack.count > 5) {
			byte0 = 3;
		}

		if (entityitem.stack.count > 20) {
			byte0 = 4;
		}

		GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
		GL11.glEnable(32826);
		float f8;
		float f10;
		float f6;
		if (itemstack.itemId < 256 && BlockRenderer.method_42(Block.BY_ID[itemstack.itemId].getRenderType())) {
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			GL11.glBindTexture(3553, net.mine_diver.infsprites.render.TextureManager.getTerrain(itemstack.getItemTexture() >> 8));
			Iterator var28 = IRenderHook.renderHooks.iterator();

			while(var28.hasNext()) {
				IRenderHook hook = (IRenderHook)var28.next();
				hook.overrideTexture(Block.BY_ID[itemstack.itemId]);
			}

			float f4 = 0.25F;
			if (!Block.BY_ID[itemstack.itemId].isFullCube() && itemstack.itemId != Block.STONE_SLAB.id && Block.BY_ID[itemstack.itemId].getRenderType() != 16) {
				f4 = 0.5F;
			}

			GL11.glScalef(f4, f4, f4);

			for(int j = 0; j < byte0; ++j) {
				GL11.glPushMatrix();
				if (j > 0) {
					f6 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					f8 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					f10 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					GL11.glTranslatef(f6, f8, f10);
				}

				this.field_1708.method_48(Block.BY_ID[itemstack.itemId], itemstack.getMeta(), entityitem.getBrightnessAtEyes(f1));
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int i = itemstack.getItemTexture();
			Iterator var15;
			IRenderHook hook;
			if (itemstack.itemId < Block.BY_ID.length) {
				GL11.glBindTexture(3553, net.mine_diver.infsprites.render.TextureManager.getTerrain(i >> 8));
				var15 = IRenderHook.renderHooks.iterator();

				while(var15.hasNext()) {
					hook = (IRenderHook)var15.next();
					hook.overrideTexture(Block.BY_ID[itemstack.itemId]);
				}
			} else {
				GL11.glBindTexture(3553, net.mine_diver.infsprites.render.TextureManager.getGuiItems(i >> 8));
				var15 = IRenderHook.renderHooks.iterator();

				while(var15.hasNext()) {
					hook = (IRenderHook)var15.next();
					hook.overrideTexture(Item.byId[itemstack.itemId]);
				}
			}

			Tessellator tessellator = Tessellator.INSTANCE;
			f6 = (float)(i % 16 * 16 + 0) / 256.0F;
			f8 = (float)(i % 16 * 16 + 16) / 256.0F;
			f10 = (float)(i / 16 * 16 + 0) / 256.0F;
			float f11 = (float)(i / 16 * 16 + 16) / 256.0F;
			float f12 = 1.0F;
			float f13 = 0.5F;
			float f14 = 0.25F;
			int l;
			float f16;
			float f18;
			float f20;
			if (this.field_1707) {
				l = Item.byId[itemstack.itemId].getNameColor(itemstack.getMeta());
				f16 = (float)(l >> 16 & 255) / 255.0F;
				f18 = (float)(l >> 8 & 255) / 255.0F;
				f20 = (float)(l & 255) / 255.0F;
				float f21 = entityitem.getBrightnessAtEyes(f1);
				GL11.glColor4f(f16 * f21, f18 * f21, f20 * f21, 1.0F);
			}

			for(l = 0; l < byte0; ++l) {
				GL11.glPushMatrix();
				if (l > 0) {
					f16 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.3F;
					f18 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.3F;
					f20 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(f16, f18, f20);
				}

				GL11.glRotatef(180.0F - this.dispatcher.field_2497, 0.0F, 1.0F, 0.0F);
				tessellator.start();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator.vertex((double)(0.0F - f13), (double)(0.0F - f14), 0.0, (double)f6, (double)f11);
				tessellator.vertex((double)(f12 - f13), (double)(0.0F - f14), 0.0, (double)f8, (double)f11);
				tessellator.vertex((double)(f12 - f13), (double)(1.0F - f14), 0.0, (double)f8, (double)f10);
				tessellator.vertex((double)(0.0F - f13), (double)(1.0F - f14), 0.0, (double)f6, (double)f10);
				tessellator.tessellate();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Override
	public void renderItemOnGui(TextRenderer fontrenderer, net.minecraft.client.texture.TextureManager renderengine, int i, int j, int k, int l, int i1) {
		float f3;
		if (i < Block.BY_ID.length && BlockRenderer.method_42(Block.BY_ID[i].getRenderType())) {
			renderengine.bindTexture(net.mine_diver.infsprites.render.TextureManager.getTerrain(k >> 8));
			Iterator var16 = IRenderHook.renderHooks.iterator();

			while(var16.hasNext()) {
				IRenderHook hook = (IRenderHook)var16.next();
				hook.overrideTexture(Block.BY_ID[i]);
			}

			Block block = Block.BY_ID[i];
			GL11.glPushMatrix();
			GL11.glTranslatef((float)(l - 2), (float)(i1 + 3), -3.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int l1 = Item.byId[i].getNameColor(j);
			f3 = (float)(l1 >> 16 & 255) / 255.0F;
			float f4 = (float)(l1 >> 8 & 255) / 255.0F;
			float f5 = (float)(l1 & 255) / 255.0F;
			if (this.field_1707) {
				GL11.glColor4f(f3, f4, f5, 1.0F);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.field_1708.field_81 = this.field_1707;
			this.field_1708.method_48(block, j, 1.0F);
			this.field_1708.field_81 = true;
			GL11.glPopMatrix();
		} else if (k >= 0) {
			GL11.glDisable(2896);
			Iterator var8;
			IRenderHook hook;
			if (i < 256) {
				renderengine.bindTexture(net.mine_diver.infsprites.render.TextureManager.getTerrain(k >> 8));
				var8 = IRenderHook.renderHooks.iterator();

				while(var8.hasNext()) {
					hook = (IRenderHook)var8.next();
					hook.overrideTexture(Block.BY_ID[i]);
				}
			} else {
				renderengine.bindTexture(TextureManager.getGuiItems(k >> 8));
				var8 = IRenderHook.renderHooks.iterator();

				while(var8.hasNext()) {
					hook = (IRenderHook)var8.next();
					hook.overrideTexture(Item.byId[i]);
				}
			}

			int k1 = Item.byId[i].getNameColor(j);
			float f = (float)(k1 >> 16 & 255) / 255.0F;
			float f1 = (float)(k1 >> 8 & 255) / 255.0F;
			f3 = (float)(k1 & 255) / 255.0F;
			if (this.field_1707) {
				GL11.glColor4f(f, f1, f3, 1.0F);
			}

			this.method_1483(l, i1, k % 16 * 16, k / 16 * 16, 16, 16);
			GL11.glEnable(2896);
		}

		GL11.glEnable(2884);
	}

	// I don't know why I have to implement this
	public void render(Entity arg, double d, double e, double f, float g, float h) {

	}
}
