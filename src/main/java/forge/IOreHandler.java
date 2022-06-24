package forge;

import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IOreHandler {
	@Legacy
	void registerOre(String string, ItemStack arg);
}
