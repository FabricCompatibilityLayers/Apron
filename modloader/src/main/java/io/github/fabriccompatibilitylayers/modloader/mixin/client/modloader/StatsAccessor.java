package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Stats.class)
public interface StatsAccessor {
	@Accessor("ID_TO_STAT")
	static Map getIdToStatMap() {
		throw new AssertionError();
	}
}
