package io.github.betterthanupdates.playerapi.mixin.client.entity.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.catcore.cursedmixinextensions.annotations.ShadowSuper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_141;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import playerapi.PlayerAPI;
import playerapi.PlayerBase;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import io.github.betterthanupdates.playerapi.client.entity.player.PlayerAPIClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity implements PlayerAPIClientPlayerEntity {
	@Shadow
	public Minecraft minecraft;

	public ClientPlayerEntityMixin(World arg) {
		super(arg);
	}

	@Unique
	public List<PlayerBase> playerBases = new ArrayList<>();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void papi$init(Minecraft arg, World arg2, Session i, int par4, CallbackInfo ci) {
		this.playerBases = PlayerAPI.playerInit((ClientPlayerEntity) (Object) this);
	}

	@Override
	public boolean damage(Entity entity, int i) {
		return !PlayerAPI.attackEntityFrom((ClientPlayerEntity) (Object) this, entity, i) && super.damage(entity, i);
	}

	@Override
	public void method_938(Entity entity) {
		if (!PlayerAPI.onDeath((ClientPlayerEntity) (Object) this, entity)) {
			super.method_938(entity);
		}
	}

	@Inject(method = "method_910", at = @At("HEAD"), cancellable = true)
	private void papi$tickHandSwing(CallbackInfo ci) {
		if (PlayerAPI.updatePlayerActionState((ClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Override
	public void superUpdatePlayerActionState() {
		super.method_910();
	}

	@Inject(method = "method_937", at = @At("HEAD"), cancellable = true)
	private void papi$updateDespawnCounter(CallbackInfo ci) {
		if (PlayerAPI.onLivingUpdate((ClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Override
	public void superOnLivingUpdate() {
		super.method_937();
	}

	@Override
	public void superOnUpdate() {
		super.tick();
	}

	@Override
	public void method_1324(float f, float f1, float f2) {
		if (!PlayerAPI.moveFlying((ClientPlayerEntity) (Object) this, f, f1, f2)) {
			super.method_1324(f, f1, f2);
		}
	}

	@Override
	protected boolean bypassesSteppingEffects() {
		return PlayerAPI.canTriggerWalking((ClientPlayerEntity) (Object) this, true);
	}

	@Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
	private void papi$method_136(int i, boolean flag, CallbackInfo ci) {
		if (PlayerAPI.handleKeyPress((ClientPlayerEntity) (Object) this, i, flag)) ci.cancel();
	}

	@Inject(method = "writeNbt", at = @At("HEAD"), cancellable = true)
	private void papi$writeAdditional(NbtCompound par1, CallbackInfo ci) {
		if (PlayerAPI.writeEntityToNBT((ClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "readNbt", at = @At("HEAD"), cancellable = true)
	private void papi$readAdditional(NbtCompound par1, CallbackInfo ci) {
		if (PlayerAPI.readEntityFromNBT((ClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "closeScreen", at = @At("HEAD"), cancellable = true)
	private void papi$closeContainer(CallbackInfo ci) {
		if (PlayerAPI.onExitGUI((ClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Inject(method = "method_489", at = @At("HEAD"), cancellable = true)
	private void papi$openSignScreen(SignBlockEntity par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIEditSign((ClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "method_486", at = @At("HEAD"), cancellable = true)
	private void papi$openChestScreen(Inventory par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIChest((ClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "method_484", at = @At("HEAD"), cancellable = true)
	private void papi$openCraftingScreen(int i, int j, int k, CallbackInfo ci) {
		if (PlayerAPI.displayWorkbenchGUI((ClientPlayerEntity) (Object) this, i, j, k)) ci.cancel();
	}

	@Inject(method = "method_487", at = @At("HEAD"), cancellable = true)
	private void papi$openFurnaceScreen(FurnaceBlockEntity par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIFurnace((ClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "method_485", at = @At("HEAD"), cancellable = true)
	private void papi$openDispenserScreen(DispenserBlockEntity par1, CallbackInfo ci) {
		if (PlayerAPI.displayGUIDispenser((ClientPlayerEntity) (Object) this, par1)) ci.cancel();
	}

	@Inject(method = "method_141", at = @At("RETURN"), cancellable = true)
	private void papi$getArmorValue(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(PlayerAPI.getPlayerArmorValue((ClientPlayerEntity) (Object) this, cir.getReturnValue()));
	}

	@Override
	public void markDead() {
		if (!PlayerAPI.setEntityDead((ClientPlayerEntity) (Object) this)) {
			super.markDead();
		}
	}

	@Override
	public double method_1347(double d, double d1, double d2) {
		return PlayerAPI.getDistanceSq((ClientPlayerEntity) (Object) this, d, d1, d2, super.method_1347(d, d1, d2));
	}

	@Override
	public boolean method_1334() {
		return PlayerAPI.isInWater((ClientPlayerEntity) (Object) this, this.field_1612);
	}

	@Inject(method = "method_1373", at = @At("RETURN"), cancellable = true)
	private void papi$isSneaking(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(PlayerAPI.isSneaking((ClientPlayerEntity) (Object) this, cir.getReturnValue()));
	}

	@Override
	public float method_511(Block block) {
		float f = this.inventory.method_674(block);

		if (this.isInFluid(Material.WATER)) {
			f /= 5.0F;
		}

		if (!this.field_1623) {
			f /= 5.0F;
		}

		return PlayerAPI.getCurrentPlayerStrVsBlock((ClientPlayerEntity) (Object) this, block, f);
	}

	@Override
	public void method_939(int i) {
		if (!PlayerAPI.heal((ClientPlayerEntity) (Object) this, i)) {
			super.method_939(i);
		}
	}

	@Inject(method = "respawn", at = @At("HEAD"), cancellable = true)
	private void papi$respawn(CallbackInfo ci) {
		if (PlayerAPI.respawn((ClientPlayerEntity) (Object) this)) ci.cancel();
	}

	@Inject(method = "pushOutOfBlock", at = @At("HEAD"), cancellable = true)
	private void papi$method_1372(double d, double d1, double d2, CallbackInfoReturnable<Boolean> cir) {
		if (PlayerAPI.pushOutOfBlocks((ClientPlayerEntity) (Object) this, d, d1, d2)) cir.setReturnValue(false);
	}

	@Override
	public class_141 superSleepInBedAt(int i, int j, int k) {
		return super.method_495(i, j, k);
	}

	@Override
	public Minecraft getMc() {
		return this.minecraft;
	}

	@Override
	public void superMoveEntity(double d, double d1, double d2) {
		super.move(d, d1, d2);
	}

	@Override
	public void setMoveForward(float f) {
		this.field_1029 = f;
	}

	@Override
	public void setMoveStrafing(float f) {
		this.field_1060 = f;
	}

	@Override
	public void setIsJumping(boolean flag) {
		this.jumping = flag;
	}

	@Override
	public float method_1394(float f) {
		return PlayerAPI.getEntityBrightness((ClientPlayerEntity) (Object) this, f, super.method_1394(f));
	}

	@Override
	public void tick() {
		PlayerAPI.beforeUpdate((ClientPlayerEntity) (Object) this);

		if (!PlayerAPI.onUpdate((ClientPlayerEntity) (Object) this)) {
			super.tick();
		}

		PlayerAPI.afterUpdate((ClientPlayerEntity) (Object) this);
	}

	@Override
	public void superMoveFlying(float f, float f1, float f2) {
		super.method_1324(f, f1, f2);
	}

	@Inject(method = "move", at = @At("HEAD"))
	private void papi$beforeMoveEntity(double d, double d1, double d2, CallbackInfo ci) {
		PlayerAPI.beforeMoveEntity((ClientPlayerEntity) (Object) this, d, d1, d2);
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;move(DDD)V"), cancellable = true)
	private void papi$moveEntity(double d, double d1, double d2, CallbackInfo ci) {
		if (PlayerAPI.moveEntity((ClientPlayerEntity) (Object) this, d, d1, d2)) {
			ci.cancel();
		}
	}

	@Inject(method = "move", at = @At("RETURN"))
	private void papi$afterMoveEntity(double d, double d1, double d2, CallbackInfo ci) {
		PlayerAPI.afterMoveEntity((ClientPlayerEntity) (Object) this, d, d1, d2);
	}

	@Override
	public class_141 method_495(int i, int j, int k) {
		PlayerAPI.beforeSleepInBedAt((ClientPlayerEntity) (Object) this, i, j, k);
		class_141 sleepStatus = PlayerAPI.sleepInBedAt((ClientPlayerEntity) (Object) this, i, j, k);
		return sleepStatus == null ? super.method_495(i, j, k) : sleepStatus;
	}

	@Override
	public void doFall(float fallDist) {
		super.method_1389(fallDist);
	}

	@Override
	public float getFallDistance() {
		return this.field_1636;
	}

	@Override
	public boolean getSleeping() {
		return this.sleeping;
	}

	@Override
	public boolean getJumping() {
		return this.jumping;
	}

	@Override
	public void doJump() {
		this.method_944();
	}

	@Override
	public Random getRandom() {
		return this.random;
	}

	@Override
	public void setFallDistance(float f) {
		this.field_1636 = f;
	}

	@Override
	public void setYSize(float f) {
		this.field_1640 = f;
	}

	@Override
	public void method_945(float f, float f1) {
		if (!PlayerAPI.moveEntityWithHeading((ClientPlayerEntity) (Object) this, f, f1)) {
			super.method_945(f, f1);
		}
	}

	@Override
	public boolean method_932() {
		return PlayerAPI.isOnLadder((ClientPlayerEntity) (Object) this, super.method_932());
	}

	@Override
	public void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping) {
		this.field_1060 = newMoveStrafing;
		this.field_1029 = newMoveForward;
		this.jumping = newIsJumping;
	}

	@Override
	public boolean isInFluid(Material material) {
		return PlayerAPI.isInsideOfMaterial((ClientPlayerEntity) (Object) this, material, super.isInFluid(material));
	}

	@Override
	public void dropSelectedItem() {
		if (!PlayerAPI.dropCurrentItem((ClientPlayerEntity) (Object) this)) {
			super.dropSelectedItem();
		}
	}

	@Override
	public void dropItem(ItemStack itemstack) {
		if (!PlayerAPI.dropPlayerItem((ClientPlayerEntity) (Object) this, itemstack)) {
			super.dropItem(itemstack);
		}
	}

	@Override
	public boolean superIsInsideOfMaterial(Material material) {
		return super.isInFluid(material);
	}

	@Override
	public float superGetEntityBrightness(float f) {
		return super.method_1394(f);
	}

	@Inject(method = "sendChatMessage", at = @At("RETURN"))
	private void papi$sendChatMessage(String s, CallbackInfo ci) {
		PlayerAPI.sendChatMessage((ClientPlayerEntity) (Object) this, s);
	}

	@Override
	protected String method_912() {
		String result = PlayerAPI.getHurtSound((ClientPlayerEntity) (Object) this);
		return result != null ? result : super.method_912();
	}

	@Override
	public String superGetHurtSound() {
		return super.method_912();
	}

	@Override
	public float superGetCurrentPlayerStrVsBlock(Block block) {
		return super.method_511(block);
	}

	@Override
	public boolean method_514(Block block) {
		Boolean result = PlayerAPI.canHarvestBlock((ClientPlayerEntity) (Object) this, block);
		return result != null ? result : super.method_514(block);
	}

	@Override
	public boolean superCanHarvestBlock(Block block) {
		return super.method_514(block);
	}

	@Override
	protected void method_1389(float f) {
		if (!PlayerAPI.fall((ClientPlayerEntity) (Object) this, f)) {
			super.method_1389(f);
		}
	}

	@Override
	public void superFall(float f) {
		super.method_1389(f);
	}

	@Override
	protected void method_944() {
		if (!PlayerAPI.jump((ClientPlayerEntity) (Object) this)) {
			super.method_944();
		}
	}

	@Override
	public void superJump() {
		super.method_944();
	}

	@Override
	protected void method_946(int i) {
		if (!PlayerAPI.damageEntity((ClientPlayerEntity) (Object) this, i)) {
			super.method_946(i);
		}
	}

	@ShadowSuper("superDamageEntity")
	public abstract void superSuperDamageEntity(int i);

	@Override
	public void superDamageEntity(int i) {
		if (FabricLoader.getInstance().isModLoaded("station-player-api-v0")) {
			this.superSuperDamageEntity(i);
		} else {
			super.method_946(i);
		}
	}

	@Override
	public double method_1352(Entity entity) {
		Double result = PlayerAPI.getDistanceSqToEntity((ClientPlayerEntity) (Object) this, entity);
		return result != null ? result : super.method_1352(entity);
	}

	@Override
	public double superGetDistanceSqToEntity(Entity entity) {
		return super.method_1352(entity);
	}

	@Override
	public void attack(Entity entity) {
		if (!PlayerAPI.attackTargetEntityWithCurrentItem((ClientPlayerEntity) (Object) this, entity)) {
			super.attack(entity);
		}
	}

	@Override
	public void superAttackTargetEntityWithCurrentItem(Entity entity) {
		super.attack(entity);
	}

	@Override
	public boolean isSubmergedInWater() {
		Boolean result = PlayerAPI.handleWaterMovement((ClientPlayerEntity) (Object) this);
		return result != null ? result : super.isSubmergedInWater();
	}

	@Override
	public boolean superHandleWaterMovement() {
		return super.isSubmergedInWater();
	}

	@Override
	public boolean method_1335() {
		Boolean result = PlayerAPI.handleLavaMovement((ClientPlayerEntity) (Object) this);
		return result != null ? result : super.method_1335();
	}

	@Override
	public boolean superHandleLavaMovement() {
		return super.method_1335();
	}

	@Override
	public void dropItem(ItemStack itemstack, boolean flag) {
		if (!PlayerAPI.dropPlayerItemWithRandomChoice((ClientPlayerEntity) (Object) this, itemstack, flag)) {
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
