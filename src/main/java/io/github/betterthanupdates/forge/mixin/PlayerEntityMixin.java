package io.github.betterthanupdates.forge.mixin;

import forge.ArmorProperties;
import forge.ForgeHooks;
import forge.ISpecialArmor;
import io.github.betterthanupdates.forge.entity.player.ForgePlayerEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ForgePlayerEntity {

	@Shadow public PlayerInventory inventory;

	@Shadow private int field_518;

	@Shadow protected abstract void method_517(int i);

	@Shadow protected boolean sleeping;

	@Shadow private int sleepTimer;

	@Shadow public Vec3i bedPosition;

	private PlayerEntityMixin(World arg) {
		super(arg);
	}

	/**
	 * Gets the strength of the player against the block, taking
	 * into account the meta value of the block state.
	 *
	 * @param block the block id to check strength against
	 * @param meta  the meta value of the block state
	 * @return the strength of the player against the given block
	 */
	@Override
	public float getCurrentPlayerStrVsBlock(Block block, int meta) {
		float f = 1.0F;
		ItemStack ist = this.inventory.getHeldItem();
		if (ist != null) {
			f = ist.getItem().getStrVsBlock(ist, block, meta);
		}

		if (this.isInFluid(Material.WATER)) {
			f /= 5.0F;
		}

		if (!this.onGround) {
			f /= 5.0F;
		}

		return f;
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	protected void applyDamage(int i) {
		boolean doRegularComputation = true;
		int initialDamage = i;

		for(ItemStack stack : this.inventory.armor) {
			if (stack != null && stack.getItem() instanceof ISpecialArmor) {
				ISpecialArmor armor = (ISpecialArmor)stack.getItem();
				ArmorProperties props = armor.getProperties((PlayerEntity)(Object) this, initialDamage, i);
				i -= props.damageRemove;
				doRegularComputation = doRegularComputation && props.allowRegularComputation;
			}
		}

		if (!doRegularComputation) {
			super.applyDamage(i);
		} else {
			int j = 25 - this.inventory.getArmorValue();
			int k = i * j + this.field_518;
			this.inventory.damageArmor(i);
			i = k / 25;
			this.field_518 = k % 25;
			super.applyDamage(i);
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void breakHeldItem() {
		ItemStack orig = this.inventory.getHeldItem();
		this.inventory.setInventoryItem(this.inventory.selectedHotBarSlot, null);
		ForgeHooks.onDestroyCurrentItem((PlayerEntity)(Object) this, orig);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public SleepStatus trySleep(int i, int j, int k) {
		SleepStatus customSleep = ForgeHooks.sleepInBedAt((PlayerEntity)(Object) this, i, j, k);
		if (customSleep != null) {
			return customSleep;
		} else {
			if (!this.world.isClient) {
				if (this.isSleeping() || !this.isAlive()) {
					return SleepStatus.YOU_SLEEPING_OR_DEAD;
				}

				if (this.world.dimension.blocksCompassAndClock) {
					return SleepStatus.CANT_SLEEP_HERE;
				}

				if (this.world.isDaylight()) {
					return SleepStatus.DAY_TIME;
				}

				if (Math.abs(this.x - (double)i) > 3.0 || Math.abs(this.y - (double)j) > 2.0 || Math.abs(this.z - (double)k) > 3.0) {
					return SleepStatus.field_2663;
				}
			}

			this.setSize(0.2F, 0.2F);
			this.standingEyeHeight = 0.2F;
			if (this.world.isBlockLoaded(i, j, k)) {
				int l = this.world.getBlockMeta(i, j, k);
				int i1 = BedBlock.orientationOnly(l);
				float f = 0.5F;
				float f1 = 0.5F;
				switch(i1) {
					case 0:
						f1 = 0.9F;
						break;
					case 1:
						f = 0.1F;
						break;
					case 2:
						f1 = 0.1F;
						break;
					case 3:
						f = 0.9F;
				}

				this.method_517(i1);
				this.setPosition((double)((float)i + f), (double)((float)j + 0.9375F), (double)((float)k + f1));
			} else {
				this.setPosition((double)((float)i + 0.5F), (double)((float)j + 0.9375F), (double)((float)k + 0.5F));
			}

			this.sleeping = true;
			this.sleepTimer = 0;
			this.bedPosition = new Vec3i(i, j, k);
			this.xVelocity = this.zVelocity = this.yVelocity = 0.0;
			if (!this.world.isClient) {
				this.world.onPlayerDisconnect();
			}

			return SleepStatus.field_2660;
		}
	}
}
