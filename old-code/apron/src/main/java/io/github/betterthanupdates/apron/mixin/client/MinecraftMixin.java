package io.github.betterthanupdates.apron.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.Minecraft;

import io.github.betterthanupdates.stapi.StAPIMinecraft;

@Mixin(Minecraft.class)
public class MinecraftMixin implements StAPIMinecraft {
}
