package shockahpi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import io.github.betterthanupdates.Legacy;

/**
 * aka "ACPage" after remapping.
 *
 * @author ShockAh
 */
@SuppressWarnings("unused")
@Legacy
public class AchievementPage {
	private static int nextID = 1;
	final int id;
	public final String title;
	ArrayList<Integer> list = new ArrayList<>();
	ArrayList<Achievement> achievements = new ArrayList<>();

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
			this.achievements.add(achievement);
		}
	}

	public int bgGetSprite(Random random, int x, int y) {
		int sprite = Block.SAND.textureId;
		int rnd = random.nextInt(1 + y) + y / 2;
		if (rnd <= 37 && y != 35) {
			if (rnd == 22) {
				sprite = random.nextInt(2) == 0 ? Block.DIAMOND_ORE.textureId : Block.REDSTONE_ORE.textureId;
			} else if (rnd == 10) {
				sprite = Block.IRON_ORE.textureId;
			} else if (rnd == 8) {
				sprite = Block.COAL_ORE.textureId;
			} else if (rnd > 4) {
				sprite = Block.STONE.textureId;
			} else if (rnd > 0) {
				sprite = Block.DIRT.textureId;
			}
		} else {
			sprite = Block.BEDROCK.textureId;
		}

		return sprite;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getList() {
		return Collections.unmodifiableList(this.list);
	}

	@ApiStatus.Internal
	public List<Achievement> getAchievements() {
		return Collections.unmodifiableList(this.achievements);
	}
}
