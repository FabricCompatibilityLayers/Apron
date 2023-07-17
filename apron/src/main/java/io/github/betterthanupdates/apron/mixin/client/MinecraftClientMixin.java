package io.github.betterthanupdates.apron.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.Minecraft;

import io.github.betterthanupdates.stapi.StAPIMinecraftClient;

@Mixin(Minecraft.class)
public class MinecraftClientMixin implements StAPIMinecraftClient {
}
