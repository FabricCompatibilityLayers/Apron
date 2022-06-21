package io.github.betterthanupdates.forge.mixin.client;

import java.util.Random;

import forge.ForgeHooksClient;
import forge.ICustomItemRenderer;
import forge.MinecraftForgeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {
	@Shadow
	private Random rand;

	@Shadow
	private BlockRenderer field_1708;

	@Shadow
	public boolean field_1707;

	@Shadow
	public abstract void method_1483(int i, int j, int k, int l, int m, int n);

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void render(ItemEntity itemEntity, double d, double d1, double d2, float f, float f1) {
		this.rand.setSeed(187L);
		ItemStack stack = itemEntity.stack;
		GL11.glPushMatrix();
		float f2 = MathHelper.sin(((float) itemEntity.age + f1) / 10.0F + itemEntity.field_567) * 0.1F + 0.1F;
		float f3 = (((float) itemEntity.age + f1) / 20.0F + itemEntity.field_567) * (float) (180.0 / Math.PI);
		byte byte0 = 1;

		if (itemEntity.stack.count > 1) {
			byte0 = 2;
		}

		if (itemEntity.stack.count > 5) {
			byte0 = 3;
		}

		if (itemEntity.stack.count > 20) {
			byte0 = 4;
		}

		GL11.glTranslatef((float) d, (float) d1 + f2, (float) d2);
		GL11.glEnable(32826);
		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(stack.itemId);

		if (customRenderer != null) {
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			this.bindTexture("/terrain.png");
			ForgeHooksClient.overrideTexture(stack.getItem());
			float f4 = 0.25F;
			f4 = 0.5F;
			GL11.glScalef(f4, f4, f4);

			for (int j = 0; j < byte0; ++j) {
				GL11.glPushMatrix();

				if (j > 0) {
					float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					float f7 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					float f9 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					GL11.glTranslatef(f5, f7, f9);
				}

				ForgeHooksClient.renderCustomItem(customRenderer, this.field_1708, stack.itemId, stack.getMeta(), itemEntity.getBrightnessAtEyes(f1));
				GL11.glPopMatrix();
			}
		} else if (stack.itemId < 256 && BlockRenderer.method_42(Block.BY_ID[stack.itemId].getRenderType())) {
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			this.bindTexture("/terrain.png");
			ForgeHooksClient.overrideTexture(Block.BY_ID[stack.itemId]);
			float f4 = 0.25F;

			if (!Block.BY_ID[stack.itemId].isFullCube() && stack.itemId != Block.STONE_SLAB.id && Block.BY_ID[stack.itemId].getRenderType() != 16) {
				f4 = 0.5F;
			}

			GL11.glScalef(f4, f4, f4);

			for (int j = 0; j < byte0; ++j) {
				GL11.glPushMatrix();

				if (j > 0) {
					float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					float f7 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					float f9 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					GL11.glTranslatef(f5, f7, f9);
				}

				this.field_1708.method_48(Block.BY_ID[stack.itemId], stack.getMeta(), itemEntity.getBrightnessAtEyes(f1));
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int i = stack.getItemTexture();

			if (stack.itemId < 256) {
				this.bindTexture("/terrain.png");
				ForgeHooksClient.overrideTexture(Block.BY_ID[stack.itemId]);
			} else {
				this.bindTexture("/gui/items.png");
				ForgeHooksClient.overrideTexture(Item.byId[stack.itemId]);
			}

			Tessellator tessellator = Tessellator.INSTANCE;
			float f6 = (float) (i % 16 * 16 + 0) / 256.0F;
			float f8 = (float) (i % 16 * 16 + 16) / 256.0F;
			float f10 = (float) (i / 16 * 16 + 0) / 256.0F;
			float f11 = (float) (i / 16 * 16 + 16) / 256.0F;
			float f12 = 1.0F;
			float f13 = 0.5F;
			float f14 = 0.25F;

			if (this.field_1707) {
				int k = Item.byId[stack.itemId].getNameColor(stack.getMeta());
				float f15 = (float) (k >> 16 & 0xFF) / 255.0F;
				float f17 = (float) (k >> 8 & 0xFF) / 255.0F;
				float f19 = (float) (k & 0xFF) / 255.0F;
				float f21 = itemEntity.getBrightnessAtEyes(f1);
				GL11.glColor4f(f15 * f21, f17 * f21, f19 * f21, 1.0F);
			}

			for (int l = 0; l < byte0; ++l) {
				GL11.glPushMatrix();

				if (l > 0) {
					float f16 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f18 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f20 = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(f16, f18, f20);
				}

				GL11.glRotatef(180.0F - this.dispatcher.field_2497, 0.0F, 1.0F, 0.0F);
				tessellator.start();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator.vertex((double) (0.0F - f13), (double) (0.0F - f14), 0.0, (double) f6, (double) f11);
				tessellator.vertex((double) (f12 - f13), (double) (0.0F - f14), 0.0, (double) f8, (double) f11);
				tessellator.vertex((double) (f12 - f13), (double) (1.0F - f14), 0.0, (double) f8, (double) f10);
				tessellator.vertex((double) (0.0F - f13), (double) (1.0F - f14), 0.0, (double) f6, (double) f10);
				tessellator.tessellate();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void renderItemOnGui(TextRenderer fontrenderer, TextureManager renderengine, int i, int j, int k, int l, int i1) {
		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(i);

		if (customRenderer != null) {
			renderengine.bindTexture(renderengine.getTextureId("/terrain.png"));
			Item item = Item.byId[i];
			ForgeHooksClient.overrideTexture(item);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) (l - 2), (float) (i1 + 3), -3.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int l1 = item.getNameColor(j);
			float f2 = (float) (l1 >> 16 & 0xFF) / 255.0F;
			float f4 = (float) (l1 >> 8 & 0xFF) / 255.0F;
			float f5 = (float) (l1 & 0xFF) / 255.0F;

			if (this.field_1707) {
				GL11.glColor4f(f2, f4, f5, 1.0F);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.field_1708.field_81 = this.field_1707;
			ForgeHooksClient.renderCustomItem(customRenderer, this.field_1708, i, j, 1.0F);
			this.field_1708.field_81 = true;
			GL11.glPopMatrix();
		} else if (i < 256 && BlockRenderer.method_42(Block.BY_ID[i].getRenderType())) {
			renderengine.bindTexture(renderengine.getTextureId("/terrain.png"));
			Block block = Block.BY_ID[i];
			ForgeHooksClient.overrideTexture(block);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) (l - 2), (float) (i1 + 3), -3.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int l1 = Item.byId[i].getNameColor(j);
			float f2 = (float) (l1 >> 16 & 0xFF) / 255.0F;
			float f4 = (float) (l1 >> 8 & 0xFF) / 255.0F;
			float f5 = (float) (l1 & 0xFF) / 255.0F;

			if (this.field_1707) {
				GL11.glColor4f(f2, f4, f5, 1.0F);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.field_1708.field_81 = this.field_1707;
			this.field_1708.method_48(block, j, 1.0F);
			this.field_1708.field_81 = true;
			GL11.glPopMatrix();
		} else if (k >= 0) {
			GL11.glDisable(2896);

			if (i < 256) {
				renderengine.bindTexture(renderengine.getTextureId("/terrain.png"));
				ForgeHooksClient.overrideTexture(Block.BY_ID[i]);
			} else {
				renderengine.bindTexture(renderengine.getTextureId("/gui/items.png"));
				ForgeHooksClient.overrideTexture(Item.byId[i]);
			}

			int k1 = Item.byId[i].getNameColor(j);
			float f = (float) (k1 >> 16 & 0xFF) / 255.0F;
			float f1 = (float) (k1 >> 8 & 0xFF) / 255.0F;
			float f3 = (float) (k1 & 0xFF) / 255.0F;

			if (this.field_1707) {
				GL11.glColor4f(f, f1, f3, 1.0F);
			}

			this.method_1483(l, i1, k % 16 * 16, k / 16 * 16, 16, 16);
			GL11.glEnable(2896);
		}

		GL11.glEnable(2884);
	}
}
