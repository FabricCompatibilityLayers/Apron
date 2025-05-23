package shockahpi;

import java.util.ArrayList;
import java.util.Iterator;

import forge.ForgeHooks;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.forge.item.ForgeItem;

@Legacy
public class Tool extends Item implements ForgeItem {
	public final boolean usingSAPI;
	public ToolBase toolBase;
	public final float baseDamage;
	public final float basePower;
	public final float defaultSpeed;
	public final float toolSpeed;
	public ArrayList<BlockHarvestPower> mineBlocks = new ArrayList<>();
	public ArrayList<Material> mineMaterials = new ArrayList<>();

	public Tool(boolean usingSAPI, ToolBase toolBase, int itemID, int uses, float baseDamage, float basePower, float toolSpeed) {
		this(usingSAPI, toolBase, itemID, uses, baseDamage, basePower, toolSpeed, 1.0F);
	}

	public Tool(boolean usingSAPI, ToolBase toolBase, int itemID, int uses, float baseDamage, float basePower, float toolSpeed, float defaultSpeed) {
		super(itemID);
		this.setMaxDamage(uses);
		this.maxCount = 1;
		this.usingSAPI = usingSAPI;
		this.toolBase = toolBase;
		this.baseDamage = baseDamage;
		this.basePower = basePower;
		this.toolSpeed = toolSpeed;
		this.defaultSpeed = defaultSpeed;
	}

	@Override
	public boolean isHandheld() {
		return true;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity living, LivingEntity living2) {
		stack.damage(2, living2);
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, int blockID, int x, int y, int z, LivingEntity living) {
		stack.damage(1, living);
		return true;
	}

	@Override
	public int getAttackDamage(Entity entity) {
		return (int)Math.floor((double)this.baseDamage);
	}

	public float getPower() {
		return this.basePower;
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, Block block) {
		return this.canHarvest(block) ? this.getToolSpeed() : this.defaultSpeed;
	}

	public boolean canHarvest(Block block) {
		if (this.toolBase == null || !this.toolBase.canHarvest(block, this.getPower())) {
			Iterator iterator1 = this.mineMaterials.iterator();

			while (iterator1.hasNext()) {
				Material material = (Material) iterator1.next();
				if (material == block.material) {
					return true;
				}
			}

			iterator1 = this.mineBlocks.iterator();

			BlockHarvestPower power;
			do {
				if (!iterator1.hasNext()) {
					return false;
				}

				power = (BlockHarvestPower) iterator1.next();
			} while (block.id != power.blockID && !(this.getPower() >= power.percentage));

		}
		return true;
	}

	@Override
	public float getStrVsBlock(ItemStack itemstack, Block block, int md) {
		return ForgeHooks.isToolEffective(itemstack, block, md) ? this.getToolSpeed() : this.getMiningSpeedMultiplier(itemstack, block);
	}

	protected float getToolSpeed() {
		return this.toolSpeed;
	}

	static {
		SAPI.showText();
	}
}
