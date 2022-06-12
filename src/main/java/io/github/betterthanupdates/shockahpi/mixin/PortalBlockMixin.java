package io.github.betterthanupdates.shockahpi.mixin;

import io.github.betterthanupdates.shockahpi.block.ShockAhPIPortalBlock;
import net.minecraft.block.PortalBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PortalBlock.class)
public abstract class PortalBlockMixin implements ShockAhPIPortalBlock {
	@Override
	public int getDimNumber() {
		return -1;
	}
}
