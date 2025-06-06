package modloader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

public abstract class BaseMod {
	public int AddFuel(int id) {
		return 0;
	}

	@Environment(EnvType.CLIENT)
	public void AddRenderer(Map<Class<? extends Entity>, EntityRenderer> renderers) {
	}

	public boolean DispenseEntity(World world, double x, double y, double z, int xVel, int zVel, ItemStack item) {
		return false;
	}

	public void GenerateNether(World world, Random random, int chunkX, int chunkZ) {
	}

	public void GenerateSurface(World world, Random random, int chunkX, int chunkZ) {
	}

	@Environment(EnvType.CLIENT)
	public void KeyboardEvent(KeyBinding event) {
	}

	@Environment(EnvType.SERVER)
	public void OnTickInGame(MinecraftServer minecraftserver) {
	}

	public void ModsLoaded() {
	}

	@Environment(EnvType.CLIENT)
	public boolean OnTickInGame(Minecraft game) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public boolean OnTickInGUI(Minecraft game, Screen gui) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public void RegisterAnimation(Minecraft game) {
	}

	@Environment(EnvType.CLIENT)
	public void RenderInvBlock(BlockRenderManager renderer, Block block, int metadata, int modelID) {
	}

	@Environment(EnvType.CLIENT)
	public boolean RenderWorldBlock(BlockRenderManager renderer, BlockView world, int x, int y, int z, Block block, int modelID) {
		return false;
	}

	public void TakenFromCrafting(PlayerEntity player, ItemStack item) {
	}

	public void TakenFromFurnace(PlayerEntity player, ItemStack item) {
	}

	public void OnItemPickup(PlayerEntity player, ItemStack item) {
	}

	public String toString() {
		return this.getClass().getName() + " " + this.Version();
	}

	public abstract String Version();
}
