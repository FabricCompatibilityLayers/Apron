package shockahpi;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.world.World;

/**
 * AKA "GuiYesNoFreezeDifficulty"
 */
public class FreezeDifficultyScreen extends Screen {
	public World world;

	public FreezeDifficultyScreen(World world) {
		this.world = world;
	}
}
