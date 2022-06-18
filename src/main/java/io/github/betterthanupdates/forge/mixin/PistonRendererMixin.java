package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooksClient;
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

@Mixin(PistonRenderer.class)
public abstract class PistonRendererMixin extends BlockEntityRenderer {

	@Shadow
	private BlockRenderer field_1131;

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void render(PistonBlockEntity tileentitypiston, double d, double d1, double d2, float f) {
		Block block = Block.BY_ID[tileentitypiston.method_1518()];
		if (block != null && tileentitypiston.method_1519(f) < 1.0F) {
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
					(double) ((float) d - (float) tileentitypiston.x + tileentitypiston.method_1524(f)),
					(double) ((float) d1 - (float) tileentitypiston.y + tileentitypiston.method_1525(f)),
					(double) ((float) d2 - (float) tileentitypiston.z + tileentitypiston.method_1526(f))
			);
			tessellator.color(1, 1, 1);
			if (block == Block.PISTON_HEAD && tileentitypiston.method_1519(f) < 0.5F) {
				this.field_1131.method_52(block, tileentitypiston.x, tileentitypiston.y, tileentitypiston.z, false);
			} else if (tileentitypiston.method_1527() && !tileentitypiston.isExtending()) {
				Block.PISTON_HEAD.method_729(((PistonBlock) block).method_767());
				this.field_1131
						.method_52(Block.PISTON_HEAD, tileentitypiston.x, tileentitypiston.y, tileentitypiston.z, tileentitypiston.method_1519(f) < 0.5F);
				Block.PISTON_HEAD.method_728();
				tessellator.setOffset(
						(double) ((float) d - (float) tileentitypiston.x),
						(double) ((float) d1 - (float) tileentitypiston.y),
						(double) ((float) d2 - (float) tileentitypiston.z)
				);
				this.field_1131.renderPistonExtended(block, tileentitypiston.x, tileentitypiston.y, tileentitypiston.z);
			} else {
				this.field_1131.renderAllSides(block, tileentitypiston.x, tileentitypiston.y, tileentitypiston.z);
			}

			tessellator.setOffset(0.0, 0.0, 0.0);
			tessellator.draw();
			ForgeHooksClient.afterBlockRender(block, this.field_1131);
			RenderHelper.enableLighting();
		}

	}
}
