package io.github.betterthanupdates.babricated;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;

@ApiStatus.Internal
public final class BabricatedModRemapper implements ModRemapper {
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	private Path getLibPath(@NotNull String name) {
		if (!name.isEmpty()) {
			return BabricatedForge.MOD_CONTAINER
					.orElseThrow(RuntimeException::new)
					.findPath("/libs/" + name + ".zip")
					.orElseThrow(RuntimeException::new);
		} else {
			throw new IllegalStateException("No library name is provided.");
		}
	}

	@Override
	public RemapLibrary[] getRemapLibraries() {
		switch (FabricLoader.getInstance().getEnvironmentType()) {
			default:
			case CLIENT:
				return new RemapLibrary[] {
						new RemapLibrary(getLibPath("modloader-b1.7.3"), new ArrayList<>(), "modloader.zip"),
						new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-v2"), new ArrayList<>(), "modloadermp-client.zip"),
						new RemapLibrary(getLibPath("minecraftforge-client-1.0.7-20110907"), new ArrayList<>(), "forge-client.zip"),
						new RemapLibrary(getLibPath("shockahpi-r8"), new ArrayList<>(), "shockahpi.zip")
				};
			case SERVER:
				return new RemapLibrary[] {
						new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-server-v2"), new ArrayList<>(), "modloadermp-server.zip"),
						new RemapLibrary(getLibPath("minecraftforge-server-1.0.7-20110907"), new ArrayList<>(), "forge-server.zip"),
						new RemapLibrary(getLibPath("shockahpi-r8"), new ArrayList<>(), "shockahpi.zip")
				};
		}
	}

	@Override
	public Map<String, List<String>> getExclusions() {
		return new HashMap<>();
	}

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
		// TODO: Rename classes on right side to fit Yarn standards
		list.add("ACPage", "shockahpi/AchievementPage");
		list.add("AnimBase", "shockahpi/AnimBase");
		list.add("AnimPulse", "shockahpi/AnimPulse");
		list.add("AnimShift", "shockahpi/AnimShift");
		list.add("BlockHarvestPower", "shockahpi/BlockHarvestPower");
		list.add("DimensionBase", "shockahpi/DimensionBase");
		list.add("GuiYesNoFreezeDifficulty", "shockahpi/FreezeDifficultyScreen");
		list.add("IInterceptBlockSet", "shockahpi/IInterceptBlockSet");
		list.add("IInterceptHarvest", "shockahpi/IInterceptHarvest");
		list.add("INBT", "shockahpi/INBT");
		list.add("IReachBlock", "shockahpi/IReachBlock");
		list.add("IReachEntity", "shockahpi/IReachEntity");
		list.add("SAPI", "shockahpi/SAPI");
		list.add("SAPIEntityPlayerSP", "shockahpi/SAPIClientPlayerEntity");
		list.add("mod_SAPI", "shockahpi/ShockAhPI");
	}

	@Override
	public Optional<TinyRemapper.ApplyVisitorProvider> getPostRemappingVisitor() {
		return Optional.empty();
	}
}
