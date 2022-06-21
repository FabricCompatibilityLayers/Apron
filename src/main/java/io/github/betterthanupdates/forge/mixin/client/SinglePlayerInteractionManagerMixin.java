package io.github.betterthanupdates.forge.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Environment(EnvType.CLIENT)
@Mixin(SingleplayerInteractionManager.class)
public class SinglePlayerInteractionManagerMixin extends ClientInteractionManager {
	@Shadow
	private float damage;

	@Shadow
	private int hitDelay;

	@Shadow
	private int blockX;

	@Shadow
	private int blockY;

	@Shadow
	private int blockZ;

	@Shadow
	private float field_2186;

	@Shadow
	private float oldDamage;

	public SinglePlayerInteractionManagerMixin(Minecraft client) {
		super(client);
	}

	@Unique
	int cachedMeta = 0;
	@Inject(method = "method_1716", at = @At("HEAD"))
	private void forge$method_1716(int j, int k, int l, int par4, CallbackInfoReturnable<Boolean> cir) {
		this.cachedMeta = this.client.world.getBlockMeta(j, k, l);
	}

	@Redirect(method = "method_1716", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;canRemoveBlock(Lnet/minecraft/block/Block;)Z"))
	private boolean forge$method_1716(AbstractClientPlayerEntity instance, Block block) {
		return ((ForgeBlock) block).canHarvestBlock(instance, this.cachedMeta);
	}

	/**
	 * @author Forge
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void method_1707(int x, int y, int z, int l) {
		this.client.world.method_172(this.client.player, x, y, z, l);
		int i1 = this.client.world.getBlockId(x, y, z);

		if (i1 > 0 && this.damage == 0.0F) {
			Block.BY_ID[i1].activate(this.client.world, x, y, z, this.client.player);
		}

		if (i1 > 0 && ((ForgeBlock) Block.BY_ID[i1]).blockStrength(this.client.world, this.client.player, x, y, z) >= 1.0F) {
			this.method_1716(x, y, z, l);
		}
	}

	/**
	 * @author Forge
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void method_1721(int i, int j, int k, int l) {
		if (this.hitDelay > 0) {
			--this.hitDelay;
		} else {
			if (i == this.blockX && j == this.blockY && k == this.blockZ) {
				int i1 = this.client.world.getBlockId(i, j, k);

				if (i1 == 0) {
					return;
				}

				Block block = Block.BY_ID[i1];
				this.damage += ((ForgeBlock) block).blockStrength(this.client.world, this.client.player, i, j, k);

				if (this.field_2186 % 4.0F == 0.0F) {
					this.client.soundHelper.playSound(
							block.sounds.getWalkSound(),
							(float) i + 0.5F,
							(float) j + 0.5F,
							(float) k + 0.5F,
							(block.sounds.getVolume() + 1.0F) / 8.0F,
							block.sounds.getPitch() * 0.5F
					);
				}

				++this.field_2186;

				if (this.damage >= 1.0F) {
					this.method_1716(i, j, k, l);
					this.damage = 0.0F;
					this.oldDamage = 0.0F;
					this.field_2186 = 0.0F;
					this.hitDelay = 5;
				}
			} else {
				this.damage = 0.0F;
				this.oldDamage = 0.0F;
				this.field_2186 = 0.0F;
				this.blockX = i;
				this.blockY = j;
				this.blockZ = k;
			}
		}
	}
}
