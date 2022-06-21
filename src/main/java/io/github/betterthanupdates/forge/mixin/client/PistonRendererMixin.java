package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.block.BlockEntityRenderer;
import net.minecraft.client.render.entity.block.PistonRenderer;
import net.minecraft.entity.block.PistonBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(PistonRenderer.class)
public abstract class PistonRendererMixin extends BlockEntityRenderer {
	@Shadow
	private BlockRenderer field_1131;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void render(PistonBlockEntity piston, double d, double d1, double d2, float f) {
		Block block = Block.BY_ID[piston.method_1518()];

		if (block != null && piston.method_1519(f) < 1.0F) {
			Tessellator tessellator = Tessellator.INSTANCE;
			this.method_1064("/terrain.png");
			RenderHelper.disableLighting();
			GL11.glBlendFunc(770, 771);
			GL11.glEnable(3042);
			GL11.glDisable(2884);

			if (Minecraft.isSmoothLightingEnabled()) {
				GL11.glShadeModel(7425);
			} else {
				GL11.glShadeModel(7424);
			}

			ForgeHooksClient.beforeBlockRender(block, this.field_1131);
			tessellator.start();
			tessellator.setOffset(
					(double) ((float) d - (float) piston.x + piston.method_1524(f)),
					(double) ((float) d1 - (float) piston.y + piston.method_1525(f)),
					(double) ((float) d2 - (float) piston.z + piston.method_1526(f))
			);
			tessellator.color(1, 1, 1);

			if (block == Block.PISTON_HEAD && piston.method_1519(f) < 0.5F) {
				this.field_1131.method_52(block, piston.x, piston.y, piston.z, false);
			} else if (piston.method_1527() && !piston.isExtending()) {
				Block.PISTON_HEAD.method_729(((PistonBlock) block).method_767());
				this.field_1131
						.method_52(Block.PISTON_HEAD, piston.x, piston.y, piston.z, piston.method_1519(f) < 0.5F);
				Block.PISTON_HEAD.method_728();
				tessellator.setOffset(
						(double) ((float) d - (float) piston.x),
						(double) ((float) d1 - (float) piston.y),
						(double) ((float) d2 - (float) piston.z)
				);
				this.field_1131.renderPistonExtended(block, piston.x, piston.y, piston.z);
			} else {
				this.field_1131.renderAllSides(block, piston.x, piston.y, piston.z);
			}

			tessellator.setOffset(0.0, 0.0, 0.0);
			tessellator.tessellate();
			ForgeHooksClient.afterBlockRender(block, this.field_1131);
			RenderHelper.enableLighting();
		}
	}
}
