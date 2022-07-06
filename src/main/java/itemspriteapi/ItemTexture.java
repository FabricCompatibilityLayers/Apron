package itemspriteapi;

import net.minecraft.item.Item;

public class ItemTexture extends Item implements IItemTexture {
	public String texturePath;

	public ItemTexture(int i, String s) {
		super(i);
		this.texturePath = s;
		this.setHasSubItems(true);
		this.setDurability(0);
	}

	public String getTextureFile() {
		return this.texturePath;
	}
}
