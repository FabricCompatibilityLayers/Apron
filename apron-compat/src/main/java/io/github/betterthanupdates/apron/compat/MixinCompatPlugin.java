package io.github.betterthanupdates.apron.compat;

import fr.catcore.modremapperapi.utils.MixinUtils;
import io.github.betterthanupdates.apron.ApronMixinPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MixinCompatPlugin extends ApronMixinPlugin {
	private static final Map<String, String> COMPAT = new HashMap<>();

	public MixinCompatPlugin() {
		COMPAT.put(".betterthanwolves.", "net.minecraft.mod_FCBetterThanWolves");
		COMPAT.put(".aether.", "net.minecraft.mod_Aether");
		COMPAT.put(".aethermp.", "net.minecraft.mod_AetherMp");
		COMPAT.put(".betatweaks.", "net.minecraft.mod_BetaTweaks");
		COMPAT.put(".hmi.", "net.minecraft.mod_HowManyItems");
		COMPAT.put(".infsprites.", "net.minecraft.mod_InfSprites");
		COMPAT.put(".overrideapi.common.", "overrideapi.utils.EnumHelper");
		COMPAT.put(".overrideapi.sarcasm.", "overrideapi.proxy.EntityRendererInjector");
		COMPAT.put(".overrideapi.old.", "overrideapi.info");
		COMPAT.put(".portalgun.", "net.minecraft.mod_PortalGun");
		COMPAT.put(".reimap.", "net.minecraft.mod_ReiMinimap");
		COMPAT.put(".twilightforest.", "net.minecraft.mod_TwilightForest");
		COMPAT.put(".somnia.", "net.minecraft.mod_Somnia");
		COMPAT.put(".betterblocks.", "net.minecraft.mod_BetterBlocks");
		COMPAT.put(".incrediblefungus.", "net.minecraft.mod_Fungus");
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		for (Map.Entry<String, String> entry : COMPAT.entrySet()) {
			if (mixinClassName.contains(entry.getKey()) && super.shouldApplyMixin(targetClassName, mixinClassName)) {
				try {
					Class.forName(entry.getValue(), false, getClass().getClassLoader());
					return true;
				} catch (ClassNotFoundException e) {
					return false;
				}
			}
		}

		return super.shouldApplyMixin(targetClassName, mixinClassName);
	}
}
