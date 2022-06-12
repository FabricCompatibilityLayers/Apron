package shockahpi;

import net.minecraft.stat.achievement.Achievement;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Random;

/**
 * Achievement Page
 * @author ShockAh
 */
@SuppressWarnings("unused")
public class AchievementPage {
	public static final AchievementPage DEFAULT = new AchievementPage();
	private static int nextID = 1;
	final int id;
	public final String title;
	final ArrayList<Integer> list = new ArrayList<>();

	private AchievementPage() {
		id = 0;
		title = "Minecraft";
		SAPI.acPageAdd(this);
	}

	public AchievementPage(String title) {
		id = nextID++;
		this.title = title;
		SAPI.acPageAdd(this);
	}

	public void addAchievements(Achievement... achievements) {
		for(Achievement achievement : achievements) {
			list.add(achievement.id);
		}
	}

	public int bgGetSprite(Random random, int x, int y) {
		int sprite = random.nextInt(1 + y) + y / 2;

		switch (sprite) {
			default: return Block.SAND.texture;
			case 0: return Block.DIRT.texture;
			case 4: return Block.STONE.texture;
			case 8: return Block.COAL_ORE.texture;
			case 22: return (random.nextInt(2) != 0 ? Block.REDSTONE_ORE.texture : Block.DIAMOND_ORE.texture);
			case 35: return Block.BEDROCK.texture;
		}
	}
}
