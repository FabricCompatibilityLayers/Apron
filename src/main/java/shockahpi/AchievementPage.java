package shockahpi;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.stat.achievement.Achievement;

/**
 * Achievement Page.
 * @author ShockAh
 */
@SuppressWarnings("unused")
public class AchievementPage {
	public static final AchievementPage DEFAULT = new AchievementPage();
	private static int nextID = 1;
	final int id;
	public final String title;
	final ArrayList<Integer> list = new ArrayList<>();

	public AchievementPage() {
		this.id = 0;
		this.title = "Minecraft";
		SAPI.acPageAdd(this);
	}

	public AchievementPage(String title) {
		this.id = nextID++;
		this.title = title;
		SAPI.acPageAdd(this);
	}

	public void addAchievements(Achievement... achievements) {
		for (Achievement achievement : achievements) {
			this.list.add(achievement.id);
		}
	}

	public int bgGetSprite(Random random, int x, int y) {
		int sprite = random.nextInt(1 + y) + y / 2;

		switch (sprite) {
			case 0:
				return Block.DIRT.texture;
			case 4:
				return Block.STONE.texture;
			case 8:
				return Block.COAL_ORE.texture;
			case 22:
				return (random.nextInt(2) != 0 ? Block.REDSTONE_ORE.texture : Block.DIAMOND_ORE.texture);
			case 35:
				return Block.BEDROCK.texture;
			default:
				return Block.SAND.texture;
		}
	}

	public int getId() {
		return id;
	}
}
