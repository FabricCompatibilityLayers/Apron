package modloader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.TextureBinder;

import io.github.betterthanupdates.babricated.impl.client.ClientUtil;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class ModTextureStatic extends TextureBinder {
	private boolean oldAnaglyph;
	private final int[] pixels;

	public ModTextureStatic(int slot, int dst, BufferedImage source) {
		this(slot, 1, dst, source);
	}

	public ModTextureStatic(int slot, int size, int dst, BufferedImage source) {
		super(slot);
		this.textureSize = size;
		this.renderMode = dst;
		this.bindTexture(ClientUtil.instance.getTextureManager());
		int targetWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096) / 16;
		int targetHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097) / 16;
		int width = source.getWidth();
		int height = source.getHeight();
		this.pixels = new int[targetWidth * targetHeight];
		this.grid = new byte[targetWidth * targetHeight * 4];
		if (width == height && width == targetWidth) {
			source.getRGB(0, 0, width, height, this.pixels, 0, width);
		} else {
			BufferedImage img = new BufferedImage(targetWidth, targetHeight, 6);
			Graphics2D gfx = img.createGraphics();
			gfx.drawImage(source, 0, 0, targetWidth, targetHeight, 0, 0, width, height, null);
			img.getRGB(0, 0, targetWidth, targetHeight, this.pixels, 0, targetWidth);
			gfx.dispose();
		}

		this.update();
	}

	@SuppressWarnings("PointlessBitwiseExpression")
	public void update() {
		for (int i = 0; i < this.pixels.length; ++i) {
			int a = this.pixels[i] >> 24 & 0xFF;
			int r = this.pixels[i] >> 16 & 0xFF;
			int g = this.pixels[i] >> 8 & 0xFF;
			int b = this.pixels[i] >> 0 & 0xFF;
			if (this.render3d) {
				int grey = (r + g + b) / 3;
				b = grey;
				g = grey;
				r = grey;
			}

			this.grid[i * 4 + 0] = (byte) r;
			this.grid[i * 4 + 1] = (byte) g;
			this.grid[i * 4 + 2] = (byte) b;
			this.grid[i * 4 + 3] = (byte) a;
		}

		this.oldAnaglyph = this.render3d;
	}

	@Override
	public void updateTexture() {
		if (this.oldAnaglyph != this.render3d) {
			this.update();
		}

	}

	public static BufferedImage scale2x(BufferedImage in) {
		int width = in.getWidth();
		int height = in.getHeight();
		BufferedImage out = new BufferedImage(width * 2, height * 2, 2);

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int E = in.getRGB(x, y);
				int B;
				if (y == 0) {
					B = E;
				} else {
					B = in.getRGB(x, y - 1);
				}

				int D;
				if (x == 0) {
					D = E;
				} else {
					D = in.getRGB(x - 1, y);
				}

				int F;
				if (x >= width - 1) {
					F = E;
				} else {
					F = in.getRGB(x + 1, y);
				}

				int H;
				if (y >= height - 1) {
					H = E;
				} else {
					H = in.getRGB(x, y + 1);
				}

				int E0;
				int E1;
				int E2;
				int E3;
				if (B != H && D != F) {
					E0 = D == B ? D : E;
					E1 = B == F ? F : E;
					E2 = D == H ? D : E;
					E3 = H == F ? F : E;
				} else {
					E0 = E;
					E1 = E;
					E2 = E;
					E3 = E;
				}

				out.setRGB(x * 2, y * 2, E0);
				out.setRGB(x * 2 + 1, y * 2, E1);
				out.setRGB(x * 2, y * 2 + 1, E2);
				out.setRGB(x * 2 + 1, y * 2 + 1, E3);
			}
		}

		return out;
	}
}
