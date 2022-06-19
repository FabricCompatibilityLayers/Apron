package shockahpi;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.block.DispenserBlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.entity.block.SignBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.achievement.Achievements;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.math.AxixAlignedBoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SapiClientPlayerEntity extends AbstractClientPlayerEntity {
	private static final SAPI sapi = new SAPI();
	public ArrayList<PlayerBase> playerBases = sapi.PAPIplayerInit(this);
	public int portal = -1;

	public SapiClientPlayerEntity(@Nullable Minecraft client, World world, Session session, int dimension) {
		super(client, world, session, dimension);
	}

	@Override
	public boolean damage(Entity entity, int i) {
		return !sapi.PAPIattackEntityFrom(this, entity, i) && super.damage(entity, i);
	}

	@Override
	public void onKilledBy(Entity entity) {
		if (!sapi.PAPIonDeath(this, entity)) {
			super.onKilledBy(entity);
		}
	}

	@Override
	public void tickHandSwing() {
		if (!sapi.PAPIupdatePlayerActionState(this)) {
			super.tickHandSwing();
		}
	}

	@Override
	public void movementInputToVelocity(float f, float f1, float f2) {
		if (!sapi.PAPImoveFlying(this, f, f1, f2)) {
			super.movementInputToVelocity(f, f1, f2);
		}
	}

	@Override
	protected boolean canClimb() {
		return sapi.PAPIcanTriggerWalking(this, true);
	}

	@Override
	public void method_136(int i, boolean flag) {
		if (!sapi.PAPIhandleKeyPress(this, i, flag)) {
			super.method_136(i, flag);
		}
	}

	@Override
	public void writeAdditional(CompoundTag compoundTag) {
		if (!sapi.PAPIwriteEntityToNBT(this, compoundTag)) {
			super.writeAdditional(compoundTag);
		}
	}

	@Override
	public void readAdditional(CompoundTag compoundTag) {
		if (!sapi.PAPIreadEntityFromNBT(this, compoundTag)) {
			super.readAdditional(compoundTag);
		}
	}

	@Override
	public void closeContainer() {
		if (!sapi.PAPIonExitGUI(this)) {
			super.closeContainer();
		}
	}

	@Override
	public void openSignScreen(SignBlockEntity signBlockEntity) {
		if (!sapi.PAPIdisplayGUIEditSign(this, signBlockEntity)) {
			super.openSignScreen(signBlockEntity);
		}
	}

	@Override
	public void openChestScreen(Inventory inventory) {
		if (!sapi.PAPIdisplayGUIChest(this, inventory)) {
			super.openChestScreen(inventory);
		}
	}

	@Override
	public void openCraftingScreen(int i, int j, int k) {
		if (!sapi.PAPIdisplayWorkbenchGUI(this, i, j, k)) {
			super.openCraftingScreen(i, j, k);
		}
	}

	@Override
	public void openFurnaceScreen(FurnaceBlockEntity furnaceBlockEntity) {
		if (!sapi.PAPIdisplayGUIFurnace(this, furnaceBlockEntity)) {
			super.openFurnaceScreen(furnaceBlockEntity);
		}
	}

	@Override
	public void openDispenserScreen(DispenserBlockEntity dispenserBlockEntity) {
		if (!sapi.PAPIdisplayGUIDispenser(this, dispenserBlockEntity)) {
			super.openDispenserScreen(dispenserBlockEntity);
		}
	}

	@Override
	public int getArmorValue() {
		return sapi.PAPIgetPlayerArmorValue(this, this.inventory.getArmorValue());
	}

	@Override
	public void remove() {
		if (!sapi.PAPIsetEntityDead(this)) {
			super.remove();
		}
	}

	@Override
	public double squaredDistanceTo(double d, double d1, double d2) {
		return sapi.PAPIgetDistanceSq(this, d, d1, d2, super.squaredDistanceTo(d, d1, d2));
	}

	@Override
	public boolean method_1334() {
		return sapi.PAPIisInWater(this, this.field_1612);
	}

	@Override
	public boolean method_1373() {
		return sapi.PAPIisSneaking(this, this.playerKeypressManager.sneak && !this.lyingOnBed);
	}

	@Override
	public float getStrengh(Block block) {
		return sapi.PAPIgetCurrentPlayerStrVsBlock(this, block, super.getStrengh(block));
	}

	@Override
	public void addHealth(int i) {
		if (!sapi.PAPIheal(this, i)) {
			super.addHealth(i);
		}
	}

	@Override
	protected boolean method_1372(double d, double d1, double d2) {
		return !sapi.PAPIpushOutOfBlocks(this, d, d1, d2) && super.method_1372(d, d1, d2);
	}

	@Override
	public SleepStatus trySleep(int i, int j, int k) {
		SleepStatus papiStatus = sapi.PAPIsleepInBedAt(this, i, j, k);
		return papiStatus == null ? super.trySleep(i, j, k) : papiStatus;
	}

	@Override
	public float getBrightnessAtEyes(float f) {
		return sapi.PAPIgetEntityBrightness(this, f, super.getBrightnessAtEyes(f));
	}

	@Override
	public void move(double d, double d1, double d2) {
		if (!sapi.PAPImoveEntity(this, d, d1, d2)) {
			super.move(d, d1, d2);
		}
	}

	@Override
	public void tick() {
		if (!sapi.PAPIonUpdate(this)) {
			super.tick();
		}

		sapi.PAPIafterUpdate(this);
	}

	@Override
	public void travel(float f, float f1) {
		if (!sapi.PAPImoveEntityWithHeading(this, f, f1)) {
			super.travel(f, f1);
		}
	}

	@Override
	public boolean method_932() {
		return sapi.PAPIisOnLadder(this, super.method_932());
	}

	@Override
	public boolean isInFluid(Material material) {
		return sapi.PAPIisInsideOfMaterial(this, material, super.isInFluid(material));
	}

	@Override
	public void dropSelectedItem() {
		if (!sapi.PAPIdropCurrentItem(this)) {
			super.dropSelectedItem();
		}
	}

	@Override
	public void dropItem(ItemStack stack) {
		if (!sapi.PAPIdropPlayerItem(this, stack)) {
			super.dropItem(stack);
		}
	}

	@Override
	public void updateDespawnCounter() {
		if (!sapi.PAPIonLivingUpdate(this)) {
			if (!this.client.statFileWriter.isAchievementUnlocked(Achievements.OPEN_INVENTORY)) {
				this.client.achievement.setAchievement(Achievements.OPEN_INVENTORY);
			}

			this.field_505 = this.field_504;

			if (this.portal != 0) {
				DimensionBase localDimensionBase = DimensionBase.getDimByNumber(this.portal);

				if (this.field_512) {
					if (!this.world.isClient && this.vehicle != null) {
						this.startRiding(null);
					}

					if (this.client.currentScreen != null) {
						this.client.openScreen(null);
					}

					if (this.field_504 == 0.0F) {
						this.client.soundHelper.playSound(localDimensionBase.soundTrigger, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
					}

					this.field_504 += 0.0125F;

					if (this.field_504 >= 1.0F) {
						this.field_504 = 1.0F;

						if (!this.world.isClient) {
							this.field_511 = 10;
							this.client.soundHelper.playSound(localDimensionBase.soundTravel, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
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

			this.method_1372(this.x - (double) this.width * 0.35, this.boundingBox.minY + 0.5, this.z + (double) this.width * 0.35);
			this.method_1372(this.x - (double) this.width * 0.35, this.boundingBox.minY + 0.5, this.z - (double) this.width * 0.35);
			this.method_1372(this.x + (double) this.width * 0.35, this.boundingBox.minY + 0.5, this.z - (double) this.width * 0.35);
			this.method_1372(this.x + (double) this.width * 0.35, this.boundingBox.minY + 0.5, this.z + (double) this.width * 0.35);

			if (this.world.difficulty == 0 && this.health < 20 && this.field_1645 % 20 * 12 == 0) {
				this.addHealth(1);
			}

			this.inventory.tickInventory();
			this.field_524 = this.field_525;

			if (this.field_1051 > 0) {
				double d1 = this.x + (this.field_1052 - this.x) / (double) this.field_1051;
				double d2 = this.y + (this.field_1053 - this.y) / (double) this.field_1051;
				double d3 = this.z + (this.field_1054 - this.z) / (double) this.field_1051;
				double d4 = this.field_1055 - (double) this.yaw;

				while (d4 < -180.0) {
					d4 += 360.0;
				}

				while (d4 >= 180.0) {
					d4 -= 360.0;
				}

				this.yaw = (float) ((double) this.yaw + d4 / (double) this.field_1051);
				this.pitch = (float) ((double) this.pitch + (this.field_1056 - (double) this.pitch) / (double) this.field_1051);
				--this.field_1051;
				this.setPosition(d1, d2, d3);
				this.setRotation(this.yaw, this.pitch);
				List<AxixAlignedBoundingBox> localList2 = this.world.method_190(this, this.boundingBox.method_104(0.03125, 0.0, 0.03125));

				if (localList2.size() > 0) {
					double d5 = 0.0;

					for (AxixAlignedBoundingBox localAABB : localList2) {
						if (!(localAABB.maxY <= d5)) {
							d5 = localAABB.maxY;
						}
					}

					d2 += d5 - this.boundingBox.minY;
					this.setPosition(d1, d2, d3);
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

			boolean bool1 = this.method_1334();
			boolean bool2 = this.method_1335();

			if (this.jumping) {
				if (bool1) {
					this.yVelocity += 0.04F;
				} else if (bool2) {
					this.yVelocity += 0.04F;
				} else if (this.onGround) {
					this.jump();
				}
			}

			this.horizontalVelocity *= 0.98F;
			this.forwardVelocity *= 0.98F;
			this.field_1030 *= 0.9F;
			this.travel(this.horizontalVelocity, this.forwardVelocity);
			List<Entity> localList1 = this.world.getEntities(this, this.boundingBox.expand(0.2000000029802322, 0.0, 0.2000000029802322));

			if (localList1 != null && localList1.size() > 0) {
				for (Entity localEntity : localList1) {
					if (localEntity.method_1380()) {
						localEntity.method_1353(this);
					}
				}
			}

			float f1 = MathHelper.sqrt(this.xVelocity * this.xVelocity + this.zVelocity * this.zVelocity);
			float f2 = (float) Math.atan(-this.yVelocity * 0.2000000029802322) * 15.0F;

			if (f1 > 0.1F) {
				f1 = 0.1F;
			}

			if (!this.onGround || this.health <= 0) {
				f1 = 0.0F;
			}

			if (this.onGround || this.health <= 0) {
				f2 = 0.0F;
			}

			this.field_525 += (f1 - this.field_525) * 0.4F;
			this.field_1044 += (f2 - this.field_1044) * 0.8F;

			if (this.health > 0) {
				List<Entity> localList = this.world.getEntities(this, this.boundingBox.expand(1.0, 0.0, 1.0));

				if (localList != null) {
					for (Entity localEntity : localList) {
						if (!localEntity.removed) {
							this.method_520(localEntity);
						}
					}
				}
			}
		}
	}

	@Override
	public void respawn() {
		if (!sapi.PAPIrespawn(this)) {
			DimensionBase.respawn(false, 0);
		}
	}
}
