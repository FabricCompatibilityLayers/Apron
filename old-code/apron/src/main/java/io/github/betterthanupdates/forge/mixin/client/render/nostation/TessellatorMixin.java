package io.github.betterthanupdates.forge.mixin.client.render.nostation;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

@Mixin(Tessellator.class)
public abstract class TessellatorMixin {
	@Shadow
	private int vertexCount;
	@Shadow
	private boolean hasColor;
	@Shadow
	private boolean hasTexture;
	@Shadow
	private boolean hasNormals;
	@Shadow
	private int bufferPosition;
	@Shadow
	private int addedVertexCount;
	@Shadow
	private boolean colorDisabled;
	@Shadow
	public boolean drawing;
	@Shadow
	private int field_2079;
	@Shadow
	private int[] buffer;

	@Shadow
	protected abstract void reset();

	@Shadow
	public int mode;
	@Shadow
	private static boolean useTriangles;
	@Shadow
	private FloatBuffer floatBuffer;
	@Shadow
	private boolean field_2077;
	@Shadow
	private ByteBuffer byteBuffer;
	@Shadow
	private IntBuffer field_2078;
	@Shadow
	private int field_2052;
	@Shadow
	private IntBuffer intBuffer;

	// Forge Fields
	@Unique
	private int rawBufferSize;
	@Unique
	private static final int NATIVE_BUFFER_SIZE = 2097152;
	@Unique
	private static final int TRI_VERTS_IN_BUFFER = NATIVE_BUFFER_SIZE / 48 * 6;

	/**
	 * @author Eloraam
	 * @reason method instruction order is different from vanilla.
	 */
	@Inject(method = "<init>", at = @At("RETURN"))
	private void ctr$overwrite(int par1, CallbackInfo ci) {
		this.vertexCount = 0;
		this.hasColor = false;
		this.hasTexture = false;
		this.hasNormals = false;
		this.bufferPosition = 0;
		this.addedVertexCount = 0;
		this.colorDisabled = false;
		this.drawing = false;
		this.field_2079 = 0;
		this.buffer = null;
		this.rawBufferSize = 0;
	}

	/**
	 * @author Eloraam
	 * @reason Multi-tessellator
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

				if (this.mode == 7 && useTriangles) {
					vtc = Math.min(this.vertexCount - offs, TRI_VERTS_IN_BUFFER);
				} else {
					vtc = Math.min(this.vertexCount - offs, NATIVE_BUFFER_SIZE >> 5);
				}

				intBuffer.clear();
				intBuffer.put(this.buffer, offs * 8, vtc * 8);
				byteBuffer.position(0);
				byteBuffer.limit(vtc * 32);
				offs += vtc;

				if (field_2077) {
					this.field_2079 = (this.field_2079 + 1) % field_2052;
					ARBVertexBufferObject.glBindBufferARB(34962, field_2078.get(this.field_2079));
					ARBVertexBufferObject.glBufferDataARB(34962, byteBuffer, 35040);
				}

				if (this.hasTexture) {
					if (field_2077) {
						GL11.glTexCoordPointer(2, 5126, 32, 12L);
					} else {
						floatBuffer.position(3);
						GL11.glTexCoordPointer(2, 32, floatBuffer);
					}

					GL11.glEnableClientState(32888);
				}

				if (this.hasColor) {
					if (field_2077) {
						GL11.glColorPointer(4, 5121, 32, 20L);
					} else {
						byteBuffer.position(20);
						GL11.glColorPointer(4, true, 32, byteBuffer);
					}

					GL11.glEnableClientState(32886);
				}

				if (this.hasNormals) {
					if (field_2077) {
						GL11.glNormalPointer(5120, 32, 24L);
					} else {
						byteBuffer.position(24);
						GL11.glNormalPointer(32, byteBuffer);
					}

					GL11.glEnableClientState(32885);
				}

				if (field_2077) {
					GL11.glVertexPointer(3, 5126, 32, 0L);
				} else {
					floatBuffer.position(0);
					GL11.glVertexPointer(3, 32, floatBuffer);
				}

				GL11.glEnableClientState(32884);

				if (this.mode == 7 && useTriangles) {
					GL11.glDrawArrays(4, 0, vtc);
				} else {
					GL11.glDrawArrays(this.mode, 0, vtc);
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

			if (this.rawBufferSize > 131072 && this.bufferPosition < this.rawBufferSize << 3) {
				this.rawBufferSize = 0;
				this.buffer = null;
			}

			this.reset();
		}
	}

	/**
	 * @author Eloraam
	 * @reason method instruction order is different from vanilla.
	 */
	@Inject(method = "vertex(DDD)V", at = @At("HEAD"))
	private void forge$addVertex$1(double d, double d1, double d2, CallbackInfo ci) {
		if (this.bufferPosition >= this.rawBufferSize - 32) {
			if (this.rawBufferSize == 0) {
				this.rawBufferSize = 65536;
				this.buffer = new int[this.rawBufferSize];
			} else {
				this.rawBufferSize *= 2;
				this.buffer = Arrays.copyOf(this.buffer, this.rawBufferSize);
			}
		}
	}

	/**
	 * @author Eloraam
	 * @reason method instruction order is different from vanilla.
	 */
	@Inject(method = "vertex(DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;draw()V"), cancellable = true)
	private void forge$addVertex$2(double d, double d1, double d2, CallbackInfo ci) {
		ci.cancel();
	}
}
