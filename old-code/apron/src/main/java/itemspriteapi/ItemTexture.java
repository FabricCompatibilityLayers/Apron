package itemspriteapi;

import net.minecraft.item.Item;

public class ItemTexture extends Item implements IItemTexture {
	public String texturePath;

	public ItemTexture(int i, String s) {
		super(i);
		this.texturePath = s;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public String getTextureFile() {
		return this.texturePath;
	}
}
