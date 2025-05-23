package io.github.betterthanupdates.forge.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.minecraft.block.Block;
import net.minecraft.class_417;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_417.class)
public class class_417Mixin {
	@ModifyVariable(
			method = {"method_1402"},
			at = @At(
					value = "STORE",
					ordinal = 2
			),
			index = 20
	)
	private int getStateLuminance(int original, @Local World world, @Local(index = 10) int x, @Local(index = 15) int y, @Local(index = 11) int z) {
		int blockId = world.getBlockId(x, y, z);
		return blockId == 0 ? 0 : ((ForgeBlock) Block.BLOCKS[blockId]).getLightValue(world, x, y, z);
	}
}
