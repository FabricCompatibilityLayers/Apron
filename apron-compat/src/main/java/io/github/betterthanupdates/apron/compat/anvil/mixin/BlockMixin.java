package io.github.betterthanupdates.apron.compat.anvil.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
	@Mutable
	@Shadow
	@Final
	public static Block[] BY_ID;

	@Mutable
	@Shadow
	@Final
	public static boolean[] TICKS_RANDOMLY;

	@Mutable
	@Shadow
	@Final
	public static boolean[] FULL_OPAQUE;

	@Mutable
	@Shadow
	@Final
	public static boolean[] HAS_BLOCK_ENTITY;

	@Mutable
	@Shadow
	@Final
	public static int[] LIGHT_OPACITY;

	@Mutable
	@Shadow
	@Final
	public static boolean[] ALLOWS_GRASS_UNDER;

	@Mutable
	@Shadow
	@Final
	public static int[] EMITTANCE;

	@Mutable
	@Shadow
	@Final
	public static boolean[] NO_NOTIFY_ON_META_CHANGE;

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void increaseIDs(CallbackInfo callbackInfo) {
		BY_ID = new Block[4096];
		TICKS_RANDOMLY = new boolean[4096];
		FULL_OPAQUE = new boolean[4096];
		HAS_BLOCK_ENTITY = new boolean[4096];
		LIGHT_OPACITY = new int[4096];
		ALLOWS_GRASS_UNDER = new boolean[4096];
		EMITTANCE = new int[4096];
		NO_NOTIFY_ON_META_CHANGE = new boolean[4096];
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 256))
	private static int increaseIDs(int value) {
		return 4096;
	}

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;", shift = At.Shift.BEFORE))
	private static void addNullCheck(CallbackInfo callbackInfo, @Local int var0) {
		if (BY_ID[var0] == null) {
		}
	}
}
