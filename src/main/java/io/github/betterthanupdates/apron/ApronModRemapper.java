package io.github.betterthanupdates.apron;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ApronModRemapper implements ModRemapper {
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	private Path getLibPath(String name) {
		return Apron.MOD_CONTAINER.findPath("./libs/" + name + ".zip").orElseThrow(RuntimeException::new);
	}

	@Override
	public void addRemapLibraries(List<RemapLibrary> libraries, EnvType environment) {
		switch (environment) {
			case CLIENT:
				libraries.add(new RemapLibrary(getLibPath("modloader-b1.7.3"), "modloader.zip"));
				libraries.add(new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-v2"), "modloadermp-client.zip"));
				libraries.add(new RemapLibrary(getLibPath("minecraftforge-client-1.0.7-20110907"), "forge-client.zip"));
				libraries.add(new RemapLibrary(getLibPath("audiomod-b1.7.3"), "audiomod.zip"));
				libraries.add(new RemapLibrary(getLibPath("shockahpi-r8"), "shockahpi.zip"));
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
		addMappingsFromMetadata(list, null);
		addMappingsFromMetadata(list, Apron.getEnvironment());
	}

	/**
	 * Adds mappings directly from Apron's fabric.mod.json file.
	 *
	 * @param list        the mappings list for Mod Remapping API
	 * @param environment the current Minecraft environment, provided by Fabric Loader
	 */
	private void addMappingsFromMetadata(RemapUtil.MappingList list, @Nullable EnvType environment) {
		final ModMetadata metadata = Apron.MOD_CONTAINER.getMetadata();
		final String custom = "apron:" + (environment == null ? "common" : environment.name().toLowerCase());

		for (Map.Entry<String, CustomValue> mapping : metadata.getCustomValue(custom).getAsObject()) {
			final String obfuscated = mapping.getKey();
			final String intermediary = mapping.getValue().getAsString();
			list.add(obfuscated, intermediary);
			Apron.LOGGER.debug("%s remapped to %s for compatibility.", intermediary, obfuscated);
		}
	}

	@Override
	public Optional<TinyRemapper.ApplyVisitorProvider> getPostRemappingVisitor() {
		return Optional.of(new ApronPostRemappingVisitor());
	}

	@Override
	public Optional<String> getDefaultPackage() {
		return Optional.of("net/minecraft/");
	}
}
