package forge;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

@Legacy
public interface IShearable {
	boolean isShearable(ItemStack itemStack, World world, int x, int y, int z);

	ArrayList<ItemStack> onSheared(ItemStack itemStack, World world, int x, int y, int z);
}
