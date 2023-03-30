package io.github.betterthanupdates.apron.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.World;

@Mixin(World.class)
public interface WorldAccessor {
	@Accessor
	void setAutoSaveInterval(int interval);
}
