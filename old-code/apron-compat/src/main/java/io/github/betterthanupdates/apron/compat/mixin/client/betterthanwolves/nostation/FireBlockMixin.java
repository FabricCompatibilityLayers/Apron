package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves.nostation;

import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FireBlock.class)
public class FireBlockMixin {
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 256, ordinal = 0))
	private int btw$changeTo1024_1(int constant) {
		return 1024;
	}

	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 256, ordinal = 1))
	private int btw$changeTo1024_2(int constant) {
		return 1024;
	}
}
