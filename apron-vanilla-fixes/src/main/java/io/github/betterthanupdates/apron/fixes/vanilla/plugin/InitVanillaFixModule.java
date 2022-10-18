package io.github.betterthanupdates.apron.fixes.vanilla.plugin;

import fr.catcore.modremapperapi.ModRemappingAPI;
import org.spongepowered.asm.mixin.Mixins;

public class InitVanillaFixModule implements Runnable {
	@Override
	public void run() {
		ModRemappingAPI.init();

		Mixins.addConfiguration("apron-vanilla-fixes.compat.mixins.json");
	}
}
