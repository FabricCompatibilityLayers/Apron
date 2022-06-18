package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.util.hit.HitResult;

@Mixin(HitResult.class)
public class HitResultMixin {
	@Unique
	public int subHit = -1;
}
