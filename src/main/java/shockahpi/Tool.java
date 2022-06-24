package shockahpi;

import java.util.ArrayList;

import forge.ForgeHooks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@Legacy
public class Tool extends Item {
	@Legacy
	public final boolean usingSAPI;
	@Legacy
	public ToolBase toolBase;
	@Legacy
	public final float baseDamage;
	@Legacy
	public final float basePower;
	@Legacy
	public final float defaultSpeed;
	@Legacy
	public final float toolSpeed;
	@Legacy
	public ArrayList<BlockHarvestPower> mineBlocks = new ArrayList<>();
	@Legacy
	public ArrayList<Material> mineMaterials = new ArrayList<>();

	@Legacy
	public Tool(boolean usingSAPI, ToolBase toolBase, int itemID, int uses, float baseDamage, float basePower, float toolSpeed) {
		this(usingSAPI, toolBase, itemID, uses, baseDamage, basePower, toolSpeed, 1.0F);
	}

	@Legacy
	public Tool(boolean usingSAPI, ToolBase toolBase, int itemID, int uses, float baseDamage, float basePower, float toolSpeed, float defaultSpeed) {
		super(itemID);
		this.setDurability(uses);
		this.maxStackSize = 1;
		this.usingSAPI = usingSAPI;
		this.toolBase = toolBase;
		this.baseDamage = baseDamage;
		this.basePower = basePower;
		this.toolSpeed = toolSpeed;
		this.defaultSpeed = defaultSpeed;
	}

	@Legacy
	@Override
	public boolean isRendered3d() {
		return true;
	}

	@Legacy
	@Override
	public boolean postHit(ItemStack stack, LivingEntity living, LivingEntity living2) {
		stack.applyDamage(2, living2);
		return true;
	}

	@Legacy
	@Override
	public boolean postMine(ItemStack stack, int blockID, int x, int y, int z, LivingEntity living) {
		stack.applyDamage(1, living);
		return true;
	}

	@Legacy
	@Override
	public int getAttackDamage(Entity entity) {
		return (int) Math.floor((double) this.baseDamage);
	}

	@Legacy
	public float getPower() {
		return this.basePower;
	}

	@Legacy
	public float getStrengthOnBlock(ItemStack stack, Block block) {
		return this.canHarvest(block) ? this.getToolSpeed() : this.defaultSpeed;
	}

	@Legacy
	public boolean canHarvest(Block block) {
		if (this.toolBase != null && this.toolBase.canHarvest(block, this.getPower())) {
			return true;
		} else {
			for (Material material : this.mineMaterials) {
				if (material == block.material) {
					return true;
				}
			}

			for (BlockHarvestPower power : this.mineBlocks) {
				if (block.id == power.blockID || this.getPower() >= power.percentage) {
					return true;
				}
			}

			return false;
		}
	}

	@Legacy
	public float getStrVsBlock(ItemStack itemstack, Block block, int md) {
		return ForgeHooks.isToolEffective(itemstack, block, md) ? this.getToolSpeed() : this.getStrengthOnBlock(itemstack, block);
	}

	@Legacy
	protected float getToolSpeed() {
		return this.toolSpeed;
	}

	static {
		SAPI.showText();
	}
}
