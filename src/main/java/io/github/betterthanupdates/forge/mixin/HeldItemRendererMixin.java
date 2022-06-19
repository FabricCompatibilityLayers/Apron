package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooksClient;
import forge.ICustomItemRenderer;
import forge.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.HeldItemRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@Shadow
	private Minecraft client;

	@Shadow
	private BlockRenderer blockRenderer;

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void render(LivingEntity livingEntity, ItemStack itemstack) {
		GL11.glPushMatrix();
		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(itemstack.itemId);

		if (customRenderer != null) {
			GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/terrain.png"));
			ForgeHooksClient.overrideTexture(itemstack.getItem());
			ForgeHooksClient.renderCustomItem(customRenderer, this.blockRenderer, itemstack.itemId, itemstack.getMeta(), livingEntity.getBrightnessAtEyes(1.0F));
		} else if (itemstack.itemId < 256 && BlockRenderer.method_42(Block.BY_ID[itemstack.itemId].getRenderType())) {
			GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/terrain.png"));
			ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
			this.blockRenderer.method_48(Block.BY_ID[itemstack.itemId], itemstack.getMeta(), livingEntity.getBrightnessAtEyes(1.0F));
		} else {
			if (itemstack.itemId < 256) {
				GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/terrain.png"));
				ForgeHooksClient.overrideTexture(Block.BY_ID[itemstack.itemId]);
			} else {
				GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/gui/items.png"));
				ForgeHooksClient.overrideTexture(Item.byId[itemstack.itemId]);
			}

			Tessellator tessellator = Tessellator.INSTANCE;
			int i = livingEntity.getHeldItemTexture(itemstack);
			float f = ((float) (i % 16 * 16) + 0.0F) / 256.0F;
			float f1 = ((float) (i % 16 * 16) + 15.99F) / 256.0F;
			float f2 = ((float) (i / 16 * 16) + 0.0F) / 256.0F;
			float f3 = ((float) (i / 16 * 16) + 15.99F) / 256.0F;
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
			tessellator.vertex(0.0, 0.0, 0.0, (double) f1, (double) f3);
			tessellator.vertex((double) f4, 0.0, 0.0, (double) f, (double) f3);
			tessellator.vertex((double) f4, 1.0, 0.0, (double) f, (double) f2);
			tessellator.vertex(0.0, 1.0, 0.0, (double) f1, (double) f2);
			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			tessellator.vertex(0.0, 1.0, (double) (0.0F - f8), (double) f1, (double) f2);
			tessellator.vertex((double) f4, 1.0, (double) (0.0F - f8), (double) f, (double) f2);
			tessellator.vertex((double) f4, 0.0, (double) (0.0F - f8), (double) f, (double) f3);
			tessellator.vertex(0.0, 0.0, (double) (0.0F - f8), (double) f1, (double) f3);
			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);

			for (int j = 0; j < 16; ++j) {
				float f9 = (float) j / 16.0F;
				float f13 = f1 + (f - f1) * f9 - 0.001953125F;
				float f17 = f4 * f9;
				tessellator.vertex((double) f17, 0.0, (double) (0.0F - f8), (double) f13, (double) f3);
				tessellator.vertex((double) f17, 0.0, 0.0, (double) f13, (double) f3);
				tessellator.vertex((double) f17, 1.0, 0.0, (double) f13, (double) f2);
				tessellator.vertex((double) f17, 1.0, (double) (0.0F - f8), (double) f13, (double) f2);
			}

			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);

			for (int k = 0; k < 16; ++k) {
				float f10 = (float) k / 16.0F;
				float f14 = f1 + (f - f1) * f10 - 0.001953125F;
				float f18 = f4 * f10 + 0.0625F;
				tessellator.vertex((double) f18, 1.0, (double) (0.0F - f8), (double) f14, (double) f2);
				tessellator.vertex((double) f18, 1.0, 0.0, (double) f14, (double) f2);
				tessellator.vertex((double) f18, 0.0, 0.0, (double) f14, (double) f3);
				tessellator.vertex((double) f18, 0.0, (double) (0.0F - f8), (double) f14, (double) f3);
			}

			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);

			for (int l = 0; l < 16; ++l) {
				float f11 = (float) l / 16.0F;
				float f15 = f3 + (f2 - f3) * f11 - 0.001953125F;
				float f19 = f4 * f11 + 0.0625F;
				tessellator.vertex(0.0, (double) f19, 0.0, (double) f1, (double) f15);
				tessellator.vertex((double) f4, (double) f19, 0.0, (double) f, (double) f15);
				tessellator.vertex((double) f4, (double) f19, (double) (0.0F - f8), (double) f, (double) f15);
				tessellator.vertex(0.0, (double) f19, (double) (0.0F - f8), (double) f1, (double) f15);
			}

			tessellator.tessellate();
			tessellator.start();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);

			for (int i1 = 0; i1 < 16; ++i1) {
				float f12 = (float) i1 / 16.0F;
				float f16 = f3 + (f2 - f3) * f12 - 0.001953125F;
				float f20 = f4 * f12;
				tessellator.vertex((double) f4, (double) f20, 0.0, (double) f, (double) f16);
				tessellator.vertex(0.0, (double) f20, 0.0, (double) f1, (double) f16);
				tessellator.vertex(0.0, (double) f20, (double) (0.0F - f8), (double) f1, (double) f16);
				tessellator.vertex((double) f4, (double) f20, (double) (0.0F - f8), (double) f, (double) f16);
			}

			tessellator.tessellate();
			GL11.glDisable(32826);
		}

		GL11.glPopMatrix();
	}
}
