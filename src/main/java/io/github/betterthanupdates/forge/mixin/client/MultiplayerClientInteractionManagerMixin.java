package io.github.betterthanupdates.forge.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerClientInteractionManager;
import net.minecraft.network.ClientPlayPacketHandler;
import net.minecraft.packet.play.PlayerDiggingC2SPacket;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Environment(EnvType.CLIENT)
@Mixin(MultiplayerClientInteractionManager.class)
public abstract class MultiplayerClientInteractionManagerMixin extends ClientInteractionManager {
	@Shadow
	private boolean field_2615;

	@Shadow
	private int field_2608;

	@Shadow
	private int field_2609;

	@Shadow
	private int field_2610;

	@Shadow
	private ClientPlayPacketHandler networkHandler;

	@Shadow
	private float field_2611;

	@Shadow
	private float field_2612;

	@Shadow
	private float field_2613;

	@Shadow
	protected abstract void method_1997();

	@Shadow
	private int field_2614;

	public MultiplayerClientInteractionManagerMixin(Minecraft minecraft) {
		super(minecraft);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_1707(int i, int j, int k, int l) {
		if (!this.field_2615 || i != this.field_2608 || j != this.field_2609 || k != this.field_2610) {
			this.networkHandler.sendPacket(new PlayerDiggingC2SPacket(0, i, j, k, l));
			int i1 = this.client.world.getBlockId(i, j, k);

			if (i1 > 0 && this.field_2611 == 0.0F) {
				Block.BY_ID[i1].activate(this.client.world, i, j, k, this.client.player);
			}

			if (i1 > 0 && ((ForgeBlock) Block.BY_ID[i1]).blockStrength(this.client.world, this.client.player, i, j, k) >= 1.0F) {
				this.method_1716(i, j, k, l);
			} else {
				this.field_2615 = true;
				this.field_2608 = i;
				this.field_2609 = j;
				this.field_2610 = k;
				this.field_2611 = 0.0F;
				this.field_2612 = 0.0F;
				this.field_2613 = 0.0F;
			}
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_1721(int i, int j, int k, int l) {
		if (this.field_2615) {
			this.method_1997();

			if (this.field_2614 > 0) {
				--this.field_2614;
			} else {
				if (i == this.field_2608 && j == this.field_2609 && k == this.field_2610) {
					int i1 = this.client.world.getBlockId(i, j, k);

					if (i1 == 0) {
						this.field_2615 = false;
						return;
					}

					Block block = Block.BY_ID[i1];
					this.field_2611 += ((ForgeBlock) block).blockStrength(this.client.world, this.client.player, i, j, k);

					if (this.field_2613 % 4.0F == 0.0F && block != null) {
						this.client.soundHelper.playSound(
								block.sounds.getWalkSound(),
								(float) i + 0.5F,
								(float) j + 0.5F,
								(float) k + 0.5F,
								(block.sounds.getVolume() + 1.0F) / 8.0F,
								block.sounds.getPitch() * 0.5F
						);
					}

					++this.field_2613;

					if (this.field_2611 >= 1.0F) {
						this.field_2615 = false;
						this.networkHandler.sendPacket(new PlayerDiggingC2SPacket(2, i, j, k, l));
						this.method_1716(i, j, k, l);
						this.field_2611 = 0.0F;
						this.field_2612 = 0.0F;
						this.field_2613 = 0.0F;
						this.field_2614 = 5;
					}
				} else {
					this.method_1707(i, j, k, l);
				}
			}
		}
	}
}
