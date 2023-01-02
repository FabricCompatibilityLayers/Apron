package io.github.betterthanupdates.apron.fixes.vanilla.infsprites;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.mine_diver.infsprites.proxy.TexturePackCustomProxy;
import net.mine_diver.infsprites.proxy.TexturePackDefaultProxy;
import net.mine_diver.infsprites.proxy.transform.IHasCor;
import net.mine_diver.infsprites.proxy.transform.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.TexturePackManager;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.resource.ZippedTexturePack;

public class TexturePackListProxy extends TexturePackManager implements IHasCor {
	@Shadow(
			obfuscatedName = "field_1176"
	)
	private List<TexturePack> field_1176;
	@Shadow(
			obfuscatedName = "field_1177"
	)
	private TexturePack field_1177;
	@Shadow(
			obfuscatedName = "field_1178"
	)
	private Map<String, TexturePack> field_1178;
	@Shadow(
			obfuscatedName = "field_1179"
	)
	private Minecraft field_1179;
	@Shadow(
			obfuscatedName = "field_1180"
	)
	private File field_1180;
	@Shadow(
			obfuscatedName = "field_1181"
	)
	private String field_1181;

	public TexturePackListProxy(Minecraft minecraft, File file) {
		super(minecraft, file);
	}

	@Override
	public void cor() {
		this.field_1176.clear();
		this.field_1177 = new TexturePackDefaultProxy();
		this.texturePack = null;
		this.field_1178.clear();
		this.findTexturePacks();
		this.texturePack.createZipFile();
	}

	@Override
	public void findTexturePacks() {
		List<TexturePack> arraylist = new ArrayList();
		this.texturePack = null;
		arraylist.add(this.field_1177);
		if (this.field_1180.exists() && this.field_1180.isDirectory()) {
			File[] afile = this.field_1180.listFiles();
			File[] afile1 = afile;
			int i = afile.length;

			for(int j = 0; j < i; ++j) {
				File file = afile1[j];
				if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
					String s = file.getName() + ":" + file.length() + ":" + file.lastModified();

					if (!this.field_1178.containsKey(s)) {
						ZippedTexturePack texturepackcustom = new TexturePackCustomProxy(file);
						texturepackcustom.field_1140 = s;
						this.field_1178.put(s, texturepackcustom);
						texturepackcustom.method_977(this.field_1179);
					}

					TexturePack texturepackbase1 = (TexturePack)this.field_1178.get(s);
					if (texturepackbase1.name.equals(this.field_1181)) {
						this.texturePack = texturepackbase1;
					}

					arraylist.add(texturepackbase1);
				}
			}
		}

		if (this.texturePack == null) {
			this.texturePack = this.field_1177;
		}

		this.field_1176.removeAll(arraylist);
		Iterator<TexturePack> iterator = this.field_1176.iterator();

		while(iterator.hasNext()) {
			TexturePack texturepackbase = (TexturePack)iterator.next();
			texturepackbase.method_979(this.field_1179);
			this.field_1178.remove(texturepackbase.field_1140);
		}

		this.field_1176 = arraylist;
	}
}
