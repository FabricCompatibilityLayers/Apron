package forge;

import java.util.ArrayList;

import io.github.betterthanupdates.Legacy;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Legacy
public interface IShearable {
	@Legacy
	boolean isShearable(ItemStack arg, World arg2, int i, int j, int k);

	@Legacy
	ArrayList<ItemStack> onSheared(ItemStack arg, World arg2, int i, int j, int k);
}
