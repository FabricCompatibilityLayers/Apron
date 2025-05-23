package playerapi;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.class_141;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import io.github.betterthanupdates.Legacy;

/**
 * Callbacks provided by PlayerAPI that may be implemented in other mods.
 */
@Legacy
public abstract class PlayerBase {
	public ClientPlayerEntity player;

	public PlayerBase(ClientPlayerEntity p) {
		this.player = p;
	}

	public void playerInit() {
	}

	public boolean onLivingUpdate() {
		return false;
	}

	public boolean updatePlayerActionState() {
		return false;
	}

	public boolean handleKeyPress(int i, boolean flag) {
		return false;
	}

	public boolean writeEntityToNBT(NbtCompound tag) {
		return false;
	}

	public boolean readEntityFromNBT(NbtCompound tag) {
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

	public double getDistanceSq(double d, double d1, double d2, double answer) {
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

	public class_141 sleepInBedAt(int x, int y, int z, class_141 status) {
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

	public boolean dropPlayerItem(ItemStack itemstack) {
		return false;
	}

	public boolean displayGUIEditSign(SignBlockEntity sign) {
		return false;
	}

	public boolean displayGUIChest(Inventory iinventory) {
		return false;
	}

	public boolean displayWorkbenchGUI(int i, int j, int k) {
		return false;
	}

	public boolean displayGUIFurnace(FurnaceBlockEntity furnace) {
		return false;
	}

	public boolean displayGUIDispenser(DispenserBlockEntity dispenser) {
		return false;
	}

	public boolean sendChatMessage(String s) {
		return false;
	}

	public String getHurtSound(String previous) {
		return null;
	}

	public Boolean canHarvestBlock(Block block, Boolean previous) {
		return null;
	}

	public boolean fall(float f) {
		return false;
	}

	public boolean jump() {
		return false;
	}

	public boolean damageEntity(int i) {
		return false;
	}

	public Double getDistanceSqToEntity(Entity entity, Double previous) {
		return null;
	}

	public boolean attackTargetEntityWithCurrentItem(Entity entity) {
		return false;
	}

	public Boolean handleWaterMovement(Boolean previous) {
		return null;
	}

	public Boolean handleLavaMovement(Boolean previous) {
		return null;
	}

	public boolean dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		return false;
	}

	public void beforeUpdate() {
	}

	public void beforeMoveEntity(double d, double d1, double d2) {
	}

	public void afterMoveEntity(double d, double d1, double d2) {
	}

	public void beforeSleepInBedAt(int i, int j, int k) {
	}
}
