package io.github.betterthanupdates.shockahpi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shockahpi.SAPI;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;

@Mixin(SingleplayerInteractionManager.class)
public class SingleplayerInteractionManagerMixin extends ClientInteractionManager {
	public SingleplayerInteractionManagerMixin(Minecraft minecraft) {
		super(minecraft);
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public float getBlockReachDistance() {
		return SAPI.reachGetBlock();
	}
}
