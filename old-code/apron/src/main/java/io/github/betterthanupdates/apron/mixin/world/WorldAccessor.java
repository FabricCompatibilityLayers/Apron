package io.github.betterthanupdates.apron.mixin.world;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.World;

@Mixin(World.class)
public interface WorldAccessor {
	@Accessor
	void setField_212(int interval);
}
