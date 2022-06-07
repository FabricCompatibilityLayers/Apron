package modloader;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

public abstract class BaseMod {

	public BaseMod() {}

	/**
	 * Used for adding new sources of fuel to the furnace.
	 * @param id
	 * @return
	 */
	public int AddFuel(int id) {
		return 0;
	}
	
	/**
	 * Used to add entity renderers.
	 * @param rendererMap
	 */
	public void AddRenderer(Map<Class<? extends Entity>, EntityRenderer> rendererMap) {}
	
	/**
	 * Dispenses the entity associated with the selected item.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param xVel
	 * @param zVel
	 * @param item
	 * @return
	 */
	public boolean DispenseEntity(World world, double x, double y, double z, int xVel, int zVel, ItemStack item) {
		return false;
	}
	
	/**
	 * Used for generating new blocks (veins) in Nether.
	 * @param world
	 * @param random
	 * @param chunkX
	 * @param chunkZ
	 */
	public void GenerateNether(World world, Random random, int chunkX, int chunkZ) {}
	
	/**
	 * Used for generating new blocks (veins) on the surface world.
	 * @param world
	 * @param random
	 * @param chunkX
	 * @param chunkZ
	 */
	public void GenerateSurface(World world, Random random, int chunkX, int chunkZ) {}
	
	/**
	 * This method will be called when the register key has been pressed, or held down.
	 * @param event
	 */
	public void KeyboardEvent(KeyBinding event) {}
	
	/**
	 * Called after all mods are loaded.
	 */
	public void ModsLoaded() {}
	
	/**
	 * Called when enabled, and in game.
	 * @param game
	 * @return
	 */
	public boolean OnTickInGame(Minecraft game) {
		return false;
	}
	
	/**
	 * Called when enabled, and a GUI is open.
	 * @param game
	 * @param gui
	 * @return
	 */
	public boolean OnTickInGUI(Minecraft game, Screen gui) {
		return false;
	}
	
	/**
	 * Used for registering animations for items and blocks.
	 * @param game
	 */
	public void RegisterAnimation(Minecraft game) {}
	
	/**
	 * Renders a block in inventory.
	 * @param renderer
	 * @param block
	 * @param metadata
	 * @param modelID
	 */
	public void RenderInvBlock(BlockRenderer renderer, Block block, int metadata, int modelID) {}
	
	/**
	 * Renders a block in the world.
	 * @param renderer
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param block
	 * @param modelID
	 * @return
	 */
	public boolean RenderWorldBlock(BlockRenderer renderer, BlockView world, int x, int y, int z, Block block, int modelID) {
		return false;
	}
	
	/**
	 * Is called when an item is picked up from crafting result slot.
	 * @param player
	 * @param item
	 */
	public void TakenFromCrafting(PlayerEntity player, ItemStack item) {}
	
	/**
	 * Is called when an item is picked up from furnace result slot.
	 * @param player
	 * @param item
	 */
	public void TakenFromFurnace(PlayerEntity player, ItemStack item) {}
	
	/**
	 * Is called when an item is picked up from the world.
	 * @param player
	 * @param item
	 */
	public void OnItemPickup(PlayerEntity player, ItemStack item) {}
	
	@Override
	public String toString() {
		return this.getClass().getName() + " " + this.Version();
	}
	
	/**
	 * Required override that informs users the version of this mod.
	 * @return
	 */
	public abstract String Version();
}
