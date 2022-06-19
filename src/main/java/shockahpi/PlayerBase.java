package shockahpi;

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

/**
 * Callbacks provided by ShockAhPI that may be implemented in other mods.
 */
@SuppressWarnings("unused")
public abstract class PlayerBase {
	public AbstractClientPlayerEntity player;

	public PlayerBase(AbstractClientPlayerEntity player) {
		this.player = player;
	}

	public void playerInit() {
	}

	public boolean onLivingUpdate() {
		return false;
	}

	public boolean updatePlayerActionState() {
		return false;
	}

	public boolean handleKeyPress(int keyCode, boolean flag) {
		return false;
	}

	public boolean writeEntityToNBT(CompoundTag tag) {
		return false;
	}

	public boolean readEntityFromNBT(CompoundTag tag) {
		return false;
	}

	public boolean setEntityDead() {
		return false;
	}

	public boolean onDeath(Entity killer) {
		return false;
	}

	public boolean respawn() {
		return false;
	}

	public boolean attackEntityFrom(Entity attacker, int damage) {
		return false;
	}

	public double getDistanceSq(double x, double y, double z, double answer) {
		return answer;
	}

	public boolean isInWater(boolean inWater) {
		return inWater;
	}

	public boolean onExitGUI() {
		return false;
	}

	public boolean heal(int i) {
		return false;
	}

	public boolean canTriggerWalking(boolean canTrigger) {
		return canTrigger;
	}

	public int getPlayerArmorValue(int armor) {
		return armor;
	}

	public float getCurrentPlayerStrVsBlock(Block block, float f) {
		return f;
	}

	public boolean moveFlying(float x, float y, float z) {
		return false;
	}

	public boolean moveEntity(double x, double y, double z) {
		return false;
	}

	public SleepStatus sleepInBedAt(int x, int y, int z, SleepStatus status) {
		return status;
	}

	public float getEntityBrightness(float f, float brightness) {
		return brightness;
	}

	public boolean pushOutOfBlocks(double x, double y, double z) {
		return false;
	}

	public boolean onUpdate() {
		return false;
	}

	public void afterUpdate() {
	}

	public boolean moveEntityWithHeading(float f, float f1) {
		return false;
	}

	public boolean isOnLadder(boolean onLadder) {
		return onLadder;
	}

	public boolean isInsideOfMaterial(Material material, boolean inMaterial) {
		return inMaterial;
	}

	public boolean isSneaking(boolean sneaking) {
		return sneaking;
	}

	public boolean dropCurrentItem() {
		return false;
	}

	public boolean dropPlayerItem(ItemStack stack) {
		return false;
	}

	public boolean displayGUIEditSign(SignBlockEntity sign) {
		return false;
	}

	public boolean displayGUIChest(Inventory inventory) {
		return false;
	}

	public boolean displayWorkbenchGUI(int x, int y, int z) {
		return false;
	}

	public boolean displayGUIFurnace(FurnaceBlockEntity furnace) {
		return false;
	}

	public boolean displayGUIDispenser(DispenserBlockEntity dispenser) {
		return false;
	}
}
