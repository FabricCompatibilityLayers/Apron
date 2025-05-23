package io.github.betterthanupdates.apron;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.MappingBuilder;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.ModRemapper;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.RemapLibrary;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.VisitorInfos;
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

	private Path getLibPath(String name) {
		return MOD_CONTAINER.findPath("./libs/" + name + ".zip")
				.orElseGet(() -> getLibDevPath(name));

	}

	private Path getLibDevPath(String name) {
		return MOD_CONTAINER.findPath("./../../../original/" + getEnvironment().name().toLowerCase() + "/" + name + ".zip").orElseThrow(RuntimeException::new);
	}

	@Override
	public String[] getJarFolders() {
		return new String[0];
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
	public void registerMappings(MappingBuilder mappingBuilder) {
		mappingBuilder.addMapping("ModLoader", "modloader/ModLoader")
				.field("usedItemSprites", "USED_ITEM_SPRITES", "[Z")
				.field("usedTerrainSprites", "USED_TERRAIN_SPRITES", "[Z");

		mappingBuilder.addMapping("ToolBase", "shockahpi/ToolBase")
				.field("Pickaxe", "PICKAXE", "LToolBase;")
				.field("Shovel", "SHOVEL", "LToolBase;")
				.field("Axe", "AXE", "LToolBase;");

		addMappingsFromMetadata(mappingBuilder, null);
		addMappingsFromMetadata(mappingBuilder, getEnvironment());
	}

	@Override
	public void registerPreVisitors(VisitorInfos visitorInfos) {

	}

	@Override
	public void registerPostVisitors(VisitorInfos visitorInfos) {
		// Reflection Remappers
		visitorInfos.registerInstantiation(
				"org/objectweb/asm/tree/FieldInsnNode",
				"io/github/betterthanupdates/apron/remapped/BetterFieldInsnNode"
		);
		visitorInfos.registerInstantiation(
				"org/objectweb/asm/tree/MethodInsnNode",
				"io/github/betterthanupdates/apron/remapped/BetterMethodInsnNode"
		);
		visitorInfos.registerInstantiation(
				"org/objectweb/asm/ClassWriter",
				"io/github/betterthanupdates/apron/remapped/BetterClassWriter"
		);
		visitorInfos.registerMethodInvocation(
				"java/lang/Class",
				"getDeclaredMethod",
				"(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/apron/remapped/RemapAwareClass",
						"getDeclaredMethod",
						"(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
						true
				)
		);
		visitorInfos.registerMethodInvocation(
				"java/lang/Class",
				"getDeclaredField",
				"(Ljava/lang/String;)Ljava/lang/reflect/Field;",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/apron/remapped/RemapAwareClass",
						"getDeclaredField",
						"(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;",
						true
				)
		);
		visitorInfos.registerMethodInvocation(
				"java/lang/Class",
				"getMethod",
				"(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/apron/remapped/RemapAwareClass",
						"getMethod",
						"(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;",
						true
				)
		);
		visitorInfos.registerMethodInvocation(
				"java/lang/Class",
				"getField",
				"(Ljava/lang/String;)Ljava/lang/reflect/Field;",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/apron/remapped/RemapAwareClass",
						"getField",
						"(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;",
						true
				)
		);
		visitorInfos.registerMethodInvocation(
				"java/lang/Class",
				"forName",
				"(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/apron/remapped/RemapAwareClass",
						"forName",
						"(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;",
						true
				)
		);
		visitorInfos.registerMethodInvocation(
				"java/lang/Class",
				"forName",
				"(Ljava/lang/String;)Ljava/lang/Class;",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/apron/remapped/RemapAwareClass",
						"forName",
						"(Ljava/lang/String;)Ljava/lang/Class;",
						true
				)
		);

		visitorInfos.registerMethodInvocation(
				"net/minecraft/class_13",
				"setRedstoneColors",
				"",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/forge/ForgeClientReflection",
						"BlockRenderer$setRedstoneColors",
						null
				)
		);

		visitorInfos.registerFieldRef(
				"net/minecraft/class_67",
				"renderingWorldRenderer",
				"",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/forge/ForgeClientReflection",
						"Tessellator$renderingWorldRenderer",
						null
				)
		);
		visitorInfos.registerFieldRef(
				"net/minecraft/class_67",
				"firstInstance",
				"",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/forge/ForgeClientReflection",
						"Tessellator$firstInstance",
						null
				)
		);
		visitorInfos.registerFieldRef(
				"net/minecraft/class_13",
				"cfgGrassFix",
				"",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/forge/ForgeClientReflection",
						"BlockRenderer$cfgGrassFix",
						null
				)
		);
		visitorInfos.registerFieldRef(
				"net/minecraft/class_13",
				"redstoneColors",
				"",
				new VisitorInfos.FullClassMember(
						"io/github/betterthanupdates/forge/ForgeClientReflection",
						"BlockRenderer$redstoneColors",
						null
				)
		);
	}

	@Override
	public Optional<String> getDefaultPackage() {
		return Optional.of("net/minecraft/");
	}

	/**
	 * Adds mappings directly from Apron's fabric.mod.json file.
	 *
	 * @param mappingBuilder        the mappings list for Mod Remapping API
	 * @param environment the current Minecraft environment, provided by Fabric Loader
	 */
	private void addMappingsFromMetadata(MappingBuilder mappingBuilder, @Nullable EnvType environment) {
		final ModMetadata metadata = MOD_CONTAINER.getMetadata();
		final String custom = "apron:" + (environment == null ? "common" : environment.name().toLowerCase(Locale.ENGLISH));

		for (Map.Entry<String, CustomValue> mapping : metadata.getCustomValue(custom).getAsObject()) {
			final String obfuscated = mapping.getKey();
			final String intermediary = mapping.getValue().getAsString();
			mappingBuilder.addMapping(obfuscated, intermediary);
			LOGGER.debug("%s remapped to %s for compatibility.", obfuscated, intermediary);
		}
	}

	public static EnvType getEnvironment() {
		return FabricLoader.getInstance().getEnvironmentType();
	}
}
