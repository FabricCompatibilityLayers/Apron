package playerapi;

import io.github.betterthanupdates.Legacy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.block.DispenserBlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.entity.block.SignBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;

@Legacy
public abstract class PlayerBase {
	@Legacy
	public AbstractClientPlayerEntity player;

	@Legacy
	public PlayerBase(AbstractClientPlayerEntity p) {
		this.player = p;
	}

	@Legacy
	public void playerInit() {
	}

	@Legacy
	public boolean onLivingUpdate() {
		return false;
	}

	@Legacy
	public boolean updatePlayerActionState() {
		return false;
	}

	@Legacy
	public boolean handleKeyPress(int i, boolean flag) {
		return false;
	}

	@Legacy
	public boolean writeEntityToNBT(CompoundTag tag) {
		return false;
	}

	@Legacy
	public boolean readEntityFromNBT(CompoundTag tag) {
		return false;
	}

	@Legacy
	public boolean setEntityDead() {
		return false;
	}

	@Legacy
	public boolean onDeath(Entity killer) {
		return false;
	}

	@Legacy
	public boolean respawn() {
		return false;
	}

	@Legacy
	public boolean attackEntityFrom(Entity attacker, int damage) {
		return false;
	}

	@Legacy
	public double getDistanceSq(double d, double d1, double d2, double answer) {
		return answer;
	}

	@Legacy
	public boolean isInWater(boolean inWater) {
		return inWater;
	}

	@Legacy
	public boolean onExitGUI() {
		return false;
	}

	@Legacy
	public boolean heal(int i) {
		return false;
	}

	@Legacy
	public boolean canTriggerWalking(boolean canTrigger) {
		return canTrigger;
	}

	@Legacy
	public int getPlayerArmorValue(int armor) {
		return armor;
	}

	@Legacy
	public float getCurrentPlayerStrVsBlock(Block block, float f) {
		return f;
	}

	@Legacy
	public boolean moveFlying(float x, float y, float z) {
		return false;
	}

	@Legacy
	public boolean moveEntity(double x, double y, double z) {
		return false;
	}

	@Legacy
	public SleepStatus sleepInBedAt(int x, int y, int z, SleepStatus status) {
		return status;
	}

	@Legacy
	public float getEntityBrightness(float f, float brightness) {
		return brightness;
	}

	@Legacy
	public boolean pushOutOfBlocks(double x, double y, double z) {
		return false;
	}

	@Legacy
	public boolean onUpdate() {
		return false;
	}

	@Legacy
	public void afterUpdate() {
	}

	@Legacy
	public boolean moveEntityWithHeading(float f, float f1) {
		return false;
	}

	@Legacy
	public boolean isOnLadder(boolean onLadder) {
		return onLadder;
	}

	@Legacy
	public boolean isInsideOfMaterial(Material material, boolean inMaterial) {
		return inMaterial;
	}

	@Legacy
	public boolean isSneaking(boolean sneaking) {
		return sneaking;
	}

	@Legacy
	public boolean dropCurrentItem() {
		return false;
	}

	@Legacy
	public boolean dropPlayerItem(ItemStack itemstack) {
		return false;
	}

	@Legacy
	public boolean displayGUIEditSign(SignBlockEntity sign) {
		return false;
	}

	@Legacy
	public boolean displayGUIChest(Inventory iinventory) {
		return false;
	}

	@Legacy
	public boolean displayWorkbenchGUI(int i, int j, int k) {
		return false;
	}

	@Legacy
	public boolean displayGUIFurnace(FurnaceBlockEntity furnace) {
		return false;
	}

	@Legacy
	public boolean displayGUIDispenser(DispenserBlockEntity dispenser) {
		return false;
	}

	@Legacy
	public boolean sendChatMessage(String s) {
		return false;
	}

	@Legacy
	public String getHurtSound(String previous) {
		return null;
	}

	@Legacy
	public Boolean canHarvestBlock(Block block, Boolean previous) {
		return null;
	}

	@Legacy
	public boolean fall(float f) {
		return false;
	}

	@Legacy
	public boolean jump() {
		return false;
	}

	@Legacy
	public boolean damageEntity(int i) {
		return false;
	}

	@Legacy
	public Double getDistanceSqToEntity(Entity entity, Double previous) {
		return null;
	}

	@Legacy
	public boolean attackTargetEntityWithCurrentItem(Entity entity) {
		return false;
	}

	@Legacy
	public Boolean handleWaterMovement(Boolean previous) {
		return null;
	}

	@Legacy
	public Boolean handleLavaMovement(Boolean previous) {
		return null;
	}

	@Legacy
	public boolean dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		return false;
	}

	@Legacy
	public void beforeUpdate() {
	}

	@Legacy
	public void beforeMoveEntity(double d, double d1, double d2) {
	}

	@Legacy
	public void afterMoveEntity(double d, double d1, double d2) {
	}

	@Legacy
	public void beforeSleepInBedAt(int i, int j, int k) {
	}
}
