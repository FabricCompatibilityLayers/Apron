package io.github.fabriccompatibilitylayers.modloader.compat.modmenu.babric;

import modloader.BaseMod;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.ModOrigin;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MLModContainer implements ModContainer {
	private final BaseMod mod;
	private final ModMetadata metadata;

	public MLModContainer(BaseMod mod) {
		this.mod = mod;
		this.metadata = new MLModMetadata(mod);
	}

	@Override
	public ModMetadata getMetadata() {
		return this.metadata;
	}

	@Override
	public List<Path> getRootPaths() {
		return Collections.emptyList();
	}

	@Override
	public ModOrigin getOrigin() {
		return new MLModOrigin();
	}

	@Override
	public Optional<ModContainer> getContainingMod() {
		return Optional.empty();
	}

	@Override
	public Collection<ModContainer> getContainedMods() {
		return Collections.emptyList();
	}

	@Override
	public Path getRootPath() {
		return null;
	}

	@Override
	public Path getPath(String file) {
		return null;
	}
}
