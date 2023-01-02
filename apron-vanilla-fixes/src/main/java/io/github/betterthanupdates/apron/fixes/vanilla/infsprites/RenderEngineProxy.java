package io.github.betterthanupdates.apron.fixes.vanilla.infsprites;

import net.mine_diver.infsprites.proxy.transform.Shadow;
import net.minecraft.client.TexturePackManager;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.List;

public abstract class RenderEngineProxy extends TextureManager {
	@Shadow(
			obfuscatedName = "field_1250"
	)
	private ByteBuffer field_1250;
	@Shadow(
			obfuscatedName = "field_1251"
	)
	private List<TextureBinder> field_1251;
	@Shadow(
			obfuscatedName = "field_1253"
	)
	private GameOptions field_1253;
	private boolean fxProxy;
	private int atlasId;

	@Shadow(obfuscatedName = "method_1086")
	protected abstract int method_1086(int i, int j);

	public RenderEngineProxy(TexturePackManager texturepacklist, GameOptions gamesettings) {
		super(texturepacklist, gamesettings);
	}

	@Override
	public int getTextureId(String s) {
		if (fxProxy) {
			switch (s) {
				case "/terrain.png":
					fxProxy = false;
					int terrainId = net.mine_diver.infsprites.render.TextureManager.getTerrain(atlasId);
					fxProxy = true;
					return terrainId;
				case "/gui/items.png":
					fxProxy = false;
					int guiItemsId = net.mine_diver.infsprites.render.TextureManager.getGuiItems(atlasId);
					fxProxy = true;
					return guiItemsId;
			}
		}
		return super.getTextureId(s);
	}

	@Override
	public void tick() {
		for (int i = 0; i < field_1251.size(); i++) {
			TextureBinder texturefx = (TextureBinder) field_1251.get(i);
			texturefx.render3d = field_1253.anaglyph3d;
			texturefx.updateTexture();
			field_1250.clear();
			field_1250.put(texturefx.grid);
			field_1250.position(0).limit(texturefx.grid.length);
			int textureId = texturefx.index;
			atlasId = texturefx.index >> 8;
			if (atlasId > 0) {
				fxProxy = true;
				texturefx.index -= atlasId << 8;
			}
			texturefx.bindTexture(this);
			fxProxy = false;
			for (int k = 0; k < texturefx.textureSize; k++) {
				for (int i1 = 0; i1 < texturefx.textureSize; i1++) {
					GL11.glTexSubImage2D(3553 /* GL_TEXTURE_2D */, 0, (texturefx.index % 16) * 16 + k * 16,
							(texturefx.index / 16) * 16 + i1 * 16, 16, 16, 6408 /* GL_RGBA */,
							5121 /* GL_UNSIGNED_BYTE */, field_1250);
					if (!field_1245)
						continue;
					int k1 = 1;
					while (k < 5) {
						int i2 = 16 >> k1 - 1;
						int k2 = 16 >> k1;
						for (int i3 = 0; i3 < k2; i3++)
							for (int k3 = 0; k3 < k2; k3++) {
								int i4 = field_1250.getInt((i3 * 2 + 0 + (k3 * 2 + 0) * i2) * 4);
								int k4 = field_1250.getInt((i3 * 2 + 1 + (k3 * 2 + 0) * i2) * 4);
								int i5 = field_1250.getInt((i3 * 2 + 1 + (k3 * 2 + 1) * i2) * 4);
								int k5 = field_1250.getInt((i3 * 2 + 0 + (k3 * 2 + 1) * i2) * 4);
								int l5 = method_1086(method_1086(i4, k4), method_1086(i5, k5));
								field_1250.putInt((i3 + k3 * k2) * 4, l5);
							}
						GL11.glTexSubImage2D(3553 /* GL_TEXTURE_2D */, k1, (texturefx.index % 16) * k2,
								(texturefx.index / 16) * k2, k2, k2, 6408 /* GL_RGBA */,
								5121 /* GL_UNSIGNED_BYTE */, field_1250);
						k1++;
					}
				}
			}
			texturefx.index = textureId;
		}
		for (int j = 0; j < field_1251.size(); j++) {
			TextureBinder texturefx1 = (TextureBinder) field_1251.get(j);
			if (texturefx1.id <= 0)
				continue;
			field_1250.clear();
			field_1250.put(texturefx1.grid);
			field_1250.position(0).limit(texturefx1.grid.length);
			GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, texturefx1.id);
			GL11.glTexSubImage2D(3553 /* GL_TEXTURE_2D */, 0, 0, 0, 16, 16, 6408 /* GL_RGBA */,
					5121 /* GL_UNSIGNED_BYTE */, field_1250);
			if (!field_1245)
				continue;
			int l = 1;
			while (l < 5) {
				int j1 = 16 >> l - 1;
				int l1 = 16 >> l;
				for (int j2 = 0; j2 < l1; j2++)
					for (int l2 = 0; l2 < l1; l2++) {
						int j3 = field_1250.getInt((j2 * 2 + 0 + (l2 * 2 + 0) * j1) * 4);
						int l3 = field_1250.getInt((j2 * 2 + 1 + (l2 * 2 + 0) * j1) * 4);
						int j4 = field_1250.getInt((j2 * 2 + 1 + (l2 * 2 + 1) * j1) * 4);
						int l4 = field_1250.getInt((j2 * 2 + 0 + (l2 * 2 + 1) * j1) * 4);
						int j5 = method_1086(method_1086(j3, l3), method_1086(j4, l4));
						field_1250.putInt((j2 + l2 * l1) * 4, j5);
					}
				GL11.glTexSubImage2D(3553 /* GL_TEXTURE_2D */, l, 0, 0, l1, l1, 6408 /* GL_RGBA */,
						5121 /* GL_UNSIGNED_BYTE */, field_1250);
				l++;
			}
		}
	}
}
