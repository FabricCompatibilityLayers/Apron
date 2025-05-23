package io.github.betterthanupdates.playerapi.client.entity.player;

import java.util.List;
import java.util.Random;

import net.minecraft.class_141;
import playerapi.PlayerBase;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

/**
 * Implements getter for public field patch in ClientPlayerEntity.
 */
public interface PlayerAPIClientPlayerEntity {
	void superUpdatePlayerActionState();

	void superOnLivingUpdate();

	void superOnUpdate();

	class_141 superSleepInBedAt(int i, int j, int k);

	Minecraft getMc();

	void superMoveEntity(double d, double d1, double d2);

	void setMoveForward(float f);

	void setMoveStrafing(float f);

	void setIsJumping(boolean flag);

	void superMoveFlying(float f, float f1, float f2);

	void doFall(float fallDist);

	float getFallDistance();

	boolean getSleeping();

	boolean getJumping();

	void doJump();

	Random getRandom();

	void setFallDistance(float f);

	void setYSize(float f);

	void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping);

	boolean superIsInsideOfMaterial(Material material);

	float superGetEntityBrightness(float f);

	String superGetHurtSound();

	float superGetCurrentPlayerStrVsBlock(Block block);

	boolean superCanHarvestBlock(Block block);

	void superFall(float f);

	void superJump();

	void superDamageEntity(int i);

	double superGetDistanceSqToEntity(Entity entity);

	void superAttackTargetEntityWithCurrentItem(Entity entity);

	boolean superHandleWaterMovement();

	boolean superHandleLavaMovement();

	void superDropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag);

	List<PlayerBase> getPlayerBases();
}
