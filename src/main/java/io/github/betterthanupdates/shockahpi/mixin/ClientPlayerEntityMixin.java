package io.github.betterthanupdates.shockahpi.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shockahpi.DimensionBase;
import shockahpi.PlayerBase;
import shockahpi.SAPI;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.block.DispenserBlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.entity.block.SignBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientPlayPacketHandler;
import net.minecraft.packet.play.CloseContainerC2SPacket;
import net.minecraft.packet.play.PlayerDiggingC2SPacket;
import net.minecraft.packet.play.RespawnC2SPacket;
import net.minecraft.stat.Stat;
import net.minecraft.stat.achievement.Achievements;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.math.AxixAlignedBoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import io.github.betterthanupdates.shockahpi.client.entity.player.ShockAhPIClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ShockAhPIClientPlayerEntity {
	@Shadow
	public ClientPlayPacketHandler networkHandler;

	public ClientPlayerEntityMixin(Minecraft minecraft, World arg, Session arg2, int i) {
		super(minecraft, arg, arg2, i);
	}

	@Shadow
	public abstract void method_1922();

	@Shadow
	protected abstract void method_1923();

	// SAPI Fields
	@Unique
	public ArrayList<PlayerBase> playerBases;
	@Unique
	private final SAPI sapi = new SAPI();
	@Unique
	public int portal;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void shockahpi$ctr(Minecraft client, World world, Session session, ClientPlayPacketHandler handler, CallbackInfo ci) {
		this.portal = -1;

		this.playerBases = this.sapi.PAPIplayerInit(((ClientPlayerEntity) (Object) this));
	}

	@Override
	public List<PlayerBase> getPlayerBases() {
		return playerBases;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void method_1924(Stat statbase, int i) {
		if (statbase != null) {
			if (!statbase.givenByClient) {
				super.increaseStat(statbase, i);
			}

		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public boolean damage(Entity entity, int i) {
		this.sapi.PAPIattackEntityFrom(this, entity, i);
		return false;
	}

	@Override
	public void onKilledBy(Entity entity) {
		if (!this.sapi.PAPIonDeath(this, entity)) {
			super.onKilledBy(entity);
		}
	}

	@Override
	public void tickHandSwing() {
		if (!this.sapi.PAPIupdatePlayerActionState(this)) {
			super.tickHandSwing();
		}
	}

	@Override
	public void movementInputToVelocity(float f, float f1, float f2) {
		if (!this.sapi.PAPImoveFlying(this, f, f1, f2)) {
			super.movementInputToVelocity(f, f1, f2);
		}
	}

	@Override
	protected boolean canClimb() {
		return this.sapi.PAPIcanTriggerWalking(this, true);
	}

	@Override
	public void method_136(int i, boolean flag) {
		if (!this.sapi.PAPIhandleKeyPress(this, i, flag)) {
			super.method_136(i, flag);
		}
	}

	@Override
	public void writeAdditional(CompoundTag compoundTag) {
		if (!this.sapi.PAPIwriteEntityToNBT(this, compoundTag)) {
			super.writeAdditional(compoundTag);
		}
	}

	@Override
	public void readAdditional(CompoundTag compoundTag) {
		if (!this.sapi.PAPIreadEntityFromNBT(this, compoundTag)) {
			super.readAdditional(compoundTag);
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void closeContainer() {
		if (!this.sapi.PAPIonExitGUI(this)) {
			this.networkHandler.sendPacket(new CloseContainerC2SPacket(this.container.currentContainerId));
			this.inventory.setCursorItem(null);
			super.closeContainer();
		}
	}

	@Override
	public void openSignScreen(SignBlockEntity signBlockEntity) {
		if (!this.sapi.PAPIdisplayGUIEditSign(this, signBlockEntity)) {
			super.openSignScreen(signBlockEntity);
		}
	}

	@Override
	public void openChestScreen(Inventory inventory) {
		if (!this.sapi.PAPIdisplayGUIChest(this, inventory)) {
			super.openChestScreen(inventory);
		}
	}

	@Override
	public void openCraftingScreen(int i, int j, int k) {
		if (!this.sapi.PAPIdisplayWorkbenchGUI(this, i, j, k)) {
			super.openCraftingScreen(i, j, k);
		}
	}

	@Override
	public void openFurnaceScreen(FurnaceBlockEntity furnaceBlockEntity) {
		if (!this.sapi.PAPIdisplayGUIFurnace(this, furnaceBlockEntity)) {
			super.openFurnaceScreen(furnaceBlockEntity);
		}
	}

	@Override
	public void openDispenserScreen(DispenserBlockEntity dispenserBlockEntity) {
		if (!this.sapi.PAPIdisplayGUIDispenser(this, dispenserBlockEntity)) {
			super.openDispenserScreen(dispenserBlockEntity);
		}
	}

	@Override
	public int getArmorValue() {
		return this.sapi.PAPIgetPlayerArmorValue(this, this.inventory.getArmorValue());
	}

	@Override
	public void remove() {
		if (!this.sapi.PAPIsetEntityDead(this)) {
			super.remove();
		}
	}

	@Override
	public double squaredDistanceTo(double d, double d1, double d2) {
		return this.sapi.PAPIgetDistanceSq(this, d, d1, d2, super.squaredDistanceTo(d, d1, d2));
	}

	@Override
	public boolean method_1334() {
		return this.sapi.PAPIisInWater(this, this.field_1612);
	}

	@Override
	public boolean method_1373() {
		return this.sapi.PAPIisSneaking(this, this.playerKeypressManager.sneak && !this.lyingOnBed);
	}

	@Override
	public float getStrengh(Block block) {
		return this.sapi.PAPIgetCurrentPlayerStrVsBlock(this, block, super.getStrengh(block));
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void addHealth(int i) {
		this.sapi.PAPIheal(this, i);
	}

	@Override
	protected boolean method_1372(double d, double d1, double d2) {
		return !this.sapi.PAPIpushOutOfBlocks(this, d, d1, d2) && super.method_1372(d, d1, d2);
	}

	@Override
	public SleepStatus trySleep(int i, int j, int k) {
		SleepStatus sleepStatus = this.sapi.PAPIsleepInBedAt(this, i, j, k);
		return sleepStatus == null ? super.trySleep(i, j, k) : sleepStatus;
	}

	@Override
	public float getBrightnessAtEyes(float f) {
		return this.sapi.PAPIgetEntityBrightness(this, f, super.getBrightnessAtEyes(f));
	}

	@Override
	public void move(double d, double d1, double d2) {
		if (!this.sapi.PAPImoveEntity(this, d, d1, d2)) {
			super.move(d, d1, d2);
		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void tick() {
		if (!this.sapi.PAPIonUpdate(this)) {
			if (this.world.isBlockLoaded(MathHelper.floor(this.x), 64, MathHelper.floor(this.z))) {
				super.tick();
				this.method_1922();
			}
		} else {
			this.sapi.PAPIafterUpdate(this);
		}
	}

	@Override
	public void travel(float f, float f1) {
		if (!this.sapi.PAPImoveEntityWithHeading(this, f, f1)) {
			super.travel(f, f1);
		}
	}

	@Override
	public boolean method_932() {
		return this.sapi.PAPIisOnLadder(this, super.method_932());
	}

	@Override
	public boolean isInFluid(Material material) {
		return this.sapi.PAPIisInsideOfMaterial(this, material, super.isInFluid(material));
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void dropSelectedItem() {
		if (!this.sapi.PAPIdropCurrentItem(this)) {
			this.networkHandler.sendPacket(new PlayerDiggingC2SPacket(4, 0, 0, 0, 0));
		}
	}

	@Override
	public void dropItem(ItemStack itemstack) {
		if (!this.sapi.PAPIdropPlayerItem(this, itemstack)) {
			super.dropItem(itemstack);
		}
	}

	@Override
	public void updateDespawnCounter() {
		if (!this.sapi.PAPIonLivingUpdate(this)) {
			if (!this.client.statFileWriter.isAchievementUnlocked(Achievements.OPEN_INVENTORY)) {
				this.client.achievement.setAchievement(Achievements.OPEN_INVENTORY);
			}

			this.field_505 = this.field_504;
			if (this.portal != 0) {
				DimensionBase dimensionbase = DimensionBase.getDimByNumber(this.portal);
				if (this.field_512) {
					if (!this.world.isClient && this.vehicle != null) {
						this.startRiding(null);
					}

					if (this.client.currentScreen != null) {
						this.client.openScreen(null);
					}

					if (this.field_504 == 0.0F) {
						this.client.soundHelper.playSound(dimensionbase.soundTrigger, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
					}

					this.field_504 += 0.0125F;
					if (this.field_504 >= 1.0F) {
						this.field_504 = 1.0F;
						if (!this.world.isClient) {
							this.field_511 = 10;
							this.client.soundHelper.playSound(dimensionbase.soundTravel, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
							DimensionBase.usePortal(this.portal);
						}
					}

					this.field_512 = false;
				} else {
					if (this.field_504 > 0.0F) {
						this.field_504 -= 0.05F;
					}

					if (this.field_504 < 0.0F) {
						this.field_504 = 0.0F;
					}
				}
			}

			if (this.field_511 > 0) {
				--this.field_511;
			}

			this.playerKeypressManager.updatePlayer(this);
			if (this.playerKeypressManager.sneak && this.field_1640 < 0.2F) {
				this.field_1640 = 0.2F;
			}

			this.method_1372(this.x - (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.z + (double)this.width * 0.35);
			this.method_1372(this.x - (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.z - (double)this.width * 0.35);
			this.method_1372(this.x + (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.z - (double)this.width * 0.35);
			this.method_1372(this.x + (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.z + (double)this.width * 0.35);
			if (this.world.difficulty == 0 && this.health < 20 && this.field_1645 % 20 * 12 == 0) {
				this.addHealth(1);
			}

			this.inventory.tickInventory();
			this.field_524 = this.field_525;
			if (this.field_1051 > 0) {
				double d = this.x + (this.field_1052 - this.x) / (double)this.field_1051;
				double d1 = this.y + (this.field_1053 - this.y) / (double)this.field_1051;
				double d2 = this.z + (this.field_1054 - this.z) / (double)this.field_1051;
				double d3 = this.field_1055 - (double)this.yaw;

				while(d3 < -180.0) {
					d3 += 360.0;
				}

				while(d3 >= 180.0) {
					d3 -= 360.0;
				}

				this.yaw = (float)((double)this.yaw + d3 / (double)this.field_1051);
				this.pitch = (float)((double)this.pitch + (this.field_1056 - (double)this.pitch) / (double)this.field_1051);
				--this.field_1051;
				this.setPosition(d, d1, d2);
				this.setRotation(this.yaw, this.pitch);
				List<AxixAlignedBoundingBox> list2 = this.world.method_190(this, this.boundingBox.method_104(0.03125, 0.0, 0.03125));
				if (list2.size() > 0) {
					double d4 = 0.0;

					for (AxixAlignedBoundingBox axixAlignedBoundingBox : list2) {
						if (axixAlignedBoundingBox.maxY > d4) {
							d4 = axixAlignedBoundingBox.maxY;
						}
					}

					d1 += d4 - this.boundingBox.minY;
					this.setPosition(d, d1, d2);
				}
			}

			if (this.cannotMove()) {
				this.jumping = false;
				this.horizontalVelocity = 0.0F;
				this.forwardVelocity = 0.0F;
				this.field_1030 = 0.0F;
			} else if (!this.field_1026) {
				this.tickHandSwing();
			}

			boolean flag = this.method_1334();
			boolean flag1 = this.method_1335();
			if (this.jumping) {
				if (flag) {
					this.yVelocity += 0.04F;
				} else if (flag1) {
					this.yVelocity += 0.04F;
				} else if (this.onGround) {
					this.jump();
				}
			}

			this.horizontalVelocity *= 0.98F;
			this.forwardVelocity *= 0.98F;
			this.field_1030 *= 0.9F;
			this.travel(this.horizontalVelocity, this.forwardVelocity);
			List<Entity> list = this.world.getEntities(this, this.boundingBox.expand(0.2000000029802322, 0.0, 0.2000000029802322));
			if (list != null && list.size() > 0) {
				for (Entity entity : list) {
					if (entity.method_1380()) {
						entity.method_1353(this);
					}
				}
			}

			float f = MathHelper.sqrt(this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity);
			float f1 = (float)Math.atan(-this.yVelocity * 0.2000000029802322) * 15.0F;
			if (f > 0.1F) {
				f = 0.1F;
			}

			if (!this.onGround || this.health <= 0) {
				f = 0.0F;
			}

			if (this.onGround || this.health <= 0) {
				f1 = 0.0F;
			}

			this.field_525 += (f - this.field_525) * 0.4F;
			this.field_1044 += (f1 - this.field_1044) * 0.8F;
			if (this.health > 0) {
				List<Entity> list1 = this.world.getEntities(this, this.boundingBox.expand(1.0, 0.0, 1.0));
				if (list1 != null) {
					for (Entity entity1 : list1) {
						if (!entity1.removed) {
							this.method_520(entity1);
						}
					}
				}
			}

		}
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void respawn() {
		if (!this.sapi.PAPIrespawn(this)) {
			this.method_1923();
			this.networkHandler.sendPacket(new RespawnC2SPacket((byte)this.dimensionId));
			DimensionBase.respawn(false, 0);
		}
	}
}
