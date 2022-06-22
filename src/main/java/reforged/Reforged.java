package reforged;

import java.util.ArrayList;

import forge.MinecraftForge;
import modloader.ModLoader;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class Reforged {
	private static boolean searchedForIDResolver;
	private static boolean foundIDResolver;
	private static boolean searchedForSAPI;
	private static boolean foundSAPI;
	private static ArrayList<IReachEntity> reachesEntity = new ArrayList();
	public static boolean disableVersionCheckCrash = false;

	public Reforged() {
	}

	public static boolean hasIDResolver() {
		if (!searchedForIDResolver) {
			searchedForIDResolver = true;
			foundIDResolver = ModLoader.isModLoaded("mod_IDResolver");
		}

		return foundIDResolver;
	}

	public static boolean hasSAPI() {
		if (!searchedForSAPI) {
			searchedForSAPI = true;
			foundSAPI = hasClass("SAPI");
		}

		return foundSAPI;
	}

	private static boolean hasClass(String s) {
		Class class1 = null;

		try {
			class1 = Class.forName(s);
			return true;
		} catch (ClassNotFoundException var3) {
			return false;
		}
	}

	public static void reachAdd(reforged.IReachEntity ireachentity) {
		reachesEntity.add(ireachentity);
	}

	public static float reachGetEntityPlayer(PlayerEntity player) {
		ItemStack itemstack = player.inventory.getHeldItem();

		for(reforged.IReachEntity ireachentity : reachesEntity) {
			if (ireachentity.reachEntityItemMatches(itemstack)) {
				return ireachentity.getReachEntity(itemstack);
			}
		}

		return 3.0F;
	}

	public static void versionDetect(String modname, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			System.out
					.println(
							modname + ": Reforged version detect was called, but strict crashing is disabled. Expected version " + major + "." + minor + "." + revision
					);
		} else {
			if (major != 1) {
				MinecraftForge.killMinecraft(modname, "Reforged Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != 0) {
				if (minor > 0) {
					MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					System.out.println(modname + ": Reforged minor version mismatch, expecting " + major + "." + minor + ".x, may lead to unexpected behavior");
				}
			} else if (revision > 0) {
				MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
			}

		}
	}

	public static void versionDetectStrict(String modname, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			System.out
					.println(
							modname + ": Reforged version detect was called, but strict crashing is disabled. Expected version " + major + "." + minor + "." + revision
					);
		} else {
			if (major != 1) {
				MinecraftForge.killMinecraft(modname, "Reforged Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != 0) {
				if (minor > 0) {
					MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					MinecraftForge.killMinecraft(modname, "Reforged minor version mismatch, expecting " + major + "." + minor + ".x");
				}
			} else if (revision > 0) {
				MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
			}

		}
	}
}
