package io.github.fabriccompatibilitylayers.modloader.compat.modmenu.ornithe;

import com.terraformersmc.modmenu.api.UpdateChecker;
import com.terraformersmc.modmenu.api.UpdateInfo;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import modloader.BaseMod;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class MLMod implements Mod {
	private final BaseMod mod;

	public MLMod(BaseMod mod) {
		this.mod = mod;
	}

	private String getModName() {
		return this.mod.toString().replace("net.minecraft.mod_", "");
	}

	private String getModId() {
		String name = getModName();
		if (name.contains(" ")) name = name.split(" ")[0];
		return "mod_" + name;
	}

	@Override
	public @NotNull String getId() {
		return getModId();
	}

	@Override
	public @NotNull String getName() {
		return getModName();
	}

	@Override
	public @NotNull BufferedImage getIcon(FabricIconHandler fabricIconHandler, int i) {
		return fabricIconHandler.createIcon(FabricLoader.getInstance().getModContainer("modmenu").orElseThrow(() -> new RuntimeException("Cannot get ModContainer for Fabric mod with id modmenu")), "assets/modmenu/unknown_icon.png");
	}

	@Override
	public @NotNull String getDescription() {
		return "";
	}

	@Override
	public @NotNull String getVersion() {
		return this.mod.Version();
	}

	@Override
	public @NotNull String getPrefixedVersion() {
		return "v" + this.mod.Version();
	}

	@Override
	public @NotNull List<String> getAuthors() {
		return Collections.emptyList();
	}

	@Override
	public @NotNull Map<String, Collection<String>> getContributors() {
		return Collections.emptyMap();
	}

	@Override
	public @NotNull SortedMap<String, Set<String>> getCredits() {
		return new TreeMap<>();
	}

	@Override
	public @NotNull Set<Badge> getBadges() {
		return Collections.emptySet();
	}

	@Override
	public @Nullable String getWebsite() {
		return null;
	}

	@Override
	public @Nullable String getIssueTracker() {
		return null;
	}

	@Override
	public @Nullable String getSource() {
		return null;
	}

	@Override
	public @Nullable String getParent() {
		return null;
	}

	@Override
	public @NotNull Set<String> getLicense() {
		return Collections.emptySet();
	}

	@Override
	public @NotNull Map<String, String> getLinks() {
		return Collections.emptyMap();
	}

	@Override
	public boolean isReal() {
		return true;
	}

	@Override
	public boolean allowsUpdateChecks() {
		return false;
	}

	@Override
	public @Nullable UpdateChecker getUpdateChecker() {
		return null;
	}

	@Override
	public void setUpdateChecker(@Nullable UpdateChecker updateChecker) {

	}

	@Override
	public @Nullable UpdateInfo getUpdateInfo() {
		return null;
	}

	@Override
	public void setUpdateInfo(@Nullable UpdateInfo updateInfo) {

	}

	@Override
	public void setChildHasUpdate() {

	}

	@Override
	public boolean getChildHasUpdate() {
		return false;
	}

	@Override
	public boolean isHidden() {
		return false;
	}
}
