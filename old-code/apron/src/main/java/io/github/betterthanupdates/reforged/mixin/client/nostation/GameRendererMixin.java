package io.github.betterthanupdates.reforged.mixin.client.nostation;

import net.minecraft.class_555;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import reforged.Reforged;

import net.minecraft.client.Minecraft;

@Mixin(class_555.class)
public class GameRendererMixin {
	@Shadow
	private Minecraft field_2349;

	/**
	 * @return Reforged-modified reach
	 * @author Meefy777
	 * @reason implement Reforged function
	 */
	@ModifyConstant(method = "method_1838", constant = @Constant(doubleValue = 3.0d))
	private double reforged$modifyReach(double constant) {
		return Reforged.reachGetEntityPlayer(this.field_2349.player);
	}
}
