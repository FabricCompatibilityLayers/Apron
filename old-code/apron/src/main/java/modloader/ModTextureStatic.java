package modloader;

import java.awt.*;
import java.awt.image.BufferedImage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_336;
import org.lwjgl.opengl.GL11;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;
import io.github.betterthanupdates.apron.impl.client.ApronClientImpl;

@SuppressWarnings("unused")
@Legacy
@Environment(EnvType.CLIENT)
public class ModTextureStatic extends class_336 {
	private boolean oldAnaglyph;
	private final int[] pixels;

	public ModTextureStatic(int slot, int dst, BufferedImage source) {
		this(slot, 1, dst, source);
	}

	public ModTextureStatic(int slot, int size, int dst, BufferedImage source) {
		super(slot);
		this.field_1415 = size;
		this.field_1416 = dst;
		this.method_1206(((ApronClientImpl) ApronApi.getInstance()).getTextureManager());
		int targetWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096) / 16;
		int targetHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097) / 16;
		int width = source.getWidth();
		int height = source.getHeight();
		this.pixels = new int[targetWidth * targetHeight];
		this.field_1411 = new byte[targetWidth * targetHeight * 4];

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

			if (this.field_1413) {
				int grey = (r + g + b) / 3;
				b = grey;
				g = grey;
				r = grey;
			}

			this.field_1411[i * 4 + 0] = (byte) r;
			this.field_1411[i * 4 + 1] = (byte) g;
			this.field_1411[i * 4 + 2] = (byte) b;
			this.field_1411[i * 4 + 3] = (byte) a;
		}

		this.oldAnaglyph = this.field_1413;
	}

	@Override
	public void method_1205() {
		if (this.oldAnaglyph != this.field_1413) {
			this.update();
		}
	}

	public static BufferedImage scale2x(BufferedImage in) {
		int width = in.getWidth();
		int height = in.getHeight();
		BufferedImage out = new BufferedImage(width * 2, height * 2, 2);

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int rgb = in.getRGB(x, y);
				int b, d, f, h;

				if (y == 0) {
					b = rgb;
				} else {
					b = in.getRGB(x, y - 1);
				}

				if (x == 0) {
					d = rgb;
				} else {
					d = in.getRGB(x - 1, y);
				}

				if (x >= width - 1) {
					f = rgb;
				} else {
					f = in.getRGB(x + 1, y);
				}

				if (y >= height - 1) {
					h = rgb;
				} else {
					h = in.getRGB(x, y + 1);
				}

				int e0, e1, e2, e3;

				if (b != h && d != f) {
					e0 = d == b ? d : rgb;
					e1 = b == f ? f : rgb;
					e2 = d == h ? d : rgb;
					e3 = h == f ? f : rgb;
				} else {
					e0 = rgb;
					e1 = rgb;
					e2 = rgb;
					e3 = rgb;
				}

				out.setRGB(x * 2, y * 2, e0);
				out.setRGB(x * 2 + 1, y * 2, e1);
				out.setRGB(x * 2, y * 2 + 1, e2);
				out.setRGB(x * 2 + 1, y * 2 + 1, e3);
			}
		}

		return out;
	}
}
