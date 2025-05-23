package io.github.betterthanupdates.forge.mixin.client.render.item.nostation;

import java.util.Random;

import forge.ForgeHooksClient;
import forge.ICustomItemRenderer;
import forge.MinecraftForgeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {
	@Shadow
	private Random field_1709;

	@Shadow
	private BlockRenderManager field_1708;

	@Shadow
	public boolean field_1707;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;isFullCube()Z", ordinal = 0))
	private void forge$render$1(ItemEntity entityitem, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
		ItemStack itemstack = entityitem.stack;
		ForgeHooksClient.overrideTexture(Block.BLOCKS[itemstack.itemId]);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC,
			target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;"))
	private void forge$render$2(ItemEntity entityitem, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
		ItemStack itemstack = entityitem.stack;

		if (itemstack.itemId < 256) {
			ForgeHooksClient.overrideTexture(Block.BLOCKS[itemstack.itemId]);
		} else {
			ForgeHooksClient.overrideTexture(Item.ITEMS[itemstack.itemId]);
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "render(Lnet/minecraft/entity/ItemEntity;DDDFF)V", cancellable = true,
			at = @At(value = "INVOKE", ordinal = 0, target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", remap = false, shift = At.Shift.AFTER))
	private void forge$render$3(ItemEntity itemEntity, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
		ItemStack stack = itemEntity.stack;

		float f3 = (((float) itemEntity.itemAge + f1) / 20.0F + itemEntity.initialRotationAngle) * (float) (180.0 / Math.PI);
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

		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(stack.itemId);

		if (customRenderer != null) {
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			this.bindTexture("/terrain.png");
			ForgeHooksClient.overrideTexture(stack.getItem());
			float f4 = 0.5F;
			GL11.glScalef(f4, f4, f4);

			for (int j = 0; j < byte0; ++j) {
				GL11.glPushMatrix();

				if (j > 0) {
					float f5 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					float f7 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					float f9 = (this.field_1709.nextFloat() * 2.0F - 1.0F) * 0.2F / f4;
					GL11.glTranslatef(f5, f7, f9);
				}

				ForgeHooksClient.renderCustomItem(customRenderer, this.field_1708, stack.itemId, stack.getDamage(), itemEntity.method_1394(f1));
				GL11.glPopMatrix();
			}

			GL11.glDisable(32826);
			GL11.glPopMatrix();

			ci.cancel();
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1486", at = @At(value = "INVOKE",
			target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, remap = false))
	private void forge$renderItemOnGui$1(TextRenderer fontrenderer, TextureManager renderengine, int i, int j, int k, int l, int i1, CallbackInfo ci) {
		ForgeHooksClient.overrideTexture(Block.BLOCKS[i]);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1486", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/item/Item;method_440(I)I", ordinal = 1))
	private void forge$renderItemOnGui$2(TextRenderer fontrenderer, TextureManager renderengine, int i, int j, int k, int l, int i1, CallbackInfo ci) {
		if (i < 256) {
			ForgeHooksClient.overrideTexture(Block.BLOCKS[i]);
		} else {
			ForgeHooksClient.overrideTexture(Item.ITEMS[i]);
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1486",
			at = @At("HEAD"),
			cancellable = true)
	private void forge$renderItemOnGui$3(TextRenderer fontrenderer, TextureManager renderengine, int i, int j, int k, int l, int i1, CallbackInfo ci) {
		ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(i);

		if (customRenderer != null) {
			renderengine.bindTexture(renderengine.getTextureId("/terrain.png"));
			Item item = Item.ITEMS[i];
			ForgeHooksClient.overrideTexture(item);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) (l - 2), (float) (i1 + 3), -3.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int l1 = item.method_440(j);
			float f2 = (float) (l1 >> 16 & 0xFF) / 255.0F;
			float f4 = (float) (l1 >> 8 & 0xFF) / 255.0F;
			float f5 = (float) (l1 & 0xFF) / 255.0F;

			if (this.field_1707) {
				GL11.glColor4f(f2, f4, f5, 1.0F);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.field_1708.inventoryColorEnabled = this.field_1707;
			ForgeHooksClient.renderCustomItem(customRenderer, this.field_1708, i, j, 1.0F);
			this.field_1708.inventoryColorEnabled = true;
			GL11.glPopMatrix();

			GL11.glEnable(2884);

			ci.cancel();
		}
	}
}
