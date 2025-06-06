package io.github.fabriccompatibilitylayers.modloader;


import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class ApronModLoader {
	public static final boolean IS_CLIENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
}
