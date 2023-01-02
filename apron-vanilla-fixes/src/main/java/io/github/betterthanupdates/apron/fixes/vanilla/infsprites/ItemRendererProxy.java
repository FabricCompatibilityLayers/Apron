package io.github.betterthanupdates.apron.fixes.vanilla.infsprites;

import java.util.Iterator;

import net.mine_diver.infsprites.proxy.RenderBlocksProxy;
import net.mine_diver.infsprites.proxy.transform.IHasCor;
import net.mine_diver.infsprites.proxy.transform.Shadow;
import net.mine_diver.infsprites.render.TextureManager;
import net.mine_diver.infsprites.api.IRenderHook;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.HeldItemRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ItemRendererProxy extends HeldItemRenderer implements IHasCor {
	@Shadow(
			obfuscatedName = "field_2405"
	)
	private BlockRenderer field_2405;

	public ItemRendererProxy(Minecraft minecraft) {
		super(minecraft);
	}

	@Override
	public void cor() {
		this.field_2405 = new RenderBlocksProxy();
	}

	@Override
	public void render(LivingEntity entityliving, ItemStack itemstack) {
		GL11.glPushMatrix();
		if (itemstack.itemId < Block.BY_ID.length && BlockRenderer.method_42(Block.BY_ID[itemstack.itemId].getRenderType())) {
			GL11.glBindTexture(3553, TextureManager.getTerrain(itemstack.getItemTexture() >> 8));
			Iterator var18 = IRenderHook.renderHooks.iterator();

			while(var18.hasNext()) {
				IRenderHook hook = (IRenderHook)var18.next();
				hook.overrideTexture(Block.BY_ID[itemstack.itemId]);
			}

			this.field_2405.method_48(Block.BY_ID[itemstack.itemId], itemstack.getMeta(), entityliving.getBrightnessAtEyes(1.0F));
		} else {
			int i = entityliving.getHeldItemTexture(itemstack);
			Iterator var4;
			IRenderHook hook;
			if (itemstack.itemId < Block.BY_ID.length) {
				GL11.glBindTexture(3553, TextureManager.getTerrain(i >> 8));
				var4 = IRenderHook.renderHooks.iterator();

				while(var4.hasNext()) {
					hook = (IRenderHook)var4.next();
					hook.overrideTexture(Block.BY_ID[itemstack.itemId]);
				}
			} else {
				GL11.glBindTexture(3553, TextureManager.getGuiItems(i >> 8));
				var4 = IRenderHook.renderHooks.iterator();

				while(var4.hasNext()) {
					hook = (IRenderHook)var4.next();
					hook.overrideTexture(Item.byId[itemstack.itemId]);
				}
			}

			Tessellator tessellator = Tessellator.INSTANCE;
			float f = ((float)(i % 16 * 16) + 0.0F) / 256.0F;
			float f1 = ((float)(i % 16 * 16) + 15.99F) / 256.0F;
			float f2 = ((float)(i / 16 * 16) + 0.0F) / 256.0F;
			float f3 = ((float)(i / 16 * 16) + 15.99F) / 256.0F;
			float f4 = 1.0F;
			float f5 = 0.0F;
			float f6 = 0.3F;
			GL11.glEnable(32826);
			GL11.glTranslatef(-f5, -f6, 0.0F);
			float f7 = 1.5F;
			GL11.glScalef(f7, f7, f7);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			float f8 = 0.0625F;
			tessellator.start();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			tessellator.vertex(0.0, 0.0, 0.0, (double)f1, (double)f3);
			tessellator.vertex((double)f4, 0.0, 0.0, (double)f, (double)f3);
			tessellator.vertex((double)f4, 1.0, 0.0, (double)f, (double)f2);
			tessellator.vertex(0.0, 1.0, 0.0, (double)f1, (double)f2);
			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			tessellator.vertex(0.0, 1.0, (double)(0.0F - f8), (double)f1, (double)f2);
			tessellator.vertex((double)f4, 1.0, (double)(0.0F - f8), (double)f, (double)f2);
			tessellator.vertex((double)f4, 0.0, (double)(0.0F - f8), (double)f, (double)f3);
			tessellator.vertex(0.0, 0.0, (double)(0.0F - f8), (double)f1, (double)f3);
			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);

			int i1;
			float f12;
			float f16;
			float f20;
			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f1 + (f - f1) * f12 - 0.001953125F;
				f20 = f4 * f12;
				tessellator.vertex((double)f20, 0.0, (double)(0.0F - f8), (double)f16, (double)f3);
				tessellator.vertex((double)f20, 0.0, 0.0, (double)f16, (double)f3);
				tessellator.vertex((double)f20, 1.0, 0.0, (double)f16, (double)f2);
				tessellator.vertex((double)f20, 1.0, (double)(0.0F - f8), (double)f16, (double)f2);
			}

			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);

			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f1 + (f - f1) * f12 - 0.001953125F;
				f20 = f4 * f12 + 0.0625F;
				tessellator.vertex((double)f20, 1.0, (double)(0.0F - f8), (double)f16, (double)f2);
				tessellator.vertex((double)f20, 1.0, 0.0, (double)f16, (double)f2);
				tessellator.vertex((double)f20, 0.0, 0.0, (double)f16, (double)f3);
				tessellator.vertex((double)f20, 0.0, (double)(0.0F - f8), (double)f16, (double)f3);
			}

			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);

			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f3 + (f2 - f3) * f12 - 0.001953125F;
				f20 = f4 * f12 + 0.0625F;
				tessellator.vertex(0.0, (double)f20, 0.0, (double)f1, (double)f16);
				tessellator.vertex((double)f4, (double)f20, 0.0, (double)f, (double)f16);
				tessellator.vertex((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
				tessellator.vertex(0.0, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
			}

			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);

			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f3 + (f2 - f3) * f12 - 0.001953125F;
				f20 = f4 * f12;
				tessellator.vertex((double)f4, (double)f20, 0.0, (double)f, (double)f16);
				tessellator.vertex(0.0, (double)f20, 0.0, (double)f1, (double)f16);
				tessellator.vertex(0.0, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
				tessellator.vertex((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
			}

			tessellator.tessellate();
			GL11.glDisable(32826);
		}

		GL11.glPopMatrix();
	}
}
