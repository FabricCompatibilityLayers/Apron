package forge;

import io.github.betterthanupdates.Legacy;
import net.minecraft.item.ItemStack;

@Legacy
public interface IOreHandler {
	@Legacy
	void registerOre(String string, ItemStack arg);
}
