package io.github.betterthanupdates.apron.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public interface EntityAccessor {
	@Accessor
	void setField_1636(float distance);
}
