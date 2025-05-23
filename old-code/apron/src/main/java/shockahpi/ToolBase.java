package shockahpi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import io.github.betterthanupdates.Legacy;

@Legacy
public class ToolBase {
	public static final ToolBase PICKAXE;
	public static final ToolBase SHOVEL;
	public static final ToolBase AXE;
	public ArrayList<BlockHarvestPower> mineBlocks = new ArrayList();
	public ArrayList<Material> mineMaterials = new ArrayList();

	static {
		SAPI.showText();
		PICKAXE = new ToolBase();
		SHOVEL = new ToolBase();
		AXE = new ToolBase();
		List<Integer> list = Arrays.asList(1, 4, 16, 21, 22, 23, 24, 43, 44, 45, 48, 52, 61, 62, 67, 77, 79, 87, 89, 93, 94);
		Iterator var2 = list.iterator();

		Integer blockID;
		while(var2.hasNext()) {
			blockID = (Integer)var2.next();
			PICKAXE.mineBlocks.add(new BlockHarvestPower(blockID, 20.0F));
		}

		list = Arrays.asList(15, 42, 71);
		var2 = list.iterator();

		while(var2.hasNext()) {
			blockID = (Integer)var2.next();
			PICKAXE.mineBlocks.add(new BlockHarvestPower(blockID, 40.0F));
		}

		list = Arrays.asList(14, 41, 56, 57, 73, 74);
		var2 = list.iterator();

		while(var2.hasNext()) {
			blockID = (Integer)var2.next();
			PICKAXE.mineBlocks.add(new BlockHarvestPower(blockID, 60.0F));
		}

		list = Arrays.asList(49);
		var2 = list.iterator();

		while(var2.hasNext()) {
			blockID = (Integer)var2.next();
			PICKAXE.mineBlocks.add(new BlockHarvestPower(blockID, 80.0F));
		}

		list = Arrays.asList(2, 3, 12, 13, 78, 80, 82);
		var2 = list.iterator();

		while(var2.hasNext()) {
			blockID = (Integer)var2.next();
			SHOVEL.mineBlocks.add(new BlockHarvestPower(blockID, 20.0F));
		}

		list = Arrays.asList(5, 17, 18, 25, 47, 53, 54, 58, 63, 64, 65, 66, 68, 69, 81, 84, 85);
		var2 = list.iterator();

		while(var2.hasNext()) {
			blockID = (Integer)var2.next();
			AXE.mineBlocks.add(new BlockHarvestPower(blockID, 20.0F));
		}

	}

	public ToolBase() {
	}

	public boolean canHarvest(Block block, float currentPower) {
		Iterator var4 = this.mineMaterials.iterator();

		while(var4.hasNext()) {
			Material material = (Material)var4.next();
			if (material == block.material) {
				return true;
			}
		}

		var4 = this.mineBlocks.iterator();

		BlockHarvestPower power;
		do {
			if (!var4.hasNext()) {
				return false;
			}

			power = (BlockHarvestPower)var4.next();
		} while(block.id != power.blockID && !(currentPower >= power.percentage));

		return true;
	}
}
