package io.github.betterthanupdates.babricated;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class BabricatedModRemapper implements ModRemapper {
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	private Path getLibPath(String name) {
		return BabricatedForge.MOD_CONTAINER.findPath("./libs/" + name + ".zip").orElseThrow(RuntimeException::new);
	}

	@Override
	public RemapLibrary[] getRemapLibraries() {
		RemapLibrary[] libraries = new RemapLibrary[0];
		switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT:
				libraries = new RemapLibrary[] {
					new RemapLibrary(getLibPath("modloader-b1.7.3"), new ArrayList<>(), "modloader.zip"),
					new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-v2"), new ArrayList<>(), "modloadermp-client.zip"),
					new RemapLibrary(getLibPath("minecraftforge-client-1.0.7-20110907"), new ArrayList<>(), "forge-client.zip"),
					new RemapLibrary(getLibPath("audiomod-b1.7.3"), new ArrayList<>(), "audiomod.zip"),
					new RemapLibrary(getLibPath("shockahpi-r8"), new ArrayList<>(), "shockahpi.zip")
				};
				break;
			case SERVER:
				libraries = new RemapLibrary[] {
					new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-server-v2"), new ArrayList<>(), "modloadermp-server.zip"),
					new RemapLibrary(getLibPath("minecraftforge-server-1.0.7-20110907"), new ArrayList<>(), "forge-server.zip")
				};
				break;
		}

		return libraries;
	}

	@Override
	public Map<String, List<String>> getExclusions() {
		return new HashMap<>();
	}

	// TODO: Rename classes on right side to fit Yarn standards
	@Override
	public void getMappingList(RemapUtil.MappingList list) {
		// ModLoader mappings
		list.add("BaseMod", "modloader/BaseMod");
		list.add("EntityRendererProxy", "modloader/EntityRendererProxy");
		list.add("MLProp", "modloader/MLProp");
		list.add("ModLoader", "modloader/ModLoader");
		list.add("ModTextureAnimation", "modloader/ModTextureAnimation");
		list.add("ModTextureStatic", "modloader/ModTextureStatic");

		// ModLoaderMP mappings
		list.add("BaseModMp", "modloadermp/BaseModMp");
		list.add("ISpawnable", "modloadermp/ISpawnable");
		list.add("ModLoaderMp", "modloadermp/ModLoaderMp");
		list.add("NetClientHandlerEntity", "modloadermp/NetClientHandlerEntity");
		list.add("Packet230ModLoader", "modloadermp/Packet230ModLoader");

		// ShockAhPI
		list.add("ACPage", "shockahpi/AchievementPage");
		list.add("AnimBase", "shockahpi/AnimBase");
		list.add("AnimPulse", "shockahpi/AnimPulse");
		list.add("AnimShift", "shockahpi/AnimShift");
		list.add("BlockHarvestPower", "shockahpi/BlockHarvestPower");
		list.add("DimensionBase", "shockahpi/DimensionBase");
		list.add("DimensionNether", "shockahpi/DimensionNether");
		list.add("DimensionOverworld", "shockahpi/DimensionOverworld");
		list.add("DungeonLoot", "shockahpi/DungeonLoot");
		list.add("GuiYesNoFreezeDifficulty", "shockahpi/FreezeDifficultyScreen");
		list.add("GenDeposit", "shockahpi/GenDeposit");
		list.add("IInterceptBlockSet", "shockahpi/IInterceptBlockSet");
		list.add("IInterceptHarvest", "shockahpi/IInterceptHarvest");
		list.add("INBT", "shockahpi/INBT");
		list.add("IReachBlock", "shockahpi/IReachBlock");
		list.add("IReachEntity", "shockahpi/IReachEntity");
		list.add("Loc", "shockahpi/Loc");
		list.add("mod_SAPI", "shockahpi/ShockAhPI");
		list.add("PlayerBase", "shockahpi/PlayerBase");
		list.add("SAPI", "shockahpi/SAPI");
		list.add("SAPIEntityPlayerSP", "shockahpi/SapiClientPlayerEntity");
		list.add("Tool", "shockahpi/Tool");
		list.add("ToolBase", "shockahpi/ToolBase");
	}

	@Override
	public Optional<TinyRemapper.ApplyVisitorProvider> getPostRemappingVisitor() {
		return Optional.of(new BabricatedPostRemappingVisitor());
	}

	@Override
	public Optional<String> getDefaultPackage() {
		return Optional.of("net/minecraft/");
	}
}
