package io.github.betterthanupdates.shockahpi.mixin.client.block;

import net.minecraft.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import io.github.betterthanupdates.shockahpi.block.ShockAhPINetherPortalBlock;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin implements ShockAhPINetherPortalBlock {
	@Override
	public int getDimNumber() {
		return -1;
	}
}
