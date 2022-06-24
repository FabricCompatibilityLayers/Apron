package playerapi;

import java.util.ArrayList;
import java.util.List;

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

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.shockahpi.client.entity.player.ShockAhPIClientPlayerEntity;

@Legacy
public class PlayerAPI {
	@Legacy
	public static List<Class<? extends PlayerBase>> playerBaseClasses = new ArrayList<>();

	@Legacy
	public PlayerAPI() {
	}

	@Legacy
	public static void RegisterPlayerBase(Class<? extends PlayerBase> pb) {
		playerBaseClasses.add(pb);
	}

	private static List<PlayerBase> bases(AbstractClientPlayerEntity player) {
		return ((ShockAhPIClientPlayerEntity) player).getPlayerBases();
	}

	@Legacy
	public static <P extends PlayerBase> P getPlayerBase(AbstractClientPlayerEntity player, Class<P> pb) {
		for (int i = 0; i < bases(player).size(); ++i) {
			if (pb.isInstance(bases(player).get(i))) {
				return (P) bases(player).get(i);
			}
		}

		return null;
	}

	@Legacy
	public static List<PlayerBase> playerInit(AbstractClientPlayerEntity player) {
		List<PlayerBase> playerBases = new ArrayList<>();

		for (Class<? extends PlayerBase> playerBaseClass : playerBaseClasses) {
			try {
				playerBases.add(playerBaseClass.getDeclaredConstructor(AbstractClientPlayerEntity.class).newInstance(player));
			} catch (Exception var4) {
				var4.printStackTrace();
			}
		}

		return playerBases;
	}

	@Legacy
	public static boolean onLivingUpdate(AbstractClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onLivingUpdate()) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean respawn(AbstractClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).respawn()) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean moveFlying(AbstractClientPlayerEntity player, float x, float y, float z) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).moveFlying(x, y, z)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean updatePlayerActionState(AbstractClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).updatePlayerActionState()) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean handleKeyPress(AbstractClientPlayerEntity player, int j, boolean flag) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).handleKeyPress(j, flag)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean writeEntityToNBT(AbstractClientPlayerEntity player, CompoundTag tag) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).writeEntityToNBT(tag)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean readEntityFromNBT(AbstractClientPlayerEntity player, CompoundTag tag) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).readEntityFromNBT(tag)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean onExitGUI(AbstractClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onExitGUI()) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean setEntityDead(AbstractClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).setEntityDead()) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean onDeath(AbstractClientPlayerEntity player, Entity killer) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onDeath(killer)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean attackEntityFrom(AbstractClientPlayerEntity player, Entity attacker, int damage) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).attackEntityFrom(attacker, damage)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static double getDistanceSq(AbstractClientPlayerEntity player, double d, double d1, double d2, double answer) {
		for (int i = 0; i < bases(player).size(); ++i) {
			answer = bases(player).get(i).getDistanceSq(d, d1, d2, answer);
		}

		return answer;
	}

	@Legacy
	public static boolean isInWater(AbstractClientPlayerEntity player, boolean inWater) {
		for (int i = 0; i < bases(player).size(); ++i) {
			inWater = bases(player).get(i).isInWater(inWater);
		}

		return inWater;
	}

	@Legacy
	public static boolean canTriggerWalking(AbstractClientPlayerEntity player, boolean canTrigger) {
		for (int i = 0; i < bases(player).size(); ++i) {
			canTrigger = bases(player).get(i).canTriggerWalking(canTrigger);
		}

		return canTrigger;
	}

	@Legacy
	public static boolean heal(AbstractClientPlayerEntity player, int j) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).heal(j)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static int getPlayerArmorValue(AbstractClientPlayerEntity player, int armor) {
		for (int i = 0; i < bases(player).size(); ++i) {
			armor = bases(player).get(i).getPlayerArmorValue(armor);
		}

		return armor;
	}

	@Legacy
	public static float getCurrentPlayerStrVsBlock(AbstractClientPlayerEntity player, Block block, float f) {
		for (int i = 0; i < bases(player).size(); ++i) {
			f = bases(player).get(i).getCurrentPlayerStrVsBlock(block, f);
		}

		return f;
	}

	@Legacy
	public static boolean moveEntity(AbstractClientPlayerEntity player, double d, double d1, double d2) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).moveEntity(d, d1, d2)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static SleepStatus sleepInBedAt(AbstractClientPlayerEntity player, int x, int y, int z) {
		SleepStatus status = null;

		for (int i = 0; i < bases(player).size(); ++i) {
			status = bases(player).get(i).sleepInBedAt(x, y, z, status);
		}

		return status;
	}

	@Legacy
	public static float getEntityBrightness(AbstractClientPlayerEntity player, float f, float brightness) {
		for (int i = 0; i < bases(player).size(); ++i) {
			f = bases(player).get(i).getEntityBrightness(f, brightness);
		}

		return f;
	}

	@Legacy
	public static boolean pushOutOfBlocks(AbstractClientPlayerEntity player, double d, double d1, double d2) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).pushOutOfBlocks(d, d1, d2)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean onUpdate(AbstractClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onUpdate()) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static void afterUpdate(AbstractClientPlayerEntity player) {
		for (int i = 0; i < bases(player).size(); ++i) {
			bases(player).get(i).afterUpdate();
		}
	}

	@Legacy
	public static boolean moveEntityWithHeading(AbstractClientPlayerEntity player, float f, float f1) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).moveEntityWithHeading(f, f1)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean isOnLadder(AbstractClientPlayerEntity player, boolean onLadder) {
		for (int i = 0; i < bases(player).size(); ++i) {
			onLadder = bases(player).get(i).isOnLadder(onLadder);
		}

		return onLadder;
	}

	@Legacy
	public static boolean isInsideOfMaterial(AbstractClientPlayerEntity player, Material material, boolean inMaterial) {
		for (int i = 0; i < bases(player).size(); ++i) {
			inMaterial = bases(player).get(i).isInsideOfMaterial(material, inMaterial);
		}

		return inMaterial;
	}

	@Legacy
	public static boolean isSneaking(AbstractClientPlayerEntity player, boolean sneaking) {
		for (int i = 0; i < bases(player).size(); ++i) {
			sneaking = bases(player).get(i).isSneaking(sneaking);
		}

		return sneaking;
	}

	@Legacy
	public static boolean dropCurrentItem(AbstractClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).dropCurrentItem()) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean dropPlayerItem(AbstractClientPlayerEntity player, ItemStack itemstack) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).dropPlayerItem(itemstack)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean displayGUIEditSign(AbstractClientPlayerEntity player, SignBlockEntity sign) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIEditSign(sign)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean displayGUIChest(AbstractClientPlayerEntity player, Inventory inventory) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIChest(inventory)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean displayWorkbenchGUI(AbstractClientPlayerEntity player, int i, int j, int k) {
		boolean override = false;

		for (int n = 0; n < bases(player).size(); ++n) {
			if (bases(player).get(n).displayWorkbenchGUI(i, j, k)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean displayGUIFurnace(AbstractClientPlayerEntity player, FurnaceBlockEntity furnace) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIFurnace(furnace)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean displayGUIDispenser(AbstractClientPlayerEntity player, DispenserBlockEntity dispenser) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIDispenser(dispenser)) {
				override = true;
			}
		}

		return override;
	}

	@Legacy
	public static boolean sendChatMessage(AbstractClientPlayerEntity entityplayersp, String s) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).sendChatMessage(s)) {
				flag = true;
			}
		}

		return flag;
	}

	@Legacy
	public static String getHurtSound(AbstractClientPlayerEntity entityplayersp) {
		String result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			String baseResult = bases(entityplayersp).get(i).getHurtSound(result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	@Legacy
	public static Boolean canHarvestBlock(AbstractClientPlayerEntity entityplayersp, Block block) {
		Boolean result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Boolean baseResult = bases(entityplayersp).get(i).canHarvestBlock(block, result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	@Legacy
	public static boolean fall(AbstractClientPlayerEntity entityplayersp, float f) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).fall(f)) {
				flag = true;
			}
		}

		return flag;
	}

	@Legacy
	public static boolean jump(AbstractClientPlayerEntity entityplayersp) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).jump()) {
				flag = true;
			}
		}

		return flag;
	}

	@Legacy
	public static boolean damageEntity(AbstractClientPlayerEntity entityplayersp, int i1) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).damageEntity(i1)) {
				flag = true;
			}
		}

		return flag;
	}

	@Legacy
	public static Double getDistanceSqToEntity(AbstractClientPlayerEntity entityplayersp, Entity entity) {
		Double result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Double baseResult = bases(entityplayersp).get(i).getDistanceSqToEntity(entity, result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	@Legacy
	public static boolean attackTargetEntityWithCurrentItem(AbstractClientPlayerEntity entityplayersp, Entity entity) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).attackTargetEntityWithCurrentItem(entity)) {
				flag = true;
			}
		}

		return flag;
	}

	@Legacy
	public static Boolean handleWaterMovement(AbstractClientPlayerEntity entityplayersp) {
		Boolean result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Boolean baseResult = bases(entityplayersp).get(i).handleWaterMovement(result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	@Legacy
	public static Boolean handleLavaMovement(AbstractClientPlayerEntity entityplayersp) {
		Boolean result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Boolean baseResult = bases(entityplayersp).get(i).handleLavaMovement(result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	@Legacy
	public static boolean dropPlayerItemWithRandomChoice(AbstractClientPlayerEntity entityplayersp, ItemStack itemstack, boolean flag1) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).dropPlayerItemWithRandomChoice(itemstack, flag1)) {
				flag = true;
			}
		}

		return flag;
	}

	@Legacy
	public static void beforeUpdate(AbstractClientPlayerEntity entityplayersp) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).beforeUpdate();
		}
	}

	@Legacy
	public static void beforeMoveEntity(AbstractClientPlayerEntity entityplayersp, double d, double d1, double d2) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).beforeMoveEntity(d, d1, d2);
		}
	}

	@Legacy
	public static void afterMoveEntity(AbstractClientPlayerEntity entityplayersp, double d, double d1, double d2) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).afterMoveEntity(d, d1, d2);
		}
	}

	@Legacy
	public static void beforeSleepInBedAt(AbstractClientPlayerEntity entityplayersp, int i1, int j, int k) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).beforeSleepInBedAt(i1, j, k);
		}
	}
}
