package io.github.betterthanupdates.apron;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import fr.catcore.modremapperapi.utils.Constants;
import modloader.BaseMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.mapping.tree.ParameterDef;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.include.com.google.common.collect.ImmutableList;
import reforged.ReforgedMod;

@ApiStatus.Internal
public final class Apron {
	public static final String MOD_ID = "apron";
	public static final String NAME = "Apron";
	public static long fabricModCount = 0, rmlModCount = 0;

	// Logging
	public static final Logger LOGGER;

	public static final File MOD_CACHE_FOLDER;

	public static final ModContainer MOD_CONTAINER;

	public static final List<Supplier<? extends BaseMod>> BUILTIN_RML_MODS = ImmutableList.of(
			ReforgedMod::new
	);

	static {
		LOGGER = Logger.get(NAME);

		MOD_CACHE_FOLDER = new File(Constants.VERSIONED_FOLDER, "mods");

		if (!(MOD_CACHE_FOLDER.exists() || MOD_CACHE_FOLDER.mkdirs())) {
			throw new RuntimeException("Could not get or create mod cache folder!");
		}

		MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new);
	}

	/**
	 * For use in screen mixins.
	 *
	 * @param original what the line originally said
	 * @return the string to replace it with
	 */
	public static String versionString(final String original) {
		return "Minecraft Beta 1.7.3" + (original.endsWith(" (") ? " (" : "");
	}

	/**
	 * For use in screen mixins.
	 *
	 * @return a string telling how many mods are loaded from ModLoader
	 */
	public static String rmlModsLoaded() {
		return rmlModCount + " RML mods";
	}

	public static String fabricModsLoaded() {
		return fabricModCount + " Fabric mods";
	}

	public static String getRemappedFieldName(Class<?> type, String name) {
		final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

		for (ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
			if (def.getName(resolver.getCurrentRuntimeNamespace())
					.replace(".", "/").equals(type.getName().replace(".", "/"))) {
				for (FieldDef fieldDef : def.getFields()) {
					if (Objects.equals(fieldDef.getName(getEnvironment().equals(EnvType.CLIENT) ? "client" : "server"), name)) {
						return fieldDef.getName(resolver.getCurrentRuntimeNamespace());
					}
				}
			}
		}

		if (type.getSuperclass() != null) {
			name = getRemappedFieldName(type.getSuperclass(), name);
		}

		return name;
	}

	public static String getRemappedMethodName(Class<?> type, String name, Class<?>[] parameterNames) {
		final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

		for (ClassDef def : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
			if (def.getName(resolver.getCurrentRuntimeNamespace())
					.replace(".", "/").equals(type.getName().replace(".", "/"))) {
				for (MethodDef methodDef : def.getMethods()) {
					boolean cont = false;

					if (Objects.equals(methodDef.getName(getEnvironment().equals(EnvType.CLIENT) ? "client" : "server"), name)) {
						if (parameterNames.length == methodDef.getParameters().size()) {
							List<ParameterDef> parameterDefList = new ArrayList<>(methodDef.getParameters());

							for (int i = 0; i < parameterNames.length; i++) {
								String parameterName = parameterNames[i].getName();
								String mappedParameterName = parameterDefList.get(i).getName(resolver.getCurrentRuntimeNamespace());

								if (!parameterName.equals(mappedParameterName)) {
									cont = true;
									break;
								}
							}

							if (cont) continue;

							return methodDef.getName(resolver.getCurrentRuntimeNamespace());
						}
					}
				}
			}
		}

		if (type.getSuperclass() != null) {
			name = getRemappedMethodName(type.getSuperclass(), name, parameterNames);
		}

		return name;
	}

	/**
	 * A shortcut to the Fabric Environment getter.
	 */
	public static EnvType getEnvironment() {
		return FabricLoader.getInstance().getEnvironmentType();
	}
}
