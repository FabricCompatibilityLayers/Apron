package io.github.betterthanupdates.apron.stapi.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.stapi.StAPIBlock;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.ModContents;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationFlatteningBlock;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.block.BlockTemplate;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin implements StAPIBlock, StationFlatteningBlock, ForgeBlock {
	@Shadow
	@Final
	public int id;

	@Shadow
	public abstract float getHardness();

	@Unique
	private static int currentId = -1;

	@ModifyVariable(method = "<init>(ILnet/minecraft/block/Material;)V", argsOnly = true, at = @At(value = "HEAD"))
	private static int modifyItemId(int i) {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();
			currentId = i;

			return currentMod.BLOCKS.registerId(currentId,
					BlockTemplate::getNextId);
		}

		return i;
	}

	@Inject(method = "<init>(ILnet/minecraft/block/Material;)V", at = @At("RETURN"))
	private void registerItemInstance(int arg, Material par2, CallbackInfo ci) {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();

			currentMod.BLOCKS.registerLate(currentId, this.id, (Block)(Object) this);

			currentId = -1;
		}
	}

	@Override
	public int getOriginalBlockId() {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();

			return currentMod.BLOCKS.autoToOriginal.get(this.id);
		}

		return -1;
	}

	@WrapOperation(method = "dropStacks(Lnet/minecraft/world/World;IIIIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDroppedItemId(ILjava/util/Random;)I"))
	private int fixBlockDrop(Block instance, int i, Random random, Operation<Integer> original) {
		int originalId = this.id;

		try {
			originalId = original.call(instance, i, random);
		} catch (NullPointerException ignored) {}

		if (originalId > 0) {
			Optional<Identifier> dropBlockId = BlockRegistry.INSTANCE.getId(originalId);
			Optional<Identifier> dropItemId = ItemRegistry.INSTANCE.getId(originalId);

			if (!dropItemId.isEmpty()) return originalId;

			if (dropBlockId.isEmpty()) {
				throw new RuntimeException("Unknown block and item id for drop: " + originalId + " block id: " + instance.id);
			}

			Identifier dropId = dropBlockId.get();

			Item item = ItemRegistry.INSTANCE.get(dropId);

			if (item == null) {
				throw new RuntimeException("No item associated with identifier: " + dropId + " block id: " + instance.id);
			}

			return item.id;
		}

		return originalId;
	}

	@Override
	public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		float hardness = this.blockStrength((World) world, player, pos.x, pos.y, pos.z);

		float stAPIhardness = this.getHardness(state, world, pos);
		float vanillaHarness = this.getHardness();

		if (stAPIhardness != vanillaHarness) {
			hardness = Math.max(hardness, stAPIhardness);
		}

		if (hardness < 0.0f) return 0.0f;
		if (!player.canHarvest(world, pos, state)) return 1.0f / hardness / 100.0f;
		return player.getBlockBreakingSpeed(world, pos, state) / hardness / 30.0f;
	}
}
