package io.github.betterthanupdates.playerapi.mixin.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import playerapi.PlayerAPI;
import playerapi.PlayerBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.PlayerKeypressManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.block.DispenserBlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.entity.block.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.world.World;

import io.github.betterthanupdates.playerapi.client.entity.player.PlayerAPIClientPlayerEntity;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements PlayerAPIClientPlayerEntity {
	@Shadow
	public PlayerKeypressManager playerKeypressManager;

	@Shadow
	public Minecraft client;

	public AbstractClientPlayerEntityMixin(World arg) {
		super(arg);
	}

	@Unique
	public List<PlayerBase> playerBases = new ArrayList<>();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void papi$init(Minecraft arg, World arg2, Session i, int par4, CallbackInfo ci) {
		this.playerBases = PlayerAPI.playerInit((AbstractClientPlayerEntity) (Object) this);
	}

	@Override
	public boolean damage(Entity entity, int i) {
		return !PlayerAPI.attackEntityFrom((AbstractClientPlayerEntity) (Object) this, entity, i) && super.damage(entity, i);
	}

	@Override
	public void onKilledBy(Entity entity) {
		if (!PlayerAPI.onDeath((AbstractClientPlayerEntity) (Object) this, entity)) {
			super.onKilledBy(entity);
		}
	}

	@Inject(method = "tickHandSwing", at = @At("HEAD"), cancellable = true)
	private void papi$tickHandSwing(CallbackInfo ci) {
		if (PlayerAPI.updatePlayerActionState((AbstractClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Override
	public void superUpdatePlayerActionState() {
		super.tickHandSwing();
	}

	@Inject(method = "updateDespawnCounter", at = @At("HEAD"), cancellable = true)
	private void papi$updateDespawnCounter(CallbackInfo ci) {
		if (PlayerAPI.onLivingUpdate((AbstractClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Override
	public void superOnLivingUpdate() {
		super.updateDespawnCounter();
	}

	@Override
	public void superOnUpdate() {
		super.tick();
	}

	@Override
	public void movementInputToVelocity(float f, float f1, float f2) {
		if (!PlayerAPI.moveFlying((AbstractClientPlayerEntity) (Object) this, f, f1, f2)) {
			super.movementInputToVelocity(f, f1, f2);
		}
	}

	@Override
	protected boolean canClimb() {
		return PlayerAPI.canTriggerWalking((AbstractClientPlayerEntity) (Object) this, true);
	}

	@Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
	private void papi$method_136(int i, boolean flag, CallbackInfo ci) {
		if (PlayerAPI.handleKeyPress((AbstractClientPlayerEntity) (Object) this, i, flag)) ci.cancel();
	}

	@Inject(method = "writeAdditional", at = @At("HEAD"), cancellable = true)
	private void papi$writeAdditional(CompoundTag par1, CallbackInfo ci) {
		if (PlayerAPI.writeEntityToNBT((AbstractClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "readAdditional", at = @At("HEAD"), cancellable = true)
	private void papi$readAdditional(CompoundTag par1, CallbackInfo ci) {
		if (PlayerAPI.readEntityFromNBT((AbstractClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "closeContainer", at = @At("HEAD"), cancellable = true)
	private void papi$closeContainer(CallbackInfo ci) {
		if (PlayerAPI.onExitGUI((AbstractClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Inject(method = "openSignScreen", at = @At("HEAD"), cancellable = true)
	private void papi$openSignScreen(SignBlockEntity par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIEditSign((AbstractClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "openChestScreen", at = @At("HEAD"), cancellable = true)
	private void papi$openChestScreen(Inventory par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIChest((AbstractClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "openCraftingScreen", at = @At("HEAD"), cancellable = true)
	private void papi$openCraftingScreen(int i, int j, int k, CallbackInfo ci) {
		if (PlayerAPI.displayWorkbenchGUI((AbstractClientPlayerEntity) (Object) this, i, j, k)) ci.cancel();
	}

	@Inject(method = "openFurnaceScreen", at = @At("HEAD"), cancellable = true)
	private void papi$openFurnaceScreen(FurnaceBlockEntity par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIFurnace((AbstractClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "openDispenserScreen", at = @At("HEAD"), cancellable = true)
	private void papi$openDispenserScreen(DispenserBlockEntity par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIDispenser((AbstractClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	/**
	 * @author PlayerAPI author
	 * @reason
	 */
	@Overwrite
	public int getArmorValue() {
		return PlayerAPI.getPlayerArmorValue((AbstractClientPlayerEntity) (Object) this, this.inventory.getArmorValue());
	}

	@Override
	public void remove() {
		if (!PlayerAPI.setEntityDead((AbstractClientPlayerEntity) (Object) this)) {
			super.remove();
		}
	}

	@Override
	public double squaredDistanceTo(double d, double d1, double d2) {
		return PlayerAPI.getDistanceSq((AbstractClientPlayerEntity) (Object) this, d, d1, d2, super.squaredDistanceTo(d, d1, d2));
	}

	@Override
	public boolean method_1334() {
		return PlayerAPI.isInWater((AbstractClientPlayerEntity) (Object) this, this.field_1612);
	}

	/**
	 * @author PlayerAPI author
	 * @reason
	 */
	@Overwrite
	public boolean method_1373() {
		return PlayerAPI.isSneaking((AbstractClientPlayerEntity) (Object) this, this.playerKeypressManager.sneak && !this.lyingOnBed);
	}

	@Override
	public float getStrengh(Block block) {
		float f = this.inventory.getStrengthOnBlock(block);

		if (this.isInFluid(Material.WATER)) {
			f /= 5.0F;
		}

		if (!this.onGround) {
			f /= 5.0F;
		}

		return PlayerAPI.getCurrentPlayerStrVsBlock((AbstractClientPlayerEntity) (Object) this, block, f);
	}

	@Override
	public void addHealth(int i) {
		if (!PlayerAPI.heal((AbstractClientPlayerEntity) (Object) this, i)) {
			super.addHealth(i);
		}
	}

	@Inject(method = "respawn", at = @At("HEAD"), cancellable = true)
	private void papi$respawn(CallbackInfo ci) {
		if (PlayerAPI.respawn((AbstractClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Inject(method = "method_1372", at = @At("HEAD"), cancellable = true)
	private void papi$method_1372(double d, double d1, double d2, CallbackInfoReturnable<Boolean> cir) {
		if (PlayerAPI.pushOutOfBlocks((AbstractClientPlayerEntity) (Object) this, d, d1, d2)) cir.setReturnValue(false);
	}

	@Override
	public SleepStatus superSleepInBedAt(int i, int j, int k) {
		return super.trySleep(i, j, k);
	}

	@Override
	public Minecraft getMc() {
		return this.client;
	}

	@Override
	public void superMoveEntity(double d, double d1, double d2) {
		super.move(d, d1, d2);
	}

	@Override
	public void setMoveForward(float f) {
		this.forwardVelocity = f;
	}

	@Override
	public void setMoveStrafing(float f) {
		this.horizontalVelocity = f;
	}

	@Override
	public void setIsJumping(boolean flag) {
		this.jumping = flag;
	}

	@Override
	public float getBrightnessAtEyes(float f) {
		return PlayerAPI.getEntityBrightness((AbstractClientPlayerEntity) (Object) this, f, super.getBrightnessAtEyes(f));
	}

	@Override
	public void tick() {
		PlayerAPI.beforeUpdate((AbstractClientPlayerEntity) (Object) this);

		if (!PlayerAPI.onUpdate((AbstractClientPlayerEntity) (Object) this)) {
			super.tick();
		}

		PlayerAPI.afterUpdate((AbstractClientPlayerEntity) (Object) this);
	}

	@Override
	public void superMoveFlying(float f, float f1, float f2) {
		super.movementInputToVelocity(f, f1, f2);
	}

	/**
	 * @author PlayerAPI author
	 * @reason
	 */
	@Overwrite
	public void move(double d, double d1, double d2) {
		PlayerAPI.beforeMoveEntity((AbstractClientPlayerEntity) (Object) this, d, d1, d2);

		if (!PlayerAPI.moveEntity((AbstractClientPlayerEntity) (Object) this, d, d1, d2)) {
			super.move(d, d1, d2);
		}

		PlayerAPI.afterMoveEntity((AbstractClientPlayerEntity) (Object) this, d, d1, d2);
	}

	@Override
	public SleepStatus trySleep(int i, int j, int k) {
		PlayerAPI.beforeSleepInBedAt((AbstractClientPlayerEntity) (Object) this, i, j, k);
		SleepStatus enumstatus = PlayerAPI.sleepInBedAt((AbstractClientPlayerEntity) (Object) this, i, j, k);
		return enumstatus == null ? super.trySleep(i, j, k) : enumstatus;
	}

	@Override
	public void doFall(float fallDist) {
		super.handleFallDamage(fallDist);
	}

	@Override
	public float getFallDistance() {
		return this.fallDistance;
	}

	@Override
	public boolean getSleeping() {
		return this.lyingOnBed;
	}

	@Override
	public boolean getJumping() {
		return this.jumping;
	}

	@Override
	public void doJump() {
		this.jump();
	}

	@Override
	public Random getRandom() {
		return this.rand;
	}

	@Override
	public void setFallDistance(float f) {
		this.fallDistance = f;
	}

	@Override
	public void setYSize(float f) {
		this.field_1640 = f;
	}

	@Override
	public void travel(float f, float f1) {
		if (!PlayerAPI.moveEntityWithHeading((AbstractClientPlayerEntity) (Object) this, f, f1)) {
			super.travel(f, f1);
		}
	}

	@Override
	public boolean method_932() {
		return PlayerAPI.isOnLadder((AbstractClientPlayerEntity) (Object) this, super.method_932());
	}

	@Override
	public void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping) {
		this.horizontalVelocity = newMoveStrafing;
		this.forwardVelocity = newMoveForward;
		this.jumping = newIsJumping;
	}

	@Override
	public boolean isInFluid(Material material) {
		return PlayerAPI.isInsideOfMaterial((AbstractClientPlayerEntity) (Object) this, material, super.isInFluid(material));
	}

	@Override
	public void dropSelectedItem() {
		if (!PlayerAPI.dropCurrentItem((AbstractClientPlayerEntity) (Object) this)) {
			super.dropSelectedItem();
		}
	}

	@Override
	public void dropItem(ItemStack itemstack) {
		if (!PlayerAPI.dropPlayerItem((AbstractClientPlayerEntity) (Object) this, itemstack)) {
			super.dropItem(itemstack);
		}
	}

	@Override
	public boolean superIsInsideOfMaterial(Material material) {
		return super.isInFluid(material);
	}

	@Override
	public float superGetEntityBrightness(float f) {
		return super.getBrightnessAtEyes(f);
	}

	/**
	 * @author PlayerAPI author
	 * @reason
	 */
	@Overwrite
	public void sendChatMessage(String s) {
		PlayerAPI.sendChatMessage((AbstractClientPlayerEntity) (Object) this, s);
	}

	@Override
	protected String getHurtSound() {
		String result = PlayerAPI.getHurtSound((AbstractClientPlayerEntity) (Object) this);
		return result != null ? result : super.getHurtSound();
	}

	@Override
	public String superGetHurtSound() {
		return super.getHurtSound();
	}

	@Override
	public float superGetCurrentPlayerStrVsBlock(Block block) {
		return super.getStrengh(block);
	}

	@Override
	public boolean canRemoveBlock(Block block) {
		Boolean result = PlayerAPI.canHarvestBlock((AbstractClientPlayerEntity) (Object) this, block);
		return result != null ? result : super.canRemoveBlock(block);
	}

	@Override
	public boolean superCanHarvestBlock(Block block) {
		return super.canRemoveBlock(block);
	}

	@Override
	protected void handleFallDamage(float f) {
		if (!PlayerAPI.fall((AbstractClientPlayerEntity) (Object) this, f)) {
			super.handleFallDamage(f);
		}
	}

	@Override
	public void superFall(float f) {
		super.handleFallDamage(f);
	}

	@Override
	protected void jump() {
		if (!PlayerAPI.jump((AbstractClientPlayerEntity) (Object) this)) {
			super.jump();
		}
	}

	@Override
	public void superJump() {
		super.jump();
	}

	@Override
	protected void applyDamage(int i) {
		if (!PlayerAPI.damageEntity((AbstractClientPlayerEntity) (Object) this, i)) {
			super.applyDamage(i);
		}
	}

	@Override
	public void superDamageEntity(int i) {
		super.applyDamage(i);
	}

	@Override
	public double method_1352(Entity entity) {
		Double result = PlayerAPI.getDistanceSqToEntity((AbstractClientPlayerEntity) (Object) this, entity);
		return result != null ? result : super.method_1352(entity);
	}

	@Override
	public double superGetDistanceSqToEntity(Entity entity) {
		return super.method_1352(entity);
	}

	@Override
	public void attack(Entity entity) {
		if (!PlayerAPI.attackTargetEntityWithCurrentItem((AbstractClientPlayerEntity) (Object) this, entity)) {
			super.attack(entity);
		}
	}

	@Override
	public void superAttackTargetEntityWithCurrentItem(Entity entity) {
		super.attack(entity);
	}

	@Override
	public boolean method_1393() {
		Boolean result = PlayerAPI.handleWaterMovement((AbstractClientPlayerEntity) (Object) this);
		return result != null ? result : super.method_1393();
	}

	@Override
	public boolean superHandleWaterMovement() {
		return super.method_1393();
	}

	@Override
	public boolean method_1335() {
		Boolean result = PlayerAPI.handleLavaMovement((AbstractClientPlayerEntity) (Object) this);
		return result != null ? result : super.method_1335();
	}

	@Override
	public boolean superHandleLavaMovement() {
		return super.method_1335();
	}

	@Override
	public void dropItem(ItemStack itemstack, boolean flag) {
		if (!PlayerAPI.dropPlayerItemWithRandomChoice((AbstractClientPlayerEntity) (Object) this, itemstack, flag)) {
			super.dropItem(itemstack, flag);
		}
	}

	@Override
	public void superDropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		super.dropItem(itemstack, flag);
	}

	@Override
	public List<PlayerBase> getPlayerBases() {
		return this.playerBases;
	}
}
