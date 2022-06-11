package io.github.betterthanupdates.modloader.mixin;

import net.minecraft.packet.misc.DisconnectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DisconnectPacket.class)
public abstract class DisconnectPacketMixin {
	/**
	 * @author Risugami
	 * @reason ModLoaderMP may expect longer strings.
	 */
	@ModifyConstant(method = "read", constant = @Constant(intValue = 100))
	private int modloader$write(int value) {
		return 1000;
	}
}
