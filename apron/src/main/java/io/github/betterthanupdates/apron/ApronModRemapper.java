package io.github.betterthanupdates.apron;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import fr.catcore.modremapperapi.remapping.VisitorInfos;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ApronModRemapper implements ModRemapper {
	public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("apron").orElseThrow(RuntimeException::new);
	public static final Logger LOGGER = Logger.get("ApronRemapper");
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	private Path getLibPath(String name) {
		return MOD_CONTAINER.findPath("./libs/" + name + ".zip")
				.orElseGet(() -> getLibDevPath(name));

    }

	private Path getLibDevPath(String name) {
		return MOD_CONTAINER.findPath("./../../../original/" + getEnvironment().name().toLowerCase() + "/" + name + ".zip").orElseThrow(RuntimeException::new);
	}

	@Override
	public void addRemapLibraries(List<RemapLibrary> libraries, EnvType environment) {
		switch (environment) {
			case CLIENT:
				libraries.add(new RemapLibrary(getLibPath("audiomod-b1.7.3"), "audiomod.zip"));
				libraries.add(new RemapLibrary(getLibPath("itemspriteapi-v1.2"), "itemspriteapi.zip"));
				libraries.add(new RemapLibrary(getLibPath("guiapi0.11.0-1.7"), "guiapi-0.11.0.zip"));
				libraries.add(new RemapLibrary(getLibPath("modloader-b1.7.3"), "modloader.zip"));
				libraries.add(new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-v2"), "modloadermp-client.zip"));
				libraries.add(new RemapLibrary(getLibPath("modoptionsapi-v0.7"), "modoptionsapi.zip"));
				libraries.add(new RemapLibrary(getLibPath("playerapi-1.7.3-v1.7"), "playerapi.zip"));
				libraries.add(new RemapLibrary(getLibPath("reforged-client-1.0.1"), "reforged-client.zip"));
				libraries.add(new RemapLibrary(getLibPath("shockahpi-r5.1"), "shockahpi-r5.1.zip"));
				break;
			case SERVER:
				libraries.add(new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-server-v2"), "modloadermp-server.zip"));
				libraries.add(new RemapLibrary(getLibPath("minecraftforge-server-1.0.7-20110907"), "forge-server.zip"));
				break;
		}
	}

	@Override
	public Map<String, List<String>> getExclusions() {
		return new HashMap<>();
	}

	@Override
	public void getMappingList(RemapUtil.MappingList list) {
		// TODO: Add this to custom mod metadata
		if (getEnvironment() == EnvType.CLIENT) {
			list.add("ToolBase", "shockahpi/ToolBase")
					.field("Pickaxe", "PICKAXE", "Lshockahpi/ToolBase;")
					.field("Shovel", "SHOVEL", "Lshockahpi/ToolBase;")
					.field("Axe", "AXE", "Lshockahpi/ToolBase;");
		}

		addMappingsFromMetadata(list, null);
		addMappingsFromMetadata(list, getEnvironment());
	}

	@Override
	public void registerVisitors(VisitorInfos infos) {
		infos.registerMethodMethodIns(
				new VisitorInfos.MethodNamed("net/minecraft/class_13", "setRedstoneColors"),
				new VisitorInfos.MethodNamed("io/github/betterthanupdates/forge/ForgeClientReflection", "BlockRenderer$setRedstoneColors")
		);

		infos.registerMethodFieldIns(
				new VisitorInfos.MethodNamed("net/minecraft/class_67", "renderingWorldRenderer"),
				new VisitorInfos.MethodNamed("io/github/betterthanupdates/forge/ForgeClientReflection", "Tessellator$renderingWorldRenderer")
		);
		infos.registerMethodFieldIns(
				new VisitorInfos.MethodNamed("net/minecraft/class_67", "firstInstance"),
				new VisitorInfos.MethodNamed("io/github/betterthanupdates/forge/ForgeClientReflection", "Tessellator$firstInstance")
		);
		infos.registerMethodFieldIns(
				new VisitorInfos.MethodNamed("net/minecraft/class_13", "cfgGrassFix"),
				new VisitorInfos.MethodNamed("io/github/betterthanupdates/forge/ForgeClientReflection", "BlockRenderer$cfgGrassFix")
		);
		infos.registerMethodFieldIns(
				new VisitorInfos.MethodNamed("net/minecraft/class_13", "redstoneColors"),
				new VisitorInfos.MethodNamed("io/github/betterthanupdates/forge/ForgeClientReflection", "BlockRenderer$redstoneColors")
		);
	}

	/**
	 * Adds mappings directly from Apron's fabric.mod.json file.
	 *
	 * @param list        the mappings list for Mod Remapping API
	 * @param environment the current Minecraft environment, provided by Fabric Loader
	 */
	private void addMappingsFromMetadata(RemapUtil.MappingList list, @Nullable EnvType environment) {
		final ModMetadata metadata = MOD_CONTAINER.getMetadata();
		final String custom = "apron:" + (environment == null ? "common" : environment.name().toLowerCase());

		for (Map.Entry<String, CustomValue> mapping : metadata.getCustomValue(custom).getAsObject()) {
			final String obfuscated = mapping.getKey();
			final String intermediary = mapping.getValue().getAsString();
			list.add(obfuscated, intermediary);
			LOGGER.debug("%s remapped to %s for compatibility.", obfuscated, intermediary);
		}
	}

	@Override
	public Optional<String> getDefaultPackage() {
		return Optional.of("net/minecraft/");
	}

	public static EnvType getEnvironment() {
		return FabricLoader.getInstance().getEnvironmentType();
	}
}
