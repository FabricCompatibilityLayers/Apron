package modloader;

import java.util.Map;
import java.util.Random;

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

import io.github.betterthanupdates.Legacy;

@SuppressWarnings({"unused", "SameReturnValue", "EmptyMethod"})
@Legacy
public abstract class BaseMod {
	/**
	 * Used for adding new sources of fuel to the furnace.
	 *
	 * @param id ItemID for the item to use as fuel
	 * @return Duration of fuel provided
	 */
	public int AddFuel(int id) {
		return 0;
	}

	/**
	 * Used to add entity renderers.
	 *
	 * @param renderers HashMap of the renderers. key is an entity class, value is the renderer.
	 */
	@Environment(EnvType.CLIENT)
	public void AddRenderer(Map<Class<? extends Entity>, EntityRenderer> renderers) {
	}

	/**
	 * Dispenses the entity associated with the selected item.
	 *
	 * @param world reference to the World
	 * @param x     X coordinate
	 * @param y     Y coordinate
	 * @param z     Z coordinate
	 * @param xVel  X velocity
	 * @param zVel  Z velocity
	 * @param item  ID of item to chosen to dispense entity
	 * @return true if item was handled
	 */
	public boolean DispenseEntity(World world, double x, double y, double z, int xVel, int zVel, ItemStack item) {
		return false;
	}

	/**
	 * Used for generating new blocks (veins) in Nether.
	 *
	 * @param world  Reference to world
	 * @param random Instance of random to use
	 * @param chunkX X coordinate of chunk
	 * @param chunkZ Z coordinate of chunk
	 */
	public void GenerateNether(World world, Random random, int chunkX, int chunkZ) {
	}

	/**
	 * Used for generating new blocks (veins) on the surface world.
	 *
	 * @param world  Reference to world.
	 * @param random Instance of random to use.
	 * @param chunkX X coordinate of chunk.
	 * @param chunkZ Z coordinate of chunk.
	 */
	public void GenerateSurface(World world, Random random, int chunkX, int chunkZ) {
	}

	/**
	 * This method will be called when the register key has been pressed, or held down.
	 *
	 * @param event Reference to the key pressed.
	 */
	@Environment(EnvType.CLIENT)
	public void KeyboardEvent(KeyBinding event) {
	}

	/**
	 * Called after all mods are loaded.
	 */
	public void ModsLoaded() {
	}

	/**
	 * Called when enabled, and in game.
	 *
	 * @param client Instance of the {@link Minecraft} class
	 * @return true to continue ticking, or false to stop ticking
	 */
	@Environment(EnvType.CLIENT)
	public boolean OnTickInGame(Minecraft client) {
		return false;
	}

	@Environment(EnvType.SERVER)
	public void OnTickInGame(MinecraftServer minecraftServer) {
	}

	/**
	 * Called when enabled, and a GUI is open.
	 *
	 * @param client Instance of the {@link Minecraft} class
	 * @param screen Current screen that is open
	 * @return true to continue ticking, or false to stop ticking
	 */
	@Environment(EnvType.CLIENT)
	public boolean OnTickInGUI(Minecraft client, Screen screen) {
		return false;
	}

	/**
	 * Used for registering animations for items and blocks.
	 *
	 * @param client Instance of the {@link Minecraft} class.
	 */
	@Environment(EnvType.CLIENT)
	public void RegisterAnimation(Minecraft client) {
	}

	/**
	 * Renders a block in inventory.
	 *
	 * @param renderer parent renderer. Methods and fields may be referenced from here
	 * @param block    reference to block to render
	 * @param metadata of block. Damage on an item
	 * @param modelID  ID of block model to render
	 */
	@Environment(EnvType.CLIENT)
	public void RenderInvBlock(BlockRenderManager renderer, Block block, int metadata, int modelID) {
	}

	/**
	 * Renders a block in the world.
	 *
	 * @param renderer parent renderer. Methods and fields may be referenced from here
	 * @param world    to render block in
	 * @param x        X Position
	 * @param y        Y Position
	 * @param z        Z Position
	 * @param block    reference to block to render
	 * @param modelID  ID of block model to render
	 * @return true if model was rendered.
	 */
	@Environment(EnvType.CLIENT)
	public boolean RenderWorldBlock(BlockRenderManager renderer, BlockView world, int x, int y, int z, Block block, int modelID) {
		return false;
	}

	/**
	 * Is called when an item is picked up from crafting result slot.
	 *
	 * @param player that crafted the item
	 * @param item   that was crafted
	 */
	public void TakenFromCrafting(PlayerEntity player, ItemStack item) {
	}

	/**
	 * Is called when an item is picked up from furnace result slot.
	 *
	 * @param player that took the item
	 * @param item   that was taken
	 */
	public void TakenFromFurnace(PlayerEntity player, ItemStack item) {
	}

	/**
	 * Is called when an item is picked up from the world.
	 *
	 * @param player that picked up the item
	 * @param item   that was picked up
	 */
	public void OnItemPickup(PlayerEntity player, ItemStack item) {
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " " + this.Version();
	}

	/**
	 * Required override that informs users the version of this mod.
	 *
	 * @return Version string
	 */
	public abstract String Version();

	public final String getClassName() {
		return this.getClass().getSimpleName();
	}
}
