package io.github.betterthanupdates.apron.compat.mixin.betterthanwolves;

import net.minecraft.FCBlockCompanionCube;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FCBlockCompanionCube.class)
public interface FCBlockCompanionCubeMixin {
	@Invoker("SpawnHearts")
	public static void SpawnHearts(World world, int i, int j, int k) {
		throw new AssertionError();
	}
}
