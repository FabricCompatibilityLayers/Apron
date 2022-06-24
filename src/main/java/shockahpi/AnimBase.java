package shockahpi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javax.imageio.ImageIO;

import io.github.betterthanupdates.Legacy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;

@Legacy
@SuppressWarnings("unused")
public abstract class AnimBase extends TextureBinder {
	@Legacy
	protected int[][] fileBuf;
	@Legacy
	protected int[][] frame;
	@Legacy
	protected int size;
	@Legacy
	public AnimBase.Mode mdSet = new AnimBase.Mode() {
		@Override
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, color);
		}
	};
	@Legacy
	public AnimBase.Mode mdAdd = new AnimBase.Mode() {
		@Override
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.add(new Color(AnimBase.this.frame[x][y]), color));
		}
	};
	@Legacy
	public AnimBase.Mode mdSubtract = new AnimBase.Mode() {
		@Override
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.subtract(new Color(AnimBase.this.frame[x][y]), color));
		}
	};
	@Legacy
	public AnimBase.Mode mdBlend = new AnimBase.Mode() {
		@Override
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.blend(new Color(AnimBase.this.frame[x][y]), color));
		}
	};

	@Legacy
	public AnimBase(int spriteID, String spritePath) {
		super(spriteID);
		this.size = (int)Math.sqrt((double)(this.grid.length / 4));
		this.fileBuf = new int[this.size][this.size];
		this.frame = new int[this.size][this.size];

		try {
			if (spritePath.isEmpty()) {
				BufferedImage bufImage = ImageIO.read(Minecraft.class.getResource(this.renderMode == 0 ? "/terrain.png" : "/gui/items.png"));
				int xx = spriteID % 16 * this.size;
				int yy = (int)Math.floor((double)(spriteID / 16)) * this.size;

				for(int y = 0; y < this.size; ++y) {
					for(int x = 0; x < this.size; ++x) {
						this.fileBuf[x][y] = bufImage.getRGB(xx + x, yy + y);
					}
				}
			} else {
				BufferedImage bufImage = ImageIO.read(Minecraft.class.getResource(spritePath));

				for(int y = 0; y < this.size; ++y) {
					for(int x = 0; x < this.size; ++x) {
						this.fileBuf[x][y] = bufImage.getRGB(x, y);
					}
				}
			}
		} catch (IOException var8) {
			var8.printStackTrace();
		}

	}

	@Legacy
	public void updateTexture() {
		this.getCleanFrame();
		this.animFrame();
		this.copyFrameToArray();
	}

	@Legacy
	public abstract void animFrame();

	@Legacy
	protected void getCleanFrame() {
		for(int y = 0; y < this.size; ++y) {
			for(int x = 0; x < this.size; ++x) {
				this.frame[x][y] = this.fileBuf[x][y];
			}
		}

	}

	@Legacy
	protected void copyFrameToArray() {
		for(int y = 0; y < this.size; ++y) {
			for(int x = 0; x < this.size; ++x) {
				int index = this.getXYIndex(x, y);
				this.grid[index * 4 + 0] = (byte)(this.frame[x][y] >> 16 & 0xFF);
				this.grid[index * 4 + 1] = (byte)(this.frame[x][y] >> 8 & 0xFF);
				this.grid[index * 4 + 2] = (byte)(this.frame[x][y] & 0xFF);
				this.grid[index * 4 + 3] = (byte)(this.frame[x][y] >> 24 & 0xFF);
			}
		}

	}

	@Legacy
	private void setPixel(int x, int y, Color color) {
		if (this.inImage(x, y)) {
			this.frame[x][y] = color.getRGB();
		}
	}

	@Legacy
	protected int getXYIndex(int x, int y) {
		return y * this.size + x;
	}

	@Legacy
	protected boolean inImage(int x, int y) {
		return x >= 0 && y >= 0 && x < this.size && y < this.size;
	}

	@Legacy
	protected void drawPoint(int x, int y, Color color) {
		this.drawPoint(x, y, color, this.mdSet);
	}

	@Legacy
	protected void drawPoint(int x, int y, Color color, AnimBase.Mode mode) {
		mode.draw(x, y, color);
	}

	@Legacy
	protected void drawRect(int x1, int y1, int x2, int y2, Color color) {
		this.drawRect(x1, y1, x2, y2, color, this.mdSet);
	}

	@Legacy
	protected void drawRect(int x1, int y1, int x2, int y2, Color color, AnimBase.Mode mode) {
		int xS = Math.min(x1, x2);
		int yS = Math.min(y1, y2);
		int xE = Math.max(x1, x2);
		int yE = Math.max(y1, y2);

		for(int y = yS; y < yE; ++y) {
			for(int x = xS; x < xE; ++x) {
				this.drawPoint(x, y, color, mode);
			}
		}

	}

	@Legacy
	protected void shiftFrame(int h, int v, boolean wrapH, boolean wrapV) {
		int[] line = new int[this.size];
		if (wrapH) {
			while(h < 0) {
				h += this.size;
			}

			h %= this.size;
		}

		if (wrapV) {
			while(v < 0) {
				v += this.size;
			}

			v %= this.size;
		}

		if (h != 0) {
			if (wrapH) {
				for(int y = 0; y < this.size; ++y) {
					for(int x = 0; x < this.size; ++x) {
						line[x] = this.frame[x][y];
					}

					for(int x = 0; x < this.size; ++x) {
						this.frame[x][y] = line[(x + h) % this.size];
					}
				}
			} else {
				for(int y = 0; y < this.size; ++y) {
					for(int x = 0; x < this.size; ++x) {
						line[x] = this.frame[x][y];
						this.frame[x][y] = 0;
					}

					for(int x = 0; x < this.size; ++x) {
						if (this.inImage(x + h, y)) {
							this.frame[x + h][y] = line[x];
						}
					}
				}
			}
		}

		if (v != 0) {
			if (wrapV) {
				for(int x = 0; x < this.size; ++x) {
					System.arraycopy(this.frame[x], 0, line, 0, this.size);

					for(int y = 0; y < this.size; ++y) {
						this.frame[x][y] = line[(y + v) % this.size];
					}
				}
			} else {
				for(int x = 0; x < this.size; ++x) {
					for(int y = 0; y < this.size; ++y) {
						line[y] = this.frame[x][y];
						this.frame[x][y] = 0;
					}

					for(int y = 0; y < this.size; ++y) {
						if (this.inImage(x, y + v)) {
							this.frame[x][y + v] = line[y];
						}
					}
				}
			}
		}

	}

	@Legacy
	protected void flipFrame(boolean h, boolean v) {
		if (h) {
			for(int x = 0; x < this.size / 2; ++x) {
				for(int y = 0; y < this.size; ++y) {
					int swap = this.frame[x][y];
					this.frame[x][y] = this.frame[this.size - 1 - x][y];
					this.frame[this.size - 1 - x][y] = swap;
				}
			}
		}

		if (v) {
			for(int y = 0; y < this.size / 2; ++y) {
				for(int x = 0; x < this.size; ++x) {
					int swap = this.frame[x][y];
					this.frame[x][y] = this.frame[x][this.size - 1 - y];
					this.frame[x][this.size - 1 - y] = swap;
				}
			}
		}

	}

	@Legacy
	public static Color add(Color c1, Color c2) {
		float value = (float)c2.getAlpha() / 255.0F;
		int R = c1.getRed();
		R = (int)((float)R + (float)c2.getRed() * value);
		R = Math.min(R, 255);
		int G = c1.getGreen();
		G = (int)((float)G + (float)c2.getGreen() * value);
		G = Math.min(G, 255);
		int B = c1.getBlue();
		B = (int)((float)B + (float)c2.getBlue() * value);
		B = Math.min(B, 255);
		int A = c1.getAlpha();
		return new Color(R, G, B, A);
	}

	@Legacy
	public static Color subtract(Color c1, Color c2) {
		float value = (float)c2.getAlpha() / 255.0F;
		int R = c1.getRed();
		R = (int)((float)R - (float)c2.getRed() * value);
		R = Math.max(R, 0);
		int G = c1.getGreen();
		G = (int)((float)G - (float)c2.getGreen() * value);
		G = Math.max(G, 0);
		int B = c1.getBlue();
		B = (int)((float)B - (float)c2.getBlue() * value);
		B = Math.max(B, 0);
		int A = c1.getAlpha();
		return new Color(R, G, B, A);
	}

	@Legacy
	public static Color merge(Color c1, Color c2, float value) {
		value = Math.min(Math.max(value, 0.0F), 1.0F);
		float R = (float)c1.getRed() - ((float)c1.getRed() - (float)c2.getRed()) * value;
		float G = (float)c1.getGreen() - ((float)c1.getGreen() - (float)c2.getGreen()) * value;
		float B = (float)c1.getBlue() - ((float)c1.getBlue() - (float)c2.getBlue()) * value;
		float A = (float)c1.getAlpha() - ((float)c1.getAlpha() - (float)c2.getAlpha()) * value;
		return new Color(R / 255.0F, G / 255.0F, B / 255.0F, A / 255.0F);
	}

	@Legacy
	public static Color blend(Color c1, Color c2) {
		float R = (float)c1.getRed() / 255.0F * ((float)c2.getRed() / 255.0F);
		float G = (float)c1.getGreen() / 255.0F * ((float)c2.getGreen() / 255.0F);
		float B = (float)c1.getBlue() / 255.0F * ((float)c2.getBlue() / 255.0F);
		float A = (float)c1.getAlpha() / 255.0F * ((float)c2.getAlpha() / 255.0F);
		return new Color(R, G, B, A);
	}

	@Legacy
	public abstract class Mode {
		@Legacy
		public Mode() {
		}

		@Legacy
		public abstract void draw(int i, int j, Color color);
	}
}
