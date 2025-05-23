package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import java.util.List;

import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.cursedmixinextensions.annotations.Public;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.mod_FCBetterThanWolves;
import net.minecraft.world.World;

@Mixin(DetectorRailBlock.class)
public class DetectorRailBlockMixin extends RailBlock {
	protected DetectorRailBlockMixin(int i, int j, boolean bl) {
		super(i, j, bl);
	}

	@Redirect(method = "method_1144", at = @At(value = "INVOKE", remap = false, target = "Ljava/util/List;size()I", ordinal = 0))
	private int btw$nullCheck(List list) {
		return -1;
	}

	@ModifyVariable(method = "method_1144", ordinal = 5, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;method_175(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
	private int btw$method_1144(int value, @Local List list, @Local World world, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		for(int listIndex = 0; listIndex < list.size(); ++listIndex) {
			MinecartEntity minecartEntity = (MinecartEntity)list.get(listIndex);
			if (ShouldPlateActivateBasedOnMinecart(world, i, j, k, minecartEntity.field_2275, minecartEntity.field_1594)) {
				value = 1;
				break;
			}
		}

		return value;
	}

	@Public
	private static boolean ShouldPlateActivateBasedOnMinecart(World world, int i, int j, int k, int iMinecartType, Entity riddenByEntity) {
		int iLocalBlockID = world.getBlockId(i, j, k);
		if (iLocalBlockID == mod_FCBetterThanWolves.fcDetectorRailWood.id) {
			return true;
		} else {
			if (iLocalBlockID == mod_FCBetterThanWolves.fcDetectorRailObsidian.id) {
				if (riddenByEntity != null && riddenByEntity instanceof PlayerEntity) {
					return true;
				}
			} else if (iLocalBlockID == Block.DETECTOR_RAIL.id && (iMinecartType > 0 || riddenByEntity != null)) {
				return true;
			}

			return false;
		}
	}
}
