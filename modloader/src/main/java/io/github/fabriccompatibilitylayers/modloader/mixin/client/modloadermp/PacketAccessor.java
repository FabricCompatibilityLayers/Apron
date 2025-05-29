package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloadermp;

import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Packet.class)
public interface PacketAccessor {
	@Invoker("register")
	static void invokeRegister(int rawId, boolean clientBound, boolean serverBound, Class type) {
		throw new AssertionError();
	}
}
