package forge;

import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IOreHandler {
	void registerOre(String string, ItemStack itemStack);
}
