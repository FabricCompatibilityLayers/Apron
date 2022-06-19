package io.github.betterthanupdates.forge.mixin;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Tessellator;

import io.github.betterthanupdates.forge.client.render.ForgeTessellator;

// FIXME
@Mixin(Tessellator.class)
public abstract class TessellatorMixin implements ForgeTessellator {
	@Shadow
	public static Tessellator INSTANCE;
	@Shadow
	private int vertexCount;
	@Shadow
	private boolean hasColor;
	@Shadow
	private boolean hasTexture;
	@Shadow
	private boolean hasNormals;
	@Shadow
	private int field_2068;
	@Shadow
	private int vertexAmount;
	@Shadow
	private boolean disableColor;
	@Shadow
	public boolean drawing;
	@Shadow
	private int field_2079;
	@Shadow
	private int[] bufferArray;
	@Shadow
	private boolean useFloatBuffer;
	@Shadow
	private int field_2052;
	@Shadow
	private IntBuffer field_2078;
	@Shadow
	private ByteBuffer byteBuffer;
	@Shadow
	private IntBuffer intBuffer;
	@Shadow
	private FloatBuffer floatBuffer;
	@Shadow
	public int drawingMode;
	@Shadow
	private static boolean useTriangles;

	@Shadow
	protected abstract void clear();

	@Shadow
	private double textureX;
	@Shadow
	private double textureY;
	@Shadow
	private int color;
	@Shadow
	private int normal;
	@Shadow
	public double xOffset;
	@Shadow
	public double yOffset;
	@Shadow
	public double zOffset;
	// Forge Fields
	@Unique
	public boolean defaultTexture = false;
	@Unique
	private int rawBufferSize;
	@Unique
	private static int nativeBufferSize = 2097152;
	@Unique
	private static int trivertsInBuffer = nativeBufferSize / 48 * 6;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void ctr$overwrite(int par1, CallbackInfo ci) {
		this.vertexCount = 0;
		this.hasColor = false;
		this.hasTexture = false;
		this.hasNormals = false;
		this.field_2068 = 0;
		this.vertexAmount = 0;
		this.disableColor = false;
		this.drawing = false;
		this.field_2079 = 0;
		this.bufferArray = null;
		this.rawBufferSize = 0;
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void classCtr$overwrite(CallbackInfo ci) {
		((TessellatorMixin) (Object) INSTANCE).defaultTexture = true;

		// TODO
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void draw() {
		if (!this.drawing) {
			throw new IllegalStateException("Not tesselating!");
		} else {
			this.drawing = false;
			int offs = 0;

			while (offs < this.vertexCount) {
				int vtc;

				if (this.drawingMode == 7 && useTriangles) {
					vtc = Math.min(this.vertexCount - offs, trivertsInBuffer);
				} else {
					vtc = Math.min(this.vertexCount - offs, nativeBufferSize >> 5);
				}

				((Buffer) intBuffer).clear();
				intBuffer.put(this.bufferArray, offs * 8, vtc * 8);
				((Buffer) byteBuffer).position(0);
				((Buffer) byteBuffer).limit(vtc * 32);
				offs += vtc;

				if (useFloatBuffer) {
					this.field_2079 = (this.field_2079 + 1) % field_2052;
					ARBVertexBufferObject.glBindBufferARB(34962, field_2078.get(this.field_2079));
					ARBVertexBufferObject.glBufferDataARB(34962, byteBuffer, 35040);
				}

				if (this.hasTexture) {
					if (useFloatBuffer) {
						GL11.glTexCoordPointer(2, 5126, 32, 12L);
					} else {
						floatBuffer.position(3);
						GL11.glTexCoordPointer(2, 32, floatBuffer);
					}

					GL11.glEnableClientState(32888);
				}

				if (this.hasColor) {
					if (useFloatBuffer) {
						GL11.glColorPointer(4, 5121, 32, 20L);
					} else {
						((Buffer) byteBuffer).position(20);
						GL11.glColorPointer(4, true, 32, byteBuffer);
					}

					GL11.glEnableClientState(32886);
				}

				if (this.hasNormals) {
					if (useFloatBuffer) {
						GL11.glNormalPointer(5120, 32, 24L);
					} else {
						((Buffer) byteBuffer).position(24);
						GL11.glNormalPointer(32, byteBuffer);
					}

					GL11.glEnableClientState(32885);
				}

				if (useFloatBuffer) {
					GL11.glVertexPointer(3, 5126, 32, 0L);
				} else {
					((Buffer) floatBuffer).position(0);
					GL11.glVertexPointer(3, 32, floatBuffer);
				}

				GL11.glEnableClientState(32884);

				if (this.drawingMode == 7 && useTriangles) {
					GL11.glDrawArrays(4, 0, vtc);
				} else {
					GL11.glDrawArrays(this.drawingMode, 0, vtc);
				}

				GL11.glDisableClientState(32884);

				if (this.hasTexture) {
					GL11.glDisableClientState(32888);
				}

				if (this.hasColor) {
					GL11.glDisableClientState(32886);
				}

				if (this.hasNormals) {
					GL11.glDisableClientState(32885);
				}
			}

			if (this.rawBufferSize > 131072 && this.field_2068 < this.rawBufferSize << 3) {
				this.rawBufferSize = 0;
				this.bufferArray = null;
			}

			this.clear();
		}
	}

	/**
	 * @author Forge
	 * @reason method instruction order is different from vanilla.
	 */
	@Overwrite
	public void addVertex(double d, double d1, double d2) {
		if (this.field_2068 >= this.rawBufferSize - 32) {
			if (this.rawBufferSize == 0) {
				this.rawBufferSize = 65536;
				this.bufferArray = new int[this.rawBufferSize];
			} else {
				this.rawBufferSize *= 2;
				this.bufferArray = Arrays.copyOf(this.bufferArray, this.rawBufferSize);
			}
		}

		++this.vertexAmount;

		if (this.drawingMode == 7 && useTriangles && this.vertexAmount % 4 == 0) {
			for (int i = 0; i < 2; ++i) {
				int j = 8 * (3 - i);

				if (this.hasTexture) {
					this.bufferArray[this.field_2068 + 3] = this.bufferArray[this.field_2068 - j + 3];
					this.bufferArray[this.field_2068 + 4] = this.bufferArray[this.field_2068 - j + 4];
				}

				if (this.hasColor) {
					this.bufferArray[this.field_2068 + 5] = this.bufferArray[this.field_2068 - j + 5];
				}

				this.bufferArray[this.field_2068 + 0] = this.bufferArray[this.field_2068 - j + 0];
				this.bufferArray[this.field_2068 + 1] = this.bufferArray[this.field_2068 - j + 1];
				this.bufferArray[this.field_2068 + 2] = this.bufferArray[this.field_2068 - j + 2];
				++this.vertexCount;
				this.field_2068 += 8;
			}
		}

		if (this.hasTexture) {
			this.bufferArray[this.field_2068 + 3] = Float.floatToRawIntBits((float) this.textureX);
			this.bufferArray[this.field_2068 + 4] = Float.floatToRawIntBits((float) this.textureY);
		}

		if (this.hasColor) {
			this.bufferArray[this.field_2068 + 5] = this.color;
		}

		if (this.hasNormals) {
			this.bufferArray[this.field_2068 + 6] = this.normal;
		}

		this.bufferArray[this.field_2068 + 0] = Float.floatToRawIntBits((float) (d + this.xOffset));
		this.bufferArray[this.field_2068 + 1] = Float.floatToRawIntBits((float) (d1 + this.yOffset));
		this.bufferArray[this.field_2068 + 2] = Float.floatToRawIntBits((float) (d2 + this.zOffset));
		this.field_2068 += 8;
		++this.vertexCount;
	}

	@Override
	public boolean defaultTexture() {
		return this.defaultTexture;
	}

	@Override
	public void defaultTexture(boolean defaultTexture) {
		this.defaultTexture = defaultTexture;
	}
}
