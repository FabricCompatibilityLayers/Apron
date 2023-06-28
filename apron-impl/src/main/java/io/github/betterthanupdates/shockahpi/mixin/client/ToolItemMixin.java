package io.github.betterthanupdates.shockahpi.mixin.client;

import fr.catcore.modremapperapi.api.mixin.ChangeSuperClass;
import fr.catcore.modremapperapi.api.mixin.Public;
import fr.catcore.modremapperapi.api.mixin.ReplaceConstructor;
import fr.catcore.modremapperapi.api.mixin.SuperConstructor;
import fr.catcore.modremapperapi.api.mixin.SuperMethod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import shockahpi.BlockHarvestPower;
import shockahpi.Tool;
import shockahpi.ToolBase;

import java.util.Iterator;

@Mixin(ToolItem.class)
@ChangeSuperClass(Tool.class)
public abstract class ToolItemMixin extends Item {
	@Shadow
	public float field_2713;

	@Shadow
	public int field_2714;

	@Shadow
	protected ToolMaterial toolMaterial;

	@Shadow
	private Block[] effectiveBlocksBase;

	public ToolItemMixin(int i) {
		super(i);
	}

	@SuperConstructor
	public abstract void Tool$ctr(boolean usingSAPI, ToolBase toolBase, int itemID, int uses, float baseDamage, float basePower, float toolSpeed);
	@ReplaceConstructor
	public void ctr(int itemID, int damage, ToolMaterial material, Block[] blocks) {
		this.Tool$ctr(false, null, itemID, material.getDurability(), (float)(damage + material.getAttackDamage()), getToolPower(material), material.getMiningSpeed());
		this.field_2713 = Float.NaN;
		this.field_2714 = Integer.MIN_VALUE;

		this.toolMaterial = material;
		((Tool)(Object)this).toolBase = this.getToolBase();
		if (blocks != null) {
			Block[] var5 = blocks;
			int var6 = blocks.length;

			for(int var7 = 0; var7 < var6; ++var7) {
				Block block = var5[var7];
				if (block != null) {
					((Tool)(Object)this).mineBlocks.add(new BlockHarvestPower(block.id, 0.0F));
				}
			}
		}
	}

	public ToolBase getToolBase() {
		if (((Object)this) instanceof PickaxeItem) {
			return ToolBase.Pickaxe;
		} else if (((Object)this) instanceof AxeItem) {
			return ToolBase.Axe;
		} else {
			return ((Object)this) instanceof ShovelItem ? ToolBase.Shovel : null;
		}
	}

	@Public
	private static float getToolPower(ToolMaterial material) {
		if (material == ToolMaterial.DIAMOND) {
			return 80.0F;
		} else if (material == ToolMaterial.IRON) {
			return 60.0F;
		} else {
			return material != ToolMaterial.STONE ? 20.0F : 40.0F;
		}
	}

	@SuperMethod("canHarvest")
	public abstract boolean Tool$canHarvest(Block block);

	public boolean canHarvest(Block block) {
		if (!((Tool)(Object)this).usingSAPI && !this.isBlockOnList(block.id)) {
			if (((Object)this) instanceof PickaxeItem) {
				if (((Tool)(Object)this).id <= 369) {
					return false;
				}

				if (block.material == Material.STONE || block.material == Material.ICE) {
					return true;
				}

				if (block.material == Material.METAL && ((Tool)(Object)this).basePower >= 40.0F) {
					return true;
				}
			} else if (((Object)this) instanceof AxeItem) {
				if (((Tool)(Object)this).id <= 369) {
					return false;
				}

				if (block.material == Material.WOOD || block.material == Material.LEAVES || block.material == Material.PLANT || block.material == Material.CACTUS || block.material == Material.PUMPKIN) {
					return true;
				}
			} else if (((Object)this) instanceof ShovelItem) {
				if (((Tool)(Object)this).id <= 369) {
					return false;
				}

				if (block.material == Material.ORGANIC || block.material == Material.DIRT || block.material == Material.SAND || block.material == Material.SNOW || block.material == Material.SNOW_BLOCK || block.material == Material.CLAY) {
					return true;
				}
			}
		}

		return this.Tool$canHarvest(block);
	}

	private boolean isBlockOnList(int blockID) {
		Iterator iterator1 = ((Tool)(Object)this).mineBlocks.iterator();

		BlockHarvestPower power;
		while(iterator1.hasNext()) {
			power = (BlockHarvestPower)iterator1.next();
			if (power.blockID == blockID) {
				return true;
			}
		}

		if (((Tool)(Object)this).toolBase != null) {
			iterator1 = ((Tool)(Object)this).toolBase.mineBlocks.iterator();

			while(iterator1.hasNext()) {
				power = (BlockHarvestPower)iterator1.next();
				if (power.blockID == blockID) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @author ShockAh
	 * @reason check tool speed
	 */
	@Overwrite
	public float getStrengthOnBlock(ItemStack stack, Block block) {
		if (this.effectiveBlocksBase != null) {
			for (Block value : this.effectiveBlocksBase) {
				if (value == block) {
					return this.getToolSpeed();
				}
			}

			return 1.0F;
		} else {
			return super.getStrengthOnBlock(stack, block);
		}
	}

	/**
	 * @author ShockAh
	 * @reason check tool damage
	 */
	@Overwrite
	public int getAttackDamage(Entity entity) {
		int i = super.getAttackDamage(entity);
		return this.field_2714 != Integer.MIN_VALUE && (double)i == Math.floor((double)((Tool)(Object)this).baseDamage) ? this.field_2714 : i;
	}

	@SuperMethod("getToolSpeed")
	public abstract float Tool$getToolSpeed();

	protected float getToolSpeed() {
		return Float.isNaN(this.field_2713) ? this.Tool$getToolSpeed() : this.field_2713;
	}

	/**
	 * @author ShockAh
	 * @reason original implementation removed
	 */
	@Overwrite
	public boolean postHit(ItemStack arg, LivingEntity arg2, LivingEntity arg3) {
		return super.postHit(arg, arg2, arg3);
	}

	/**
	 * @author ShockAh
	 * @reason original implementation removed
	 */
	@Overwrite
	public boolean postMine(ItemStack arg, int i, int j, int k, int l, LivingEntity arg2) {
		return super.postMine(arg, i, j, k, l, arg2);
	}

	/**
	 * @author ShockAh
	 * @reason original implementation removed
	 */
	@Environment(EnvType.CLIENT)
	@Overwrite
	public boolean isRendered3d() {
		return super.isRendered3d();
	}
}
