package shockahpi;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.class_336;
import net.minecraft.client.Minecraft;

import io.github.betterthanupdates.Legacy;

@Legacy
@SuppressWarnings("unused")
public abstract class AnimBase extends class_336 {
	protected int[][] fileBuf;
	protected int[][] frame;
	protected int size;
	public Mode mdSet = new Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, color);
		}
	};
	public Mode mdAdd = new Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.add(new Color(AnimBase.this.frame[x][y]), color));
		}
	};
	public Mode mdSubtract = new Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.subtract(new Color(AnimBase.this.frame[x][y]), color));
		}
	};
	public Mode mdBlend = new Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.blend(new Color(AnimBase.this.frame[x][y]), color));
		}
	};

	public AnimBase(int spriteID, String spritePath) {
		super(spriteID);
		this.size = (int)Math.sqrt((double)(this.field_1411.length / 4));
		this.fileBuf = new int[this.size][this.size];
		this.frame = new int[this.size][this.size];

		try {
			BufferedImage bufImage;
			int xx;
			int yy;
			if (spritePath.isEmpty()) {
				bufImage = ImageIO.read(Minecraft.class.getResource(this.field_1416 == 0 ? "/terrain.png" : "/gui/items.png"));
				xx = spriteID % 16 * this.size;
				yy = (int)Math.floor((double)(spriteID / 16)) * this.size;

				for (int y = 0; y < this.size; ++y) {
					for (int x = 0; x < this.size; ++x) {
						this.fileBuf[x][y] = bufImage.getRGB(xx + x, yy + y);
					}
				}
			} else {
				bufImage = ImageIO.read(Minecraft.class.getResource(spritePath));

				for(xx = 0; xx < this.size; ++xx) {
					for(yy = 0; yy < this.size; ++yy) {
						this.fileBuf[yy][xx] = bufImage.getRGB(yy, xx);
					}
				}
			}
		} catch (IOException var8) {
			var8.printStackTrace();
		}
	}

	public void updateTexture() {
		this.getCleanFrame();
		this.animFrame();
		this.copyFrameToArray();
	}

	public abstract void animFrame();

	protected void getCleanFrame() {
		for (int y = 0; y < this.size; ++y) {
			for (int x = 0; x < this.size; ++x) {
				this.frame[x][y] = this.fileBuf[x][y];
			}
		}
	}

	protected void copyFrameToArray() {
		for (int y = 0; y < this.size; ++y) {
			for (int x = 0; x < this.size; ++x) {
				int index = this.getXYIndex(x, y);
				this.field_1411[index * 4 + 0] = (byte) (this.frame[x][y] >> 16 & 0xFF);
				this.field_1411[index * 4 + 1] = (byte) (this.frame[x][y] >> 8 & 0xFF);
				this.field_1411[index * 4 + 2] = (byte) (this.frame[x][y] & 0xFF);
				this.field_1411[index * 4 + 3] = (byte) (this.frame[x][y] >> 24 & 0xFF);
			}
		}
	}

	private void setPixel(int x, int y, Color color) {
		if (this.inImage(x, y)) {
			this.frame[x][y] = color.getRGB();
		}
	}

	protected int getXYIndex(int x, int y) {
		return y * this.size + x;
	}

	protected boolean inImage(int x, int y) {
		return x >= 0 && y >= 0 && x < this.size && y < this.size;
	}

	protected void drawPoint(int x, int y, Color color) {
		this.drawPoint(x, y, color, this.mdSet);
	}

	protected void drawPoint(int x, int y, Color color, Mode mode) {
		mode.draw(x, y, color);
	}

	protected void drawRect(int x1, int y1, int x2, int y2, Color color) {
		this.drawRect(x1, y1, x2, y2, color, this.mdSet);
	}

	protected void drawRect(int x1, int y1, int x2, int y2, Color color, Mode mode) {
		int xS = Math.min(x1, x2);
		int yS = Math.min(y1, y2);
		int xE = Math.max(x1, x2);
		int yE = Math.max(y1, y2);

		for (int y = yS; y < yE; ++y) {
			for (int x = xS; x < xE; ++x) {
				this.drawPoint(x, y, color, mode);
			}
		}
	}

	protected void shiftFrame(int h, int v, boolean wrapH, boolean wrapV) {
		int[] line = new int[this.size];
		if (wrapH) {
			while(true) {
				if (h >= 0) {
					h %= this.size;
					break;
				}

				h += this.size;
			}
		}

		if (wrapV) {
			while (v < 0) {
				v += this.size;
			}

			v %= this.size;
		}

		int x;
		int y;
		if (h != 0) {
			if (wrapH) {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[y][x];
					}

					for(y = 0; y < this.size; ++y) {
						this.frame[y][x] = line[(y + h) % this.size];
					}
				}
			} else {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[y][x];
						this.frame[y][x] = 0;
					}

					for(y = 0; y < this.size; ++y) {
						if (this.inImage(y + h, x)) {
							this.frame[y + h][x] = line[y];
						}
					}
				}
			}
		}

		if (v != 0) {
			if (wrapV) {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[x][y];
					}

					for(y = 0; y < this.size; ++y) {
						this.frame[x][y] = line[(y + v) % this.size];
					}
				}
			} else {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[x][y];
						this.frame[x][y] = 0;
					}

					for(y = 0; y < this.size; ++y) {
						if (this.inImage(x, y + v)) {
							this.frame[x][y + v] = line[y];
						}
					}
				}
			}
		}
	}

	protected void flipFrame(boolean h, boolean v) {
		int swap;
		int y;
		int x;
		if (h) {
			for(y = 0; y < this.size / 2; ++y) {
				for(x = 0; x < this.size; ++x) {
					swap = this.frame[y][x];
					this.frame[y][x] = this.frame[this.size - 1 - y][x];
					this.frame[this.size - 1 - y][x] = swap;
				}
			}
		}

		if (v) {
			for(y = 0; y < this.size / 2; ++y) {
				for(x = 0; x < this.size; ++x) {
					swap = this.frame[x][y];
					this.frame[x][y] = this.frame[x][this.size - 1 - y];
					this.frame[x][this.size - 1 - y] = swap;
				}
			}
		}
	}

	public static Color add(Color c1, Color c2) {
		float value = (float) c2.getAlpha() / 255.0F;
		int R = c1.getRed();
		R = (int) ((float) R + (float) c2.getRed() * value);
		R = Math.min(R, 255);
		int G = c1.getGreen();
		G = (int) ((float) G + (float) c2.getGreen() * value);
		G = Math.min(G, 255);
		int B = c1.getBlue();
		B = (int) ((float) B + (float) c2.getBlue() * value);
		B = Math.min(B, 255);
		int A = c1.getAlpha();
		return new Color(R, G, B, A);
	}

	public static Color subtract(Color c1, Color c2) {
		float value = (float) c2.getAlpha() / 255.0F;
		int R = c1.getRed();
		R = (int) ((float) R - (float) c2.getRed() * value);
		R = Math.max(R, 0);
		int G = c1.getGreen();
		G = (int) ((float) G - (float) c2.getGreen() * value);
		G = Math.max(G, 0);
		int B = c1.getBlue();
		B = (int) ((float) B - (float) c2.getBlue() * value);
		B = Math.max(B, 0);
		int A = c1.getAlpha();
		return new Color(R, G, B, A);
	}

	public static Color merge(Color c1, Color c2, float value) {
		value = Math.min(Math.max(value, 0.0F), 1.0F);
		float R = (float) c1.getRed() - ((float) c1.getRed() - (float) c2.getRed()) * value;
		float G = (float) c1.getGreen() - ((float) c1.getGreen() - (float) c2.getGreen()) * value;
		float B = (float) c1.getBlue() - ((float) c1.getBlue() - (float) c2.getBlue()) * value;
		float A = (float) c1.getAlpha() - ((float) c1.getAlpha() - (float) c2.getAlpha()) * value;
		return new Color(R / 255.0F, G / 255.0F, B / 255.0F, A / 255.0F);
	}

	public static Color blend(Color c1, Color c2) {
		float R = (float) c1.getRed() / 255.0F * ((float) c2.getRed() / 255.0F);
		float G = (float) c1.getGreen() / 255.0F * ((float) c2.getGreen() / 255.0F);
		float B = (float) c1.getBlue() / 255.0F * ((float) c2.getBlue() / 255.0F);
		float A = (float) c1.getAlpha() / 255.0F * ((float) c2.getAlpha() / 255.0F);
		return new Color(R, G, B, A);
	}

	public abstract class Mode {
		public Mode() {
		}

		public abstract void draw(int i, int j, Color color);
	}
}
