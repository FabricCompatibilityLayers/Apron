package shockahpi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import io.github.betterthanupdates.Legacy;

@Legacy
public class ToolBase {
	@Legacy
	public static final ToolBase Pickaxe = new ToolBase();
	@Legacy
	public static final ToolBase Shovel = new ToolBase();
	@Legacy
	public static final ToolBase Axe = new ToolBase();
	@Legacy
	public ArrayList<BlockHarvestPower> mineBlocks = new ArrayList<>();
	@Legacy
	public ArrayList<Material> mineMaterials = new ArrayList<>();

	static {
		SAPI.showText();

		for (Integer blockID : Arrays.asList(1, 4, 16, 21, 22, 23, 24, 43, 44, 45, 48, 52, 61, 62, 67, 77, 79, 87, 89, 93, 94)) {
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID, 20.0F));
		}

		for (Integer blockID : Arrays.asList(15, 42, 71)) {
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID, 40.0F));
		}

		for (Integer blockID : Arrays.asList(14, 41, 56, 57, 73, 74)) {
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID, 60.0F));
		}

		for (Integer blockID : Collections.singletonList(49)) {
			Pickaxe.mineBlocks.add(new BlockHarvestPower(blockID, 80.0F));
		}

		for (Integer blockID : Arrays.asList(2, 3, 12, 13, 78, 80, 82)) {
			Shovel.mineBlocks.add(new BlockHarvestPower(blockID, 20.0F));
		}

		for (Integer blockID : Arrays.asList(5, 17, 18, 25, 47, 53, 54, 58, 63, 64, 65, 66, 68, 69, 81, 84, 85)) {
			Axe.mineBlocks.add(new BlockHarvestPower(blockID, 20.0F));
		}
	}

	@Legacy
	public ToolBase() {
	}

	@Legacy
	public boolean canHarvest(Block block, float currentPower) {
		for (Material material : this.mineMaterials) {
			if (material == block.material) {
				return true;
			}
		}

		for (BlockHarvestPower power : this.mineBlocks) {
			if (block.id == power.blockID || currentPower >= power.percentage) {
				return true;
			}
		}

		return false;
	}
}
