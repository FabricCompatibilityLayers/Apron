package io.github.betterthanupdates.shockahpi.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shockahpi.BlockHarvestPower;
import shockahpi.ToolBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

import io.github.betterthanupdates.shockahpi.SAPITool;
import io.github.betterthanupdates.shockahpi.ShockAhPIToolItem;

@Mixin(ToolItem.class)
public class ToolItemMixin extends Item implements SAPITool {
	@Shadow
	public float field_2713;

	@Shadow
	public int field_2714;

	public ToolItemMixin(int i) {
		super(i);
	}

	// SAPI Fields
	@Unique
	public boolean usingSAPI;
	@Unique
	public ToolBase toolBase;
	@Unique
	public float baseDamage;
	@Unique
	public float basePower;
	@Unique
	public float defaultSpeed;
	@Unique
	public float toolSpeed;
	@Unique
	public ArrayList<BlockHarvestPower> mineBlocks = new ArrayList<>();
	@Unique
	public ArrayList<Material> mineMaterials = new ArrayList<>();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void sapi$ctr(int i, int j, ToolMaterial material, Block[] blocks, CallbackInfo ci) {
		this.toolSpeed = this.field_2713;
		this.baseDamage = this.field_2714;
		this.defaultSpeed = 1.0F;
		this.basePower = ShockAhPIToolItem.getToolPower(material);
		this.toolBase = this.getToolBase();
		this.usingSAPI = false;

		for (Block block : blocks) {
			if (block == null) continue;
			this.mineBlocks.add(new BlockHarvestPower(block.id, 0.0F));
		}
	}

	public ToolBase getToolBase() {
		if (((ToolItem) (Object) this) instanceof PickaxeItem) {
			return ToolBase.Pickaxe;
		} else if (((ToolItem) (Object) this) instanceof AxeItem) {
			return ToolBase.Axe;
		} else {
			return ((ToolItem) (Object) this) instanceof ShovelItem ? ToolBase.Shovel : null;
		}
	}

	/**
	 * @author SAPI
	 * @reason yes
	 */
	@Overwrite
	public int getAttackDamage(Entity entity) {
		return (int) Math.floor((double) this.baseDamage);
	}

	@Override
	public float getPower() {
		return this.basePower;
	}

	/**
	 * @author SAPI
	 * @reason yes
	 */
	@Overwrite
	public float getStrengthOnBlock(ItemStack stack, Block block) {
		return this.canHarvest(block) ? this.toolSpeed : this.defaultSpeed;
	}

	@Override
	public boolean canHarvest(Block block) {
		if (!this.usingSAPI && !this.isBlockOnList(block.id)) {
			if (((ToolItem) (Object) this) instanceof PickaxeItem) {
				if (this.id <= 369) {
					return false;
				}

				if (block.material == Material.STONE || block.material == Material.ICE) {
					return true;
				}

				if (block.material == Material.METAL && this.basePower >= 40.0F) {
					return true;
				}
			} else if (((ToolItem) (Object) this) instanceof AxeItem) {
				if (this.id <= 369) {
					return false;
				}

				if (block.material == Material.WOOD
						|| block.material == Material.LEAVES
						|| block.material == Material.PLANT
						|| block.material == Material.CACTUS
						|| block.material == Material.PUMPKIN) {
					return true;
				}
			} else if (((ToolItem) (Object) this) instanceof ShovelItem) {
				if (this.id <= 369) {
					return false;
				}

				if (block.material == Material.ORGANIC
						|| block.material == Material.DIRT
						|| block.material == Material.SAND
						|| block.material == Material.SNOW
						|| block.material == Material.SNOW_BLOCK
						|| block.material == Material.CLAY) {
					return true;
				}
			}
		}

		return this.canHarvest$Tool(block);
	}

	private boolean canHarvest$Tool(Block block) {
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

	private boolean isBlockOnList(int blockID) {
		for (BlockHarvestPower power : this.mineBlocks) {
			if (power.blockID == blockID) {
				return true;
			}
		}

		for (BlockHarvestPower power : this.toolBase.mineBlocks) {
			if (power.blockID == blockID) {
				return true;
			}
		}

		return false;
	}
}
