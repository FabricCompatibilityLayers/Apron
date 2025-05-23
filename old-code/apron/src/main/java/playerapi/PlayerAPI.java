package playerapi;

import java.util.ArrayList;
import java.util.List;

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
import io.github.betterthanupdates.playerapi.client.entity.player.PlayerAPIClientPlayerEntity;

@Legacy
public class PlayerAPI {
	public static List<Class<? extends PlayerBase>> playerBaseClasses = new ArrayList<>();

	public PlayerAPI() {
	}

	public static void RegisterPlayerBase(Class<? extends PlayerBase> pb) {
		playerBaseClasses.add(pb);
	}

	private static List<PlayerBase> bases(ClientPlayerEntity player) {
		return ((PlayerAPIClientPlayerEntity) player).getPlayerBases();
	}

	public static <P extends PlayerBase> P getPlayerBase(ClientPlayerEntity player, Class<P> pb) {
		for (int i = 0; i < bases(player).size(); ++i) {
			if (pb.isInstance(bases(player).get(i))) {
				return (P) bases(player).get(i);
			}
		}

		return null;
	}

	public static List<PlayerBase> playerInit(ClientPlayerEntity player) {
		List<PlayerBase> playerBases = new ArrayList<>();

		for (Class<? extends PlayerBase> playerBaseClass : playerBaseClasses) {
			try {
				playerBases.add(playerBaseClass.getDeclaredConstructor(ClientPlayerEntity.class).newInstance(player));
			} catch (Exception var4) {
				var4.printStackTrace();
			}
		}

		return playerBases;
	}

	public static boolean onLivingUpdate(ClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onLivingUpdate()) {
				override = true;
			}
		}

		return override;
	}

	public static boolean respawn(ClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).respawn()) {
				override = true;
			}
		}

		return override;
	}

	public static boolean moveFlying(ClientPlayerEntity player, float x, float y, float z) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).moveFlying(x, y, z)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean updatePlayerActionState(ClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).updatePlayerActionState()) {
				override = true;
			}
		}

		return override;
	}

	public static boolean handleKeyPress(ClientPlayerEntity player, int j, boolean flag) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).handleKeyPress(j, flag)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean writeEntityToNBT(ClientPlayerEntity player, NbtCompound tag) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).writeEntityToNBT(tag)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean readEntityFromNBT(ClientPlayerEntity player, NbtCompound tag) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).readEntityFromNBT(tag)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean onExitGUI(ClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onExitGUI()) {
				override = true;
			}
		}

		return override;
	}

	public static boolean setEntityDead(ClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).setEntityDead()) {
				override = true;
			}
		}

		return override;
	}

	public static boolean onDeath(ClientPlayerEntity player, Entity killer) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onDeath(killer)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean attackEntityFrom(ClientPlayerEntity player, Entity attacker, int damage) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).attackEntityFrom(attacker, damage)) {
				override = true;
			}
		}

		return override;
	}

	public static double getDistanceSq(ClientPlayerEntity player, double d, double d1, double d2, double answer) {
		for (int i = 0; i < bases(player).size(); ++i) {
			answer = bases(player).get(i).getDistanceSq(d, d1, d2, answer);
		}

		return answer;
	}

	public static boolean isInWater(ClientPlayerEntity player, boolean inWater) {
		for (int i = 0; i < bases(player).size(); ++i) {
			inWater = bases(player).get(i).isInWater(inWater);
		}

		return inWater;
	}

	public static boolean canTriggerWalking(ClientPlayerEntity player, boolean canTrigger) {
		for (int i = 0; i < bases(player).size(); ++i) {
			canTrigger = bases(player).get(i).canTriggerWalking(canTrigger);
		}

		return canTrigger;
	}

	public static boolean heal(ClientPlayerEntity player, int j) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).heal(j)) {
				override = true;
			}
		}

		return override;
	}

	public static int getPlayerArmorValue(ClientPlayerEntity player, int armor) {
		for (int i = 0; i < bases(player).size(); ++i) {
			armor = bases(player).get(i).getPlayerArmorValue(armor);
		}

		return armor;
	}

	public static float getCurrentPlayerStrVsBlock(ClientPlayerEntity player, Block block, float f) {
		for (int i = 0; i < bases(player).size(); ++i) {
			f = bases(player).get(i).getCurrentPlayerStrVsBlock(block, f);
		}

		return f;
	}

	public static boolean moveEntity(ClientPlayerEntity player, double d, double d1, double d2) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).moveEntity(d, d1, d2)) {
				override = true;
			}
		}

		return override;
	}

	public static class_141 sleepInBedAt(ClientPlayerEntity player, int x, int y, int z) {
		class_141 status = null;

		for (int i = 0; i < bases(player).size(); ++i) {
			status = bases(player).get(i).sleepInBedAt(x, y, z, status);
		}

		return status;
	}

	public static float getEntityBrightness(ClientPlayerEntity player, float f, float brightness) {
		for (int i = 0; i < bases(player).size(); ++i) {
			f = bases(player).get(i).getEntityBrightness(f, brightness);
		}

		return f;
	}

	public static boolean pushOutOfBlocks(ClientPlayerEntity player, double d, double d1, double d2) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).pushOutOfBlocks(d, d1, d2)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean onUpdate(ClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).onUpdate()) {
				override = true;
			}
		}

		return override;
	}

	public static void afterUpdate(ClientPlayerEntity player) {
		for (int i = 0; i < bases(player).size(); ++i) {
			bases(player).get(i).afterUpdate();
		}
	}

	public static boolean moveEntityWithHeading(ClientPlayerEntity player, float f, float f1) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).moveEntityWithHeading(f, f1)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean isOnLadder(ClientPlayerEntity player, boolean onLadder) {
		for (int i = 0; i < bases(player).size(); ++i) {
			onLadder = bases(player).get(i).isOnLadder(onLadder);
		}

		return onLadder;
	}

	public static boolean isInsideOfMaterial(ClientPlayerEntity player, Material material, boolean inMaterial) {
		for (int i = 0; i < bases(player).size(); ++i) {
			inMaterial = bases(player).get(i).isInsideOfMaterial(material, inMaterial);
		}

		return inMaterial;
	}

	public static boolean isSneaking(ClientPlayerEntity player, boolean sneaking) {
		for (int i = 0; i < bases(player).size(); ++i) {
			sneaking = bases(player).get(i).isSneaking(sneaking);
		}

		return sneaking;
	}

	public static boolean dropCurrentItem(ClientPlayerEntity player) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).dropCurrentItem()) {
				override = true;
			}
		}

		return override;
	}

	public static boolean dropPlayerItem(ClientPlayerEntity player, ItemStack itemstack) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).dropPlayerItem(itemstack)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean displayGUIEditSign(ClientPlayerEntity player, SignBlockEntity sign) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIEditSign(sign)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean displayGUIChest(ClientPlayerEntity player, Inventory inventory) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIChest(inventory)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean displayWorkbenchGUI(ClientPlayerEntity player, int i, int j, int k) {
		boolean override = false;

		for (int n = 0; n < bases(player).size(); ++n) {
			if (bases(player).get(n).displayWorkbenchGUI(i, j, k)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean displayGUIFurnace(ClientPlayerEntity player, FurnaceBlockEntity furnace) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIFurnace(furnace)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean displayGUIDispenser(ClientPlayerEntity player, DispenserBlockEntity dispenser) {
		boolean override = false;

		for (int i = 0; i < bases(player).size(); ++i) {
			if (bases(player).get(i).displayGUIDispenser(dispenser)) {
				override = true;
			}
		}

		return override;
	}

	public static boolean sendChatMessage(ClientPlayerEntity entityplayersp, String s) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).sendChatMessage(s)) {
				flag = true;
			}
		}

		return flag;
	}

	public static String getHurtSound(ClientPlayerEntity entityplayersp) {
		String result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			String baseResult = bases(entityplayersp).get(i).getHurtSound(result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	public static Boolean canHarvestBlock(ClientPlayerEntity entityplayersp, Block block) {
		Boolean result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Boolean baseResult = bases(entityplayersp).get(i).canHarvestBlock(block, result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	public static boolean fall(ClientPlayerEntity entityplayersp, float f) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).fall(f)) {
				flag = true;
			}
		}

		return flag;
	}

	public static boolean jump(ClientPlayerEntity entityplayersp) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).jump()) {
				flag = true;
			}
		}

		return flag;
	}

	public static boolean damageEntity(ClientPlayerEntity entityplayersp, int i1) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).damageEntity(i1)) {
				flag = true;
			}
		}

		return flag;
	}

	public static Double getDistanceSqToEntity(ClientPlayerEntity entityplayersp, Entity entity) {
		Double result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Double baseResult = bases(entityplayersp).get(i).getDistanceSqToEntity(entity, result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	public static boolean attackTargetEntityWithCurrentItem(ClientPlayerEntity entityplayersp, Entity entity) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).attackTargetEntityWithCurrentItem(entity)) {
				flag = true;
			}
		}

		return flag;
	}

	public static Boolean handleWaterMovement(ClientPlayerEntity entityplayersp) {
		Boolean result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Boolean baseResult = bases(entityplayersp).get(i).handleWaterMovement(result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	public static Boolean handleLavaMovement(ClientPlayerEntity entityplayersp) {
		Boolean result = null;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			Boolean baseResult = bases(entityplayersp).get(i).handleLavaMovement(result);

			if (baseResult != null) {
				result = baseResult;
			}
		}

		return result;
	}

	public static boolean dropPlayerItemWithRandomChoice(ClientPlayerEntity entityplayersp, ItemStack itemstack, boolean flag1) {
		boolean flag = false;

		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			if (bases(entityplayersp).get(i).dropPlayerItemWithRandomChoice(itemstack, flag1)) {
				flag = true;
			}
		}

		return flag;
	}

	public static void beforeUpdate(ClientPlayerEntity entityplayersp) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).beforeUpdate();
		}
	}

	public static void beforeMoveEntity(ClientPlayerEntity entityplayersp, double d, double d1, double d2) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).beforeMoveEntity(d, d1, d2);
		}
	}

	public static void afterMoveEntity(ClientPlayerEntity entityplayersp, double d, double d1, double d2) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).afterMoveEntity(d, d1, d2);
		}
	}

	public static void beforeSleepInBedAt(ClientPlayerEntity entityplayersp, int i1, int j, int k) {
		for (int i = 0; i < bases(entityplayersp).size(); ++i) {
			bases(entityplayersp).get(i).beforeSleepInBedAt(i1, j, k);
		}
	}
}
