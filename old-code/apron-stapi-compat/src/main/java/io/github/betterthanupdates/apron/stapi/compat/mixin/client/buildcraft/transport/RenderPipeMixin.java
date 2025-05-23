package io.github.betterthanupdates.apron.stapi.compat.mixin.client.buildcraft.transport;

import buildcraft.api.EntityPassiveItem;
import buildcraft.transport.RenderPipe;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderPipe.class, remap = false)
public class RenderPipeMixin {
	@Shadow(remap = false)
	private BlockRenderManager renderBlocks;

	/**
	 * @author CatCore
	 * @reason Buildcraft can't know which item is a block accurately on its own
	 */
	@Inject(method = "doRenderItem", at = @At("HEAD"), cancellable = true, remap = false)
	private void apron$doRenderItem(EntityPassiveItem entityitem, double x, double y, double z, double brigntess, CallbackInfo ci) {
		if (entityitem != null && entityitem.item != null) {
			ItemStack itemstack = entityitem.item;
			GL11.glPushMatrix();
			byte byte0 = 1;
			if (entityitem.item.count > 1) {
				byte0 = 2;
			}

			if (entityitem.item.count > 5) {
				byte0 = 3;
			}

			if (entityitem.item.count > 20) {
				byte0 = 4;
			}

			GL11.glTranslatef((float)x, (float)y, (float)z);
			GL11.glEnable(32826);

			Item item = itemstack.getItem();
			SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
			atlas.bindTexture();

			if (item instanceof BlockItem blockItem && BlockRenderManager.isSideLit(blockItem.getBlock().getRenderType())) {
				GL11.glTranslatef(0.0F, 0.25F, 0.0F);

				float f4 = 0.25F;
				if (!blockItem.getBlock().isFullCube() && blockItem.getBlock() != Block.SLAB) {
					f4 = 0.5F;
				}

				GL11.glScalef(f4, f4, f4);

				for(int j = 0; j < byte0; ++j) {
					GL11.glPushMatrix();
					this.renderBlocks.render(blockItem.getBlock(), itemstack.getDamage(), (float)brigntess);
					GL11.glPopMatrix();
				}
			} else {
				GL11.glTranslatef(0.0F, 0.1F, 0.0F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);

				int i = ApronStAPICompat.fixItemTexture(itemstack.getTextureId(), item);
				Identifier textureId = item.getAtlas().getTexture(i).getId();
				Sprite sprite = atlas.getSprite(
						textureId
				);

				Tessellator tessellator = Tessellator.INSTANCE;
				float f12 = 1.0F;
				float f13 = 0.5F;
				float f14 = 0.25F;

				for(int k = 0; k < byte0; ++k) {
					GL11.glPushMatrix();
					GL11.glRotatef(180.0F - EntityRenderDispatcher.field_2489.field_2497, 0.0F, 1.0F, 0.0F);
					tessellator.startQuads();
					tessellator.normal(0.0F, 1.0F, 0.0F);
					tessellator.vertex((double)(0.0F - f13), (double)(0.0F - f14), 0.0, (double)sprite.getMinU(), (double)sprite.getMaxV());
					tessellator.vertex((double)(f12 - f13), (double)(0.0F - f14), 0.0, (double)sprite.getMaxU(), (double)sprite.getMaxV());
					tessellator.vertex((double)(f12 - f13), (double)(1.0F - f14), 0.0, (double)sprite.getMaxU(), (double)sprite.getMinV());
					tessellator.vertex((double)(0.0F - f13), (double)(1.0F - f14), 0.0, (double)sprite.getMinU(), (double)sprite.getMinV());
					tessellator.draw();
					GL11.glPopMatrix();
				}
			}

			GL11.glDisable(32826);
			GL11.glPopMatrix();
		}

		ci.cancel();
	}
}
