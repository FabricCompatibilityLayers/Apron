package io.github.fabriccompatibilitylayers.modloader.compat.modmenu.babric;

import modloader.BaseMod;
import modloadermp.BaseModMp;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ContactInformation;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class MLModMetadata implements ModMetadata {
	private final BaseMod mod;

	public MLModMetadata(BaseMod mod) {
		this.mod = mod;
	}

	@Override
	public String getType() {
		return "modloader";
	}

	@Override
	public String getId() {
		return getModId();
	}

	@Override
	public Collection<String> getProvides() {
		return Collections.emptyList();
	}

	@Override
	public Version getVersion() {
		try {
			return Version.parse(this.mod.Version());
		} catch (VersionParsingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ModEnvironment getEnvironment() {
		return this.mod instanceof BaseModMp ? ModEnvironment.UNIVERSAL : ModEnvironment.CLIENT;
	}

	@Override
	public Collection<ModDependency> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return getModName();
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public Collection<Person> getAuthors() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Person> getContributors() {
		return Collections.emptyList();
	}

	@Override
	public ContactInformation getContact() {
		return ContactInformation.EMPTY;
	}

	@Override
	public Collection<String> getLicense() {
		return Collections.emptyList();
	}

	@Override
	public Optional<String> getIconPath(int size) {
		return Optional.empty();
	}

	@Override
	public boolean containsCustomValue(String key) {
		return false;
	}

	@Override
	public CustomValue getCustomValue(String key) {
		return null;
	}

	@Override
	public Map<String, CustomValue> getCustomValues() {
		return Collections.emptyMap();
	}

	@Override
	public boolean containsCustomElement(String key) {
		return false;
	}

	private String getModName() {
		return this.mod.toString().replace("net.minecraft.mod_", "");
	}

	private String getModId() {
		String name = getModName();
		if (name.contains(" ")) name = name.split(" ")[0];
		return "mod_" + name;
	}
}
