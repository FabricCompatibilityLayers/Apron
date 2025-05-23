package io.github.betterthanupdates.apron.stapi;

import io.github.betterthanupdates.apron.ApronMixinPlugin;

public class MixinCompatPlugin extends ApronMixinPlugin {
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}
}
