package io.github.betterthanupdates.shockahpi.mixin;

import io.github.betterthanupdates.shockahpi.client.entity.player.SapiClientPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientPlayPacketHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.PlayerBase;
import shockahpi.SAPI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements SapiClientPlayerEntity {
	public ArrayList<PlayerBase> playerBases;
	private final SAPI sapi = new SAPI();
	public int portal;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void shockahpi$ctor(Minecraft client, World world, Session session,
	                            ClientPlayPacketHandler handler, CallbackInfo ci) {
		this.portal = -1;

		this.playerBases = this.sapi.PAPIplayerInit(((ClientPlayerEntity)(Object)this));
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void shockahpi$damage(Entity attacker, int amount, CallbackInfoReturnable<Boolean> cir) {
		this.sapi.PAPIattackEntityFrom(((ClientPlayerEntity)(Object)this), attacker, amount);
	}

	@Override
	public List<PlayerBase> getPlayerBases() {
		return playerBases;
	}
}
