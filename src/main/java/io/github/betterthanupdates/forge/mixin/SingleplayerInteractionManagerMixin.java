package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Mixin(SingleplayerInteractionManager.class)
public class SingleplayerInteractionManagerMixin extends ClientInteractionManager {
	@Shadow
	private float damage;

	@Shadow
	private int hitDelay;

	@Shadow
	private int blockX;

	@Shadow
	private int blockY;

	@Shadow
	private int blockZ;

	@Shadow
	private float field_2186;

	@Shadow
	private float oldDamage;

	public SingleplayerInteractionManagerMixin(Minecraft minecraft) {
		super(minecraft);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean method_1716(int i, int j, int k, int l) {
		int i1 = this.client.world.getBlockId(i, j, k);
		int j1 = this.client.world.getBlockMeta(i, j, k);
		boolean flag = super.method_1716(i, j, k, l);
		ItemStack itemstack = this.client.player.getHeldItem();
		boolean flag1 = ((ForgeBlock) Block.BY_ID[i1]).canHarvestBlock(this.client.player, j1);
		if (itemstack != null) {
			itemstack.postMine(i1, i, j, k, this.client.player);
			if (itemstack.count == 0) {
				itemstack.unusedEmptyMethod1(this.client.player);
				this.client.player.breakHeldItem();
			}
		}

		if (flag && flag1) {
			Block.BY_ID[i1].afterBreak(this.client.world, this.client.player, i, j, k, j1);
		}

		return flag;
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_1707(int i, int j, int k, int l) {
		this.client.world.method_172(this.client.player, i, j, k, l);
		int i1 = this.client.world.getBlockId(i, j, k);
		if (i1 > 0 && this.damage == 0.0F) {
			Block.BY_ID[i1].activate(this.client.world, i, j, k, this.client.player);
		}

		if (i1 > 0 && ((ForgeBlock) Block.BY_ID[i1]).blockStrength(this.client.world, this.client.player, i, j, k) >= 1.0F) {
			this.method_1716(i, j, k, l);
		}

	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_1721(int i, int j, int k, int l) {
		if (this.hitDelay > 0) {
			--this.hitDelay;
		} else {
			if (i == this.blockX && j == this.blockY && k == this.blockZ) {
				int i1 = this.client.world.getBlockId(i, j, k);
				if (i1 == 0) {
					return;
				}

				Block block = Block.BY_ID[i1];
				this.damage += ((ForgeBlock) block).blockStrength(this.client.world, this.client.player, i, j, k);
				if (this.field_2186 % 4.0F == 0.0F && block != null) {
					this.client
							.soundHelper
							.playSound(
									block.sounds.getWalkSound(),
									(float) i + 0.5F,
									(float) j + 0.5F,
									(float) k + 0.5F,
									(block.sounds.getVolume() + 1.0F) / 8.0F,
									block.sounds.getPitch() * 0.5F
							);
				}

				++this.field_2186;
				if (this.damage >= 1.0F) {
					this.method_1716(i, j, k, l);
					this.damage = 0.0F;
					this.oldDamage = 0.0F;
					this.field_2186 = 0.0F;
					this.hitDelay = 5;
				}
			} else {
				this.damage = 0.0F;
				this.oldDamage = 0.0F;
				this.field_2186 = 0.0F;
				this.blockX = i;
				this.blockY = j;
				this.blockZ = k;
			}

		}
	}
}
