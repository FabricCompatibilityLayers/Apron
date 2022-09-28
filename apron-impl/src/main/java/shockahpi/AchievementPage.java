package shockahpi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.Block;
import net.minecraft.stat.achievement.Achievement;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.stapi.StationAPIHelper;

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

		if (this.id == 0 && FabricLoader.getInstance().isModLoaded("stationapi")) {
			StationAPIHelper.registerMinecraftAchievement(achievements);
		}
	}

	public int bgGetSprite(Random random, int x, int y) {
		int sprite = Block.SAND.texture;
		int rnd = random.nextInt(1 + y) + y / 2;

		if (rnd > 37 || y == 35) {
			sprite = Block.BEDROCK.texture;
		} else if (rnd == 22) {
			sprite = random.nextInt(2) == 0 ? Block.DIAMOND_ORE.texture : Block.REDSTONE_ORE.texture;
		} else if (rnd == 10) {
			sprite = Block.IRON_ORE.texture;
		} else if (rnd == 8) {
			sprite = Block.COAL_ORE.texture;
		} else if (rnd > 4) {
			sprite = Block.STONE.texture;
		} else if (rnd > 0) {
			sprite = Block.DIRT.texture;
		}

		return sprite;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getList() {
		return Collections.unmodifiableList(this.list);
	}
}
