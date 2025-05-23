package io.github.betterthanupdates.apron.compat.mixin.client.incrediblefungus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import net.minecraft.block.Material;
import net.minecraft.mod_Fungus;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends Block {
	@Shadow
	protected abstract void method_1822(int i, int j, int k);

	protected FireBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	@Inject(method = "init", at = @At("RETURN"))
	private void incrediblefungus$init(CallbackInfo ci) {
		this.method_1822(mod_Fungus.fungus.id, 300, 300);
	}
}
