package io.github.betterthanupdates.apron.compat;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import fr.catcore.modremapperapi.utils.MixinUtils;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MixinCompatPlugin implements IMixinConfigPlugin {
	private static final Map<String, String> COMPAT = new HashMap<>();

	public MixinCompatPlugin() {
		COMPAT.put(".betterthanwolves.", "net.minecraft.mod_FCBetterThanWolves");
		COMPAT.put(".aether.", "net.minecraft.mod_Aether");
		COMPAT.put(".aethermp.", "net.mine_diver.aethermp.Core");
		COMPAT.put(".betatweaks.", "betatweaks.Utils");
		COMPAT.put(".hmi.", "hmi.Utils");
		COMPAT.put(".infsprites.", "net.mine_diver.infsprites.util.Util");
		COMPAT.put(".overrideapi.", "overrideapi.OverrideAPI");
		COMPAT.put(".portalgun.", "net.minecraft.mod_PortalGun");
		COMPAT.put(".reimap.", "reifnsk.minimap.ReiMinimap");
		COMPAT.put(".twilightforest.", "net.minecraft.mod_TwilightForest");
	}

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		for (Map.Entry<String, String> entry : COMPAT.entrySet()) {
			if (mixinClassName.contains(entry.getKey())) {
				try {
					Class.forName(entry.getValue(), false, getClass().getClassLoader());
					return true;
				} catch (ClassNotFoundException e) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		MixinUtils.applyASMMagic(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}
