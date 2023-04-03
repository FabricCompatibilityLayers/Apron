package io.github.betterthanupdates.apron.compat;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import io.github.betterthanupdates.apron.Apron;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinCompatPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
		MixinExtrasBootstrap.init();
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		//if (mixinClassName.contains("betterthanwolves")) {
		//	try {
		//		Class.forName("net.minecraft.mod_FCBetterThanWolves", false, getClass().getClassLoader());;
		//		System.out.println("Applying BTW specific mixin: " + mixinClassName);
		//		return true;
		//	} catch (ClassNotFoundException e) {
		//		return false;
		//	}
		//}
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

	}
}
