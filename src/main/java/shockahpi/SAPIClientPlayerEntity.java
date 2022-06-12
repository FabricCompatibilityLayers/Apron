package shockahpi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SAPIClientPlayerEntity extends AbstractClientPlayerEntity {
	private static final SAPI sapi = new SAPI();
	public ArrayList<PlayerBase> playerBases = sapi.PAPIplayerInit(this);
	public int portal = -1;

	public SAPIClientPlayerEntity(Minecraft client, World world, Session session, int dimension) {
		super(client, world, session, dimension);
	}
}
