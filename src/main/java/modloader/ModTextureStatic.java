package modloader;

import net.minecraft.client.render.TextureBinder;
import org.lwjgl.opengl.GL11;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ModTextureStatic extends TextureBinder {
	private boolean oldanaglyph;
	private int[] pixels;
	
	public ModTextureStatic(final int index, final int renderMode, final BufferedImage image) {
		this(index, 1, renderMode, image);
	}
	
	public ModTextureStatic(final int index, final int textureSize, final int renderMode, final BufferedImage image) {
		super(index);
		this.pixels = null;
		this.textureSize = textureSize;
		this.renderMode = renderMode;
		this.bindTexture(ModLoader.getMinecraftInstance().textureManager);
		final int targetWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096) / 16;
		final int targetHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097) / 16;
		final int width = image.getWidth();
		final int height = image.getHeight();
		this.pixels = new int[targetWidth * targetHeight];
		this.grid = new byte[targetWidth * targetHeight * 4];
		if (width != height || width != targetWidth) {
			final BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, 6);
			final Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, targetWidth, targetHeight, 0, 0, width, height, null);
			bufferedImage.getRGB(0, 0, targetWidth, targetHeight, this.pixels, 0, targetWidth);
			g.dispose();
		}
		else {
			image.getRGB(0, 0, width, height, this.pixels, 0, width);
		}
		this.update();
	}

	public void update() {
		for(int i = 0; i < this.pixels.length; ++i) {
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

			this.grid[i * 4 + 0] = (byte)r;
			this.grid[i * 4 + 1] = (byte)g;
			this.grid[i * 4 + 2] = (byte)b;
			this.grid[i * 4 + 3] = (byte)a;
		}

		this.oldanaglyph = this.render3d;
	}

	@Override
	public void updateTexture() {
		if (this.oldanaglyph != this.render3d) {
			this.update();
		}

	}
	
	public static BufferedImage scale2x(BufferedImage image) {
		int width = image.getWidth();
		final int height = image.getHeight();
		final BufferedImage bufferedImage = new BufferedImage(width * 2, height * 2, 2);
        
        int a, b, c, rgb1, rgb2, rgb3, rgb4;
        for (int x = 0; x < height; ++x) {
            for (int y = 0; y < width; ++y) {
                int rgb = image.getRGB(y, x);
                
                if (x == 0) {
                    a = rgb;
                }
				else {
                    a = image.getRGB(y, x - 1);
                }
                
                if (y == 0) {
                    b = rgb;
                }
				else {
                    b = image.getRGB(y - 1, x);
                }
				
				if (y >= width - 1) {
					c = rgb;
				}
				else {
					c = image.getRGB(y + 1, x);
				}
				
				if (x >= height - 1) {
					width = rgb;
				}
				else {
					width = image.getRGB(y, x + 1);
				}
				
				if (a != width && b != c) {
					rgb1 = ((b == a) ? b : rgb);
					rgb2 = ((a == c) ? c : rgb);
					rgb3 = ((b == width) ? b : rgb);
					rgb4 = ((width == c) ? c : rgb);
				}
				else {
					rgb1 = rgb;
					rgb2 = rgb;
					rgb3 = rgb;
					rgb4 = rgb;
				}
				
				bufferedImage.setRGB(y * 2, x * 2, rgb1);
				bufferedImage.setRGB(y * 2 + 1, x * 2, rgb2);
				bufferedImage.setRGB(y * 2, x * 2 + 1, rgb3);
				bufferedImage.setRGB(y * 2 + 1, x * 2 + 1, rgb4);
			}
		}
		
		return bufferedImage;
	}
}
