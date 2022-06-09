package io.github.betterthanupdates.modloader.mixin;

import net.minecraft.packet.misc.DisconnectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DisconnectPacket.class)
public class DisconnectPacketMixin {
	@ModifyConstant(method = "read", constant = @Constant(intValue = 100))
	private int modloader$write(int value) {
		return 1000;
	}
}
