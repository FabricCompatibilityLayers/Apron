package modloader;

import static io.github.betterthanupdates.apron.Apron.BUILTIN_RML_MODS;
import static io.github.betterthanupdates.apron.Apron.MOD_CACHE_FOLDER;
import static io.github.betterthanupdates.apron.LifecycleUtils.CURRENT_MOD;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import io.github.betterthanupdates.apron.LifecycleUtils;
import io.github.betterthanupdates.apron.ReflectionUtils;
import io.github.betterthanupdates.stapi.StAPIBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.api.FabricLoader;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.minecraft.class_238;
import net.minecraft.class_288;
import net.minecraft.class_303;
import net.minecraft.class_336;
import net.minecraft.class_359;
import net.minecraft.class_447;
import net.minecraft.class_51;
import net.minecraft.class_538;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stat.ItemOrBlockStat;
import net.minecraft.stat.Stats;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.Apron;
import io.github.betterthanupdates.apron.api.ApronApi;
import io.github.betterthanupdates.stapi.StAPIMinecraft;

@SuppressWarnings("unused")
@Legacy
public class ModLoader {
	// Apron
	static final ApronApi APRON = ApronApi.getInstance();

	@Environment(EnvType.CLIENT)
	private static List<class_336> ANIM_LIST;
	@Environment(EnvType.CLIENT)
	private static Map<Integer, BaseMod> BLOCK_MODELS;
	@Environment(EnvType.CLIENT)
	private static Map<Integer, Boolean> BLOCK_SPECIAL_INV;
	private static final File CONFIG_DIR = FabricLoader.getInstance().getConfigDir().toFile();
	private static final File CONFIG_FILE = new File(CONFIG_DIR, "ModLoader.cfg");
	public static Level cfgLoggingLevel = Level.FINER;
	private static long clock = 0L;
	private static Field field_modifiers = null;
	private static boolean hasInit = false;
	private static int highestEntityId = 3000;
	private static final Map<BaseMod, Boolean> inGameHooks = new HashMap<>();
	@Environment(EnvType.CLIENT)
	private static Map<BaseMod, Boolean> inGUIHooks;
	private static int itemSpriteIndex = 0;
	private static int itemSpritesLeft = 0;
	@Environment(EnvType.CLIENT)
	private static Map<BaseMod, Map<KeyBinding, boolean[]>> keyList;
	private static final File LOG_FILE = new File(FabricLoader.getInstance().getGameDir().toFile(), "ModLoader.txt");
	private static final java.util.logging.Logger MOD_LOGGER = java.util.logging.Logger.getLogger("ModLoader");
	public static final Logger LOGGER = Apron.getLogger("ModLoader");
	private static FileHandler logHandler = null;
	private static final LinkedList<BaseMod> MOD_LIST = new LinkedList<>();
	private static int nextBlockModelID = 1000;
	private static final Map<Integer, Map<String, Integer>> overrides = new HashMap<>();
	public static final Properties props = new Properties();
	private static int terrainSpriteIndex = 0;
	private static int terrainSpritesLeft = 0;
	@Environment(EnvType.CLIENT)
	private static String texPack;
	@Environment(EnvType.CLIENT)
	private static boolean texturesAdded;
	private static final boolean[] USED_ITEM_SPRITES = new boolean[256];
	private static final boolean[] USED_TERRAIN_SPRITES = new boolean[256];
	public static final String VERSION = APRON.getModLoaderVersion();

	static {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ANIM_LIST = new LinkedList<>();
			BLOCK_MODELS = new HashMap<>();
			BLOCK_SPECIAL_INV = new HashMap<>();
			inGUIHooks = new HashMap<>();
			keyList = new HashMap<>();
			texPack = null;
			texturesAdded = false;
		}
	}

	/**
	 * Used to give your achievement a readable name and description.
	 *
	 * @param achievement the entry to be described
	 * @param name        the name of the entry
	 * @param description the description of the entry
	 */
	@SuppressWarnings("unused")
	public static void AddAchievementDesc(Achievement achievement, String name, String description) {
		try {
			if (achievement.stringId.contains(".")) {
				String[] split = achievement.stringId.split("\\.");

				if (split.length == 2) {
					String key = split[1];

					if (APRON.isClient()) {
						AddLocalization("achievement." + key, name);
						AddLocalization("achievement." + key + ".desc", description);
					}

					achievement.stringId = APRON.translate("achievement." + key);
					achievement.translationKey = APRON.translate("achievement." + key + ".desc");
				} else {
					achievement.stringId = name;
					achievement.translationKey = description;
				}
			} else {
				achievement.stringId = name;
				achievement.translationKey = description;
			}
		} catch (IllegalArgumentException | SecurityException e) {
			MOD_LOGGER.throwing("ModLoader", "AddAchievementDesc", e);
			ThrowException(e);
		}
	}

	/**
	 * Used for adding new sources of fuel to the furnace.
	 *
	 * @param id the item to be used as fuel.
	 * @return the fuel ID assigned to the item.
	 */
	public static int AddAllFuel(int id) {
		LOGGER.debug("Finding fuel for " + id);
		int result = 0;
		Iterator<BaseMod> iter = MOD_LIST.iterator();

		while (iter.hasNext() && result == 0) {
			result = iter.next().AddFuel(id);
		}

		if (result != 0) {
			LOGGER.debug("Returned " + result);
		}

		return result;
	}

	/**
	 * Used to add all mod entity renderers.
	 *
	 * @param rendererMap renderers to add
	 */
	@Environment(EnvType.CLIENT)
	public static void AddAllRenderers(Map<Class<? extends Entity>, EntityRenderer> rendererMap) {
		init();

		for (BaseMod mod : MOD_LIST) {
			CURRENT_MOD = mod.getClassName();
			mod.AddRenderer(rendererMap);
		}

		CURRENT_MOD = null;
		LifecycleUtils.triggerModsAllLoadedEvent();
	}

	/**
	 * Registers one animation instance.
	 *
	 * @param textureBinder animation instance to register
	 */
	@Environment(EnvType.CLIENT)
	public static void addAnimation(class_336 textureBinder) {
		LOGGER.debug("Adding animation " + textureBinder.toString());

		for (class_336 oldAnim : ANIM_LIST) {
			if (oldAnim.field_1416 == textureBinder.field_1416 && oldAnim.field_1412 == textureBinder.field_1412) {
				ANIM_LIST.remove(textureBinder);
				break;
			}
		}

		ANIM_LIST.add(textureBinder);
	}

	/**
	 * Use this when you need the player to have new armor skin.
	 *
	 * @param armor Name of the armor skin
	 * @return index assign for the armor skin
	 */
	@SuppressWarnings("unused")
	public static int AddArmor(String armor) {
		if (APRON.isClient()) {
			try {
				String[] existingArmor = PlayerEntityRenderer.armorTextureNames;
				List<String> existingArmorList = Arrays.asList(existingArmor);
				List<String> combinedList = new ArrayList<>(existingArmorList);

				if (!combinedList.contains(armor)) {
					combinedList.add(armor);
				}

				int index = combinedList.indexOf(armor);
				PlayerEntityRenderer.armorTextureNames = combinedList.toArray(new String[0]);
				return index;
			} catch (IllegalArgumentException e) {
				MOD_LOGGER.throwing("ModLoader", "AddArmor", e);
				ThrowException("An impossible error has occured!", e);
			}
		}

		return -1;
	}

	/**
	 * Method for adding raw strings to the translation table.
	 *
	 * @param key   tag for string
	 * @param value string to add
	 */
	@Environment(EnvType.CLIENT)
	public static void AddLocalization(String key, String value) {
		LifecycleUtils.CACHED_TRANSLATIONS.put(key, value);
		Properties props = TranslationStorage.getInstance().translations;

		if (props != null) {
			props.put(key, value);
		}
	}

	@ApiStatus.Internal
	public static void addInternalMod(ClassLoader loader, String filename) {
		try {
			String name = filename.replace("/", ".").replace("\\", ".").replace(".class", "");

			if (name.contains("$")) {
				return;
			}

			String modName = Apron.getOriginalClassName(name);

			if (props.containsKey(modName) && (props.getProperty(modName).equalsIgnoreCase("no") || props.getProperty(modName).equalsIgnoreCase("off"))) {
				return;
			}

			CURRENT_MOD = modName;

			Class<?> modClass = loader.loadClass(name);

			if (!BaseMod.class.isAssignableFrom(modClass)) return;

			//noinspection unchecked
			setupProperties((Class<? extends BaseMod>) modClass);
			Constructor<?> modConstructor = modClass.getDeclaredConstructor();
			modConstructor.setAccessible(true);
			BaseMod mod = (BaseMod) modConstructor.newInstance();
			MOD_LIST.add(mod);
			LOGGER.debug("Mod Loaded: \"" + mod + "\" from " + filename);
		} catch (Throwable e) {
			LOGGER.debug("Failed to load mod from \"" + filename + "\"");
			MOD_LOGGER.throwing("ModLoader", "addMod", e);
			ThrowException(e);
		}
	}

	private static void addInternalMod(BaseMod mod) {

		MOD_LIST.add(mod);
		LOGGER.info("Internal mod loaded: %s %s", mod.getClass().getSimpleName(), mod.Version());
	}

	/**
	 * This method will allow adding name to item in inventory.
	 *
	 * @param instance A block, item, or item stack reference to name
	 * @param name     The name to give
	 */
	@SuppressWarnings("unused")
	@Environment(EnvType.CLIENT)
	public static void AddName(Object instance, String name) {
		String tag = null;

		if (instance instanceof Item) {
			Item item = (Item) instance;

			if (item.getTranslationKey() != null) {
				tag = item.getTranslationKey() + ".name";
			}
		} else if (instance instanceof Block) {
			Block block = (Block) instance;

			if (block.getTranslationKey() != null) {
				tag = block.getTranslationKey() + ".name";
			}
		} else if (instance instanceof ItemStack) {
			ItemStack stack = (ItemStack) instance;

			if (stack.getTranslationKey() != null) {
				tag = stack.getTranslationKey() + ".name";
			}
		} else {
			Exception e = new Exception(instance.getClass().getName() + " cannot have name attached to it!");
			MOD_LOGGER.throwing("ModLoader", "AddName", e);
			ThrowException(e);
		}

		if (tag != null) {
			AddLocalization(tag, name);
		} else {
			Exception e = new Exception(instance + " is missing name tag!");
			MOD_LOGGER.throwing("ModLoader", "AddName", e);
			ThrowException(e);
		}
	}

	/**
	 * Use this to add custom images for your items and blocks.
	 *
	 * @param fileToOverride file to override ("/terrain.png" or "/gui/items.png")
	 * @param fileToAdd      path to the image you want to add
	 * @return unique sprite index
	 */
	@SuppressWarnings("unused")
	public static int addOverride(String fileToOverride, String fileToAdd) {
		try {
			if (FabricLoader.getInstance().isModLoaded("stationapi")) {
				return ((StAPIMinecraft) getMinecraftInstance())
						.apron$stapi$registerTextureOverride(fileToOverride, fileToAdd);
			} else {
				int i = getUniqueSpriteIndex(fileToOverride);
				addOverride(fileToOverride, fileToAdd, i);
				return i;
			}
		} catch (Throwable e) {
			MOD_LOGGER.throwing("ModLoader", "addOverride", e);
			ThrowException(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registers one texture override to be done.
	 *
	 * @param path        Path to the texture file to modify ("/terrain.png" or "/gui/items.png")
	 * @param overlayPath Path to the texture file which is to be overlaid
	 * @param index       Sprite index into the texture to be modified
	 */
	public static void addOverride(String path, String overlayPath, int index) {
		int dst;
		int left;

		if (path.equals("/terrain.png")) {
			dst = 0;
			left = terrainSpritesLeft;
		} else {
			if (!path.equals("/gui/items.png")) {
				return;
			}

			dst = 1;
			left = itemSpritesLeft;
		}

		LOGGER.debug("addOverride(" + path + "," + overlayPath + "," + index + "). " + left + " left.");
		Map<String, Integer> overlays = overrides.computeIfAbsent(dst, k -> new HashMap<>());

		overlays.put(overlayPath, index);
	}

	/**
	 * Add a shaped recipe to crafting list.
	 *
	 * @param output      the result of the crafting recipe
	 * @param ingredients the ingredients for the crafting recipe from top left to bottom right.
	 */
	@SuppressWarnings("unused")
	public static void AddRecipe(ItemStack output, Object... ingredients) {
		CraftingRecipeManager.getInstance().addShapedRecipe(output, ingredients);
	}

	/**
	 * Add recipe to crafting list.
	 *
	 * @param output      the result of the crafting recipe
	 * @param ingredients ingredients for the recipe in any order
	 */
	public static void AddShapelessRecipe(ItemStack output, Object... ingredients) {
		CraftingRecipeManager.getInstance().addShapelessRecipe(output, ingredients);
	}

	/**
	 * Used to add smelting recipes to the furnace.
	 *
	 * @param input  ingredient for the recipe
	 * @param output the result of the furnace recipe
	 */
	public static void AddSmelting(int input, ItemStack output) {
		SmeltingRecipeManager.getInstance().addRecipe(input, output);
	}

	/**
	 * Add entity to spawn list for all biomes except Hell.
	 *
	 * @param entityClass  entity to spawn
	 * @param weightedProb chance of spawning for every try
	 * @param spawnGroup   group to spawn the entity in
	 */
	public static void AddSpawn(Class<? extends LivingEntity> entityClass, int weightedProb, class_238 spawnGroup) {
		AddSpawn(entityClass, weightedProb, spawnGroup, (Biome[]) null);
	}

	/**
	 * Add entity to spawn list for selected biomes.
	 *
	 * @param entityClass  entity to spawn
	 * @param weightedProb chance of spawning for every try
	 * @param spawnGroup   group to spawn the entity in
	 * @param biomes       biomes to spawn the entity in
	 */
	@SuppressWarnings("unchecked")
	public static void AddSpawn(Class<? extends LivingEntity> entityClass, int weightedProb, class_238 spawnGroup, Biome... biomes) {
		if (entityClass == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if (spawnGroup == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if (biomes == null || biomes.length > 0 && biomes[0] == null) {
				biomes = APRON.getStandardBiomes();
			}

			for (Biome biome : biomes) {
				if (biome == null) {
					LOGGER.warn("Attempted to add entity %s with spawn group %s to a null biome", entityClass, spawnGroup);
					continue;
				}

				List<class_288> list = biome.method_789(spawnGroup);

				if (list != null) {
					boolean exists = false;

					for (class_288 entry : list) {
						if (entry.field_1142 == entityClass) {
							entry.field_1143 = weightedProb;
							exists = true;
							break;
						}
					}

					if (!exists) {
						list.add(new class_288(entityClass, weightedProb));
					}
				}
			}
		}
	}

	/**
	 * Add entity to spawn list for all biomes except Hell.
	 *
	 * @param entityName Name of entity to spawn
	 * @param chance     Higher number means more likely to spawn
	 * @param spawnGroup The spawn group to add entity to (Monster, Creature, or Water)
	 */
	public static void AddSpawn(String entityName, int chance, class_238 spawnGroup) {
		AddSpawn(entityName, chance, spawnGroup, (Biome[]) null);
	}

	/**
	 * Add entity to spawn list for selected biomes.
	 *
	 * @param entityName   Name of entity to spawn
	 * @param weightedProb Higher number means more likely to spawn
	 * @param spawnGroup   The spawn group to add entity to (Monster, Creature, or Water)
	 * @param biomes       Array of biomes to add entity spawning to
	 */
	@SuppressWarnings("unchecked")
	public static void AddSpawn(String entityName, int weightedProb, class_238 spawnGroup, Biome... biomes) {
		Class<? extends Entity> entityClass = (Class<? extends Entity>) EntityRegistry.idToClass.get(entityName);

		if (entityClass != null && LivingEntity.class.isAssignableFrom(entityClass)) {
			AddSpawn((Class<? extends LivingEntity>) entityClass, weightedProb, spawnGroup, biomes);
		}
	}

	/**
	 * Dispenses the entity associated with the selected item.
	 *
	 * @param world world to spawn in
	 * @param x     x position
	 * @param y     y position
	 * @param z     z position
	 * @param xVel  x velocity
	 * @param zVel  z velocity
	 * @param stack item inside the dispenser to dispense
	 * @return whether dispensing was successful
	 */
	public static boolean DispenseEntity(World world, double x, double y, double z, int xVel, int zVel, ItemStack stack) {
		boolean result = false;
		Iterator<BaseMod> iter = MOD_LIST.iterator();

		while (iter.hasNext() && !result) {
			result = iter.next().DispenseEntity(world, x, y, z, xVel, zVel, stack);
		}

		return result;
	}

	/**
	 * Use this method if you need a list of loaded mods.
	 *
	 * @return the list of loaded {@link BaseMod ModLoader mods}
	 */
	public static List<BaseMod> getLoadedMods() {
		return Collections.unmodifiableList(MOD_LIST);
	}

	/**
	 * Use this to get a reference to the logger ModLoader mods use.
	 *
	 * @return the logger instance
	 */
	public static java.util.logging.Logger getLogger() {
		return MOD_LOGGER;
	}

	/**
	 * Use this method to get a reference to Minecraft instance.
	 *
	 * @return Minecraft client instance
	 */
	@Nullable
	@Environment(EnvType.CLIENT)
	public static Minecraft getMinecraftInstance() {
		return (Minecraft) ApronApi.getInstance().getGame();
	}

	@Nullable
	@Environment(EnvType.SERVER)
	public static MinecraftServer getMinecraftServerInstance() {
		return (MinecraftServer) APRON.getGame();
	}

	/**
	 * Used for getting value of private fields.
	 *
	 * @param instanceClass Class to use with instance.
	 * @param instance      Object to get private field from.
	 * @param fieldIndex    Name of the field.
	 * @param <T>           Return type
	 * @param <E>           Type of instance
	 * @return Value of private field
	 * @throws IllegalArgumentException if instance isn't compatible with instanceClass
	 * @throws SecurityException        if the thread is not allowed to access field
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> T getPrivateValue(Class<? super E> instanceClass, E instance, int fieldIndex) throws IllegalArgumentException, SecurityException {
		try {
			Field f = instanceClass.getDeclaredFields()[fieldIndex];
			f.setAccessible(true);
			return (T) f.get(instance);
		} catch (IllegalAccessException e) {
			MOD_LOGGER.throwing("ModLoader", "getPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
			return null;
		}
	}

	/**
	 * Used for getting value of private fields.
	 *
	 * @param instanceClass Class to use with instance
	 * @param instance      Object to get private field from
	 * @param fieldName     Name of the field
	 * @param <T>           Return type
	 * @param <E>           Type of instance
	 * @return Value of private field
	 * @throws IllegalArgumentException if instance isn't compatible with instanceClass
	 * @throws SecurityException        if the thread is not allowed to access field
	 * @throws NoSuchFieldException     if field does not exist
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> T getPrivateValue(Class<? super E> instanceClass, E instance, String fieldName) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			T value = (T) ReflectionUtils.getField(instanceClass, instance, fieldName);

			if (value == null) throw new IllegalAccessException("Failed to get value of field " + fieldName + " of class " + instanceClass.getName() + " instance " + instance);

			return value;
		} catch (IllegalAccessException e) {
			MOD_LOGGER.throwing("ModLoader", "getPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
			return null;
		}
	}

	/**
	 * Assigns a model id for blocks to use for the given mod.
	 *
	 * @param mod        to assign id to
	 * @param full3DItem if true the item will have 3D model created from {@link #RenderInvBlock(BlockRenderManager, Block, int, int)}, if false will be a flat image
	 * @return assigned block model id
	 */
	public static int getUniqueBlockModelID(BaseMod mod, boolean full3DItem) {
		int id = nextBlockModelID++;

		if (APRON.isClient()) {
			BLOCK_MODELS.put(id, mod);
			BLOCK_SPECIAL_INV.put(id, full3DItem);
		}

		return id;
	}

	/**
	 * Gets next Entity ID to use.
	 *
	 * @return Assigned ID
	 */
	public static int getUniqueEntityId() {
		return highestEntityId++;
	}

	private static int getUniqueItemSpriteIndex() {
		while (itemSpriteIndex < USED_ITEM_SPRITES.length) {
			if (!USED_ITEM_SPRITES[itemSpriteIndex]) {
				USED_ITEM_SPRITES[itemSpriteIndex] = true;
				--itemSpritesLeft;
				return itemSpriteIndex++;
			}

			++itemSpriteIndex;
		}

		Exception e = new Exception("No more empty item sprite indices left!");
		MOD_LOGGER.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
		ThrowException(e);
		return 0;
	}

	/**
	 * Gets next available index for this sprite map.
	 *
	 * @param path path to sprite sheet to get available index from
	 * @return Assigned sprite index to use
	 */
	public static int getUniqueSpriteIndex(String path) {
		if (path.equals("/gui/items.png")) {
			return getUniqueItemSpriteIndex();
		} else if (path.equals("/terrain.png")) {
			return getUniqueTerrainSpriteIndex();
		} else {
			Exception e = new Exception("No registry for this texture: " + path);
			MOD_LOGGER.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
			ThrowException(e);
			return 0;
		}
	}

	private static int getUniqueTerrainSpriteIndex() {
		while (terrainSpriteIndex < USED_TERRAIN_SPRITES.length) {
			if (!USED_TERRAIN_SPRITES[terrainSpriteIndex]) {
				USED_TERRAIN_SPRITES[terrainSpriteIndex] = true;
				--terrainSpritesLeft;
				return terrainSpriteIndex++;
			}

			++terrainSpriteIndex;
		}

		Exception e = new Exception("No more empty terrain sprite indices left!");
		MOD_LOGGER.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
		ThrowException(e);
		return 0;
	}

	private static void init() {
		if (hasInit) return;

		String usedItemSpritesString = APRON.getUsedItemSpritesString();
		String usedTerrainSpritesString = APRON.getUsedTerrainSpritesString();

		for (int i = 0; i < 256; ++i) {
			USED_ITEM_SPRITES[i] = usedItemSpritesString.charAt(i) == '1';

			if (!USED_ITEM_SPRITES[i]) {
				++itemSpritesLeft;
			}

			USED_TERRAIN_SPRITES[i] = usedTerrainSpritesString.charAt(i) == '1';

			if (!USED_TERRAIN_SPRITES[i]) {
				++terrainSpritesLeft;
			}
		}

		if (APRON.isClient()) {
			initGameRenderer();
		}

		try {
			loadConfig();

			if (props.containsKey("loggingLevel")) {
				cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
			}

			MOD_LOGGER.setLevel(cfgLoggingLevel);

			if ((LOG_FILE.exists() || LOG_FILE.createNewFile()) && LOG_FILE.canWrite() && logHandler == null) {
				logHandler = new FileHandler(LOG_FILE.getPath());
				logHandler.setFormatter(new SimpleFormatter());
				MOD_LOGGER.addHandler(logHandler);
			}

			LOGGER.debug(VERSION + " initializing...");
			File source = new File(ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			if (MOD_CACHE_FOLDER.isDirectory()) {
				readFromModFolder(MOD_CACHE_FOLDER);
			} else {
				LOGGER.error("Path to mod folder is not a directory!");
			}

			for (Path rootPath : Apron.MOD_CONTAINER.getRootPaths()) {
				try {
					readFromClassPath(rootPath.toFile());
				} catch (UnsupportedOperationException ignored) {
					// rootPath is actually a virtual file, cannot be resolved. Skip.
				}
			}

			readFromClassPath(source);

			BUILTIN_RML_MODS.forEach(supplier -> {
				CURRENT_MOD = supplier.getLeft();
				addInternalMod(supplier.getRight().get());
			});

			LOGGER.info("Done initializing.");
			CURRENT_MOD = null;
			props.setProperty("loggingLevel", cfgLoggingLevel.getName());

			for (BaseMod mod : MOD_LIST) {
				CURRENT_MOD = mod.getClassName();
				LifecycleUtils.MOD_LIST.add(CURRENT_MOD);
				mod.ModsLoaded();

				if (!props.containsKey(mod.getClassName())) {
					props.setProperty(mod.getClassName(), "on");
				}
			}

			CURRENT_MOD = null;

			if (APRON.isClient()) {
				Minecraft client = getMinecraftInstance();

				if (client != null) {
					client.options.allKeys = RegisterAllKeys(client.options.allKeys);
					client.options.load();
				}
			}

			LifecycleUtils.triggerModsAllLoadedEvent();

			initStats();
			saveConfig();
		} catch (Throwable e) {
			MOD_LOGGER.throwing("ModLoader", "init", e);
			ThrowException("ModLoader has failed to initialize.", e);

			if (logHandler != null) {
				logHandler.close();
			}

			throw new RuntimeException(e);
		}

		hasInit = true;
		LOGGER.debug("Initialized");
	}

	@Environment(EnvType.CLIENT)
	private static void initGameRenderer() {
		try {
			Minecraft client = getMinecraftInstance();
			if (client != null) client.field_2818 = new EntityRendererProxy(client);
		} catch (SecurityException | IllegalArgumentException e) {
			MOD_LOGGER.throwing("ModLoader", "init", e);
			ThrowException(e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static void initStats() {
		for (int id = 0; id < Block.BLOCKS.length; ++id) {
			if (!Stats.ID_TO_STAT.containsKey(16777216 + id) && Block.BLOCKS[id] != null && Block.BLOCKS[id].isTrackingStatistics()
					&& id < Stats.MINE_BLOCK.length) {
				String str = TranslationStorage.getInstance().get("stat.mineBlock", Block.BLOCKS[id].getTranslatedName());
				Stats.MINE_BLOCK[id] = new ItemOrBlockStat(16777216 + id, str, id).addStat();
				Stats.BLOCK_MINED_STATS.add(Stats.MINE_BLOCK[id]);
			}
		}

		for (int id = 0; id < Item.ITEMS.length; ++id) {
			if (!Stats.ID_TO_STAT.containsKey(16908288 + id) && Item.ITEMS[id] != null) {
				String str = TranslationStorage.getInstance().get("stat.useItem", Item.ITEMS[id].getTranslatedName());
				Stats.USED[id] = new ItemOrBlockStat(16908288 + id, str, id).addStat();

				if (id >= Block.BLOCKS.length) {
					Stats.ITEM_STATS.add(Stats.USED[id]);
				}
			}

			if (!Stats.ID_TO_STAT.containsKey(16973824 + id) && Item.ITEMS[id] != null && Item.ITEMS[id].isDamageable()) {
				String str = TranslationStorage.getInstance().get("stat.breakItem", Item.ITEMS[id].getTranslatedName());
				Stats.BROKEN[id] = new ItemOrBlockStat(16973824 + id, str, id).addStat();
			}
		}

		HashSet<Integer> idHashSet = new HashSet<>();

		for (Object result : CraftingRecipeManager.getInstance().getRecipes()) {
			idHashSet.add(((CraftingRecipe) result).getOutput().itemId);
		}

		for (Object result : SmeltingRecipeManager.getInstance().getRecipes().values()) {
			idHashSet.add(((ItemStack) result).itemId);
		}

		for (int id : idHashSet) {
			if (!Stats.ID_TO_STAT.containsKey(16842752 + id) && Item.ITEMS[id] != null) {
				String str = TranslationStorage.getInstance().get("stat.craftItem", Item.ITEMS[id].getTranslatedName());
				Stats.CRAFTED[id] = new ItemOrBlockStat(16842752 + id, str, id).addStat();
			}
		}
	}

	/**
	 * Use this method to check if GUI is opened for the player.
	 *
	 * @param gui The type of GUI to check for. If null, will check for any GUI
	 * @return true if GUI is open
	 */
	@Environment(EnvType.CLIENT)
	public static boolean isGUIOpen(@Nullable Class<? extends Screen> gui) {
		Minecraft client = getMinecraftInstance();
		if (client == null) return false;

		if (gui == null) {
			return client.currentScreen == null;
		} else {
			return gui.isInstance(client.currentScreen);
		}
	}

	/**
	 * Checks if a mod is loaded.
	 *
	 * @param modName name of the mod to check for
	 * @return true if a mod with supplied name exists in the mod list
	 */
	public static boolean isModLoaded(String modName) {
		Class<?> chk;

		try {
			chk = Class.forName(modName, false, ModLoader.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			try {
				chk = Class.forName("net.minecraft." + modName, false, ModLoader.class.getClassLoader());
			} catch (ClassNotFoundException e2) {
				return false;
			}
		}

		for (BaseMod mod : MOD_LIST) {
			if (chk.isInstance(mod)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Reads the config file and stores the contents in props.
	 */
	public static void loadConfig() {
		try {
			if (CONFIG_DIR.mkdir() && (CONFIG_FILE.exists() || CONFIG_FILE.createNewFile())) {
				if (CONFIG_FILE.canRead()) {
					InputStream in = Files.newInputStream(CONFIG_FILE.toPath());
					props.load(in);
					in.close();
				}
			}
		} catch (IOException e) {
			LOGGER.error("Config could not be loaded!", e);
		}
	}

	/**
	 * Loads an image from a file in the jar into a BufferedImage.
	 *
	 * @param textureManager Reference to texture cache
	 * @param path           Path inside the jar to the image (starts with /)
	 * @return Loaded image to override with
	 * @throws FileNotFoundException if the image is not found
	 * @throws Exception             if the image is corrupted
	 */
	@Environment(EnvType.CLIENT)
	public static BufferedImage loadImage(TextureManager textureManager, String path)
			throws FileNotFoundException, Exception {
		class_303 packManager = textureManager.field_1256;
		InputStream input = packManager.field_1175.method_976(path);

		if (input == null && !path.startsWith("/")) {
			input = packManager.field_1175.method_976("/" + path);
		}

		if (input == null) {
			throw new FileNotFoundException("Image not found: " + path);
		} else {
			BufferedImage image = ImageIO.read(input);

			if (image == null) {
				throw new Exception("Image corrupted: " + path);
			} else {
				return image;
			}
		}
	}

	/**
	 * Is called when an item is picked up from the world.
	 *
	 * @param player that picked up item
	 * @param item   that was picked up
	 */
	public static void OnItemPickup(PlayerEntity player, ItemStack item) {
		for (BaseMod mod : MOD_LIST) {
			mod.OnItemPickup(player, item);
		}
	}

	/**
	 * This method is called every tick while minecraft is running.
	 *
	 * @param client instance of the game class
	 */
	@Environment(EnvType.CLIENT)
	public static void OnTick(Minecraft client) {
		init();

		if (texPack == null || !Objects.equals(client.options.skin, texPack)) {
			texturesAdded = false;
			texPack = client.options.skin;
		}

		if (!texturesAdded && client.textureManager != null) {
			RegisterAllTextureOverrides(client.textureManager);
			texturesAdded = true;
		}

		long newClock = 0L;

		if (client.world != null) {
			newClock = client.world.getTime();
			Iterator<Entry<BaseMod, Boolean>> iterator = inGameHooks.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<BaseMod, Boolean> modSet = iterator.next();

				if ((clock != newClock || !modSet.getValue()) && !modSet.getKey().OnTickInGame(client)) {
					iterator.remove();
				}
			}
		}

		if (client.currentScreen != null) {
			Iterator<Entry<BaseMod, Boolean>> iterator = inGUIHooks.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<BaseMod, Boolean> modSet = iterator.next();

				if ((clock != newClock || !(modSet.getValue() & client.world != null)) && !modSet.getKey().OnTickInGUI(client, client.currentScreen)) {
					iterator.remove();
				}
			}
		}

		if (clock != newClock) {
			for (Entry<BaseMod, Map<KeyBinding, boolean[]>> modSet : keyList.entrySet()) {
				for (Entry<KeyBinding, boolean[]> keySet : modSet.getValue().entrySet()) {
					boolean state = Keyboard.isKeyDown(keySet.getKey().code);
					boolean[] keyInfo = keySet.getValue();
					boolean oldState = keyInfo[1];
					keyInfo[1] = state;

					if (state && (!oldState || keyInfo[0])) {
						modSet.getKey().KeyboardEvent(keySet.getKey());
					}
				}
			}
		}

		clock = newClock;
	}

	@Environment(EnvType.SERVER)
	public static void OnTick(MinecraftServer server) {
		init();

		long l = 0L;

		if (server.field_2841 != null && server.field_2841[0] != null) {
			l = server.field_2841[0].getTime();

			for (Entry<BaseMod, Boolean> entry : inGameHooks.entrySet()) {
				if (clock != l || !entry.getValue()) {
					entry.getKey().OnTickInGame(server);
				}
			}
		}

		clock = l;
	}

	/**
	 * Opens GUI for use with mods.
	 *
	 * @param player instance to open GUI for
	 * @param screen instance of GUI to open for player
	 */
	@Environment(EnvType.CLIENT)
	public static void OpenGUI(PlayerEntity player, Screen screen) {
		init();

		Minecraft client = getMinecraftInstance();

		if (client != null && client.player == player) {
			if (screen != null) {
				client.setScreen(screen);
			}
		}
	}

	/**
	 * Used for generating new blocks in the world.
	 *
	 * @param source Generator to pair with
	 * @param chunkX X coordinate of chunk
	 * @param chunkZ Z coordinate of chunk
	 * @param world  World to generate blocks in
	 */
	public static void PopulateChunk(class_51 source, int chunkX, int chunkZ, World world) {
		init();

		if (APRON.isClient()) {
			Random rnd = new Random(world.getSeed());
			long xSeed = rnd.nextLong() / 2L * 2L + 1L;
			long zSeed = rnd.nextLong() / 2L * 2L + 1L;
			rnd.setSeed((long) chunkX * xSeed + (long) chunkZ * zSeed ^ world.getSeed());

			for (BaseMod mod : MOD_LIST) {
				if (source.toString().equals("RandomLevelSource")) {
					mod.GenerateSurface(world, rnd, chunkX << 4, chunkZ << 4);
				} else if (source.toString().equals("HellRandomLevelSource")) {
					mod.GenerateNether(world, rnd, chunkX << 4, chunkZ << 4);
				}
			}
		} else {
			for (BaseMod mod : MOD_LIST) {
				if (source instanceof class_538) {
					mod.GenerateSurface(world, world.field_214, chunkX, chunkZ);
				} else if (source instanceof class_359) {
					mod.GenerateNether(world, world.field_214, chunkX, chunkZ);
				}
			}
		}
	}

	private static void readFromClassPath(File source) throws IOException {
		LOGGER.debug("Adding mods from " + source.getCanonicalPath());
		ClassLoader loader = ModLoader.class.getClassLoader();

		if (source.isFile() && (source.getName().endsWith(".jar") || source.getName().endsWith(".zip"))) {
			LOGGER.debug("Zip found.");
			InputStream input = Files.newInputStream(source.toPath());
			ZipInputStream zip = new ZipInputStream(input);
			ZipEntry entry;

			while (true) {
				entry = zip.getNextEntry();

				if (entry == null) {
					input.close();
					break;
				}

				String name = entry.getName();
				String[] nameParts = name.replace("\\", "/").split("/");
				String fileName = nameParts[nameParts.length - 1];

				if (!entry.isDirectory() && (fileName.startsWith("mod_") && fileName.endsWith(".class"))) {
					addInternalMod(loader, name);
				}
			}
		} else if (source.isDirectory()) {
			Package pkg = ModLoader.class.getPackage();

			if (pkg != null) {
				String pkgdir = pkg.getName().replace('.', File.separatorChar);
				source = new File(source, pkgdir);
			}

			LOGGER.debug("Directory found.");
			File[] files = source.listFiles();

			if (files != null) {
				for (File file : files) {
					String name = file.getName();

					if (file.isFile() && name.startsWith("mod_") && name.endsWith(".class")) {
						addInternalMod(loader, name);
					}
				}
			}
		}
	}

	@SuppressWarnings({"SameParameterValue", "BulkFileAttributesRead"})
	private static void readFromModFolder(File folder) throws IOException, IllegalArgumentException, SecurityException {
		ClassLoader loader = ModLoader.class.getClassLoader();

		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("folder must be a Directory.");
		} else {
			File[] sourceFiles = folder.listFiles();

			if (sourceFiles != null && sourceFiles.length > 0) {
				for (File source : sourceFiles) {
					if (source.isDirectory()
							|| (source.isFile() && (source.getName().endsWith(".jar") || source.getName().endsWith(".zip")))) {
						FabricLauncherBase.getLauncher().addToClassPath(source.toPath());
						LifecycleUtils.MOD_FILES.add(source);
					}
				}
			}

			if (sourceFiles != null && sourceFiles.length > 0) {
				for (File sourceFile : sourceFiles) {
					File source = sourceFile;

					if (source.isDirectory() || source.isFile() && (source.getName().endsWith(".jar") || source.getName().endsWith(".zip"))) {
						LOGGER.debug("Adding mods from " + source.getCanonicalPath());

						if (!source.isFile()) {
							if (source.isDirectory()) {
								Package pkg = ModLoader.class.getPackage();

								if (pkg != null) {
									String packageDirectory = pkg.getName().replace('.', File.separatorChar);
									source = new File(source, packageDirectory);
								}

								LOGGER.debug("Directory found.");
								File[] directoryFiles = source.listFiles();

								if (directoryFiles != null) {
									for (File directoryFile : directoryFiles) {
										String name = directoryFile.getName();

										if (directoryFile.isFile() && name.startsWith("mod_") && name.endsWith(".class")) {
											addInternalMod(loader, name);
										}
									}
								}
							}
						} else {
							LOGGER.debug("Zip found.");
							InputStream input = Files.newInputStream(source.toPath());
							ZipInputStream zip = new ZipInputStream(input);
							ZipEntry entry;

							while (true) {
								entry = zip.getNextEntry();

								if (entry == null) {
									zip.close();
									input.close();
									break;
								}

								String name = entry.getName();
								String[] parts = name.replace("\\", "/").split("/");
								String fileName = parts[parts.length - 1];

								if (!entry.isDirectory() && fileName.startsWith("mod_") && fileName.endsWith(".class")) {
									addInternalMod(loader, name);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Appends all mod key handlers to the given array and returns it.
	 *
	 * @param keyBindings Array of the original keys
	 * @return the appended array
	 */
	@Environment(EnvType.CLIENT)
	public static KeyBinding[] RegisterAllKeys(KeyBinding[] keyBindings) {
		List<KeyBinding> combinedList = new LinkedList<>(Arrays.asList(keyBindings));

		for (Map<KeyBinding, boolean[]> keyMap : keyList.values()) {
			combinedList.addAll(keyMap.keySet());
		}

		return combinedList.toArray(new KeyBinding[0]);
	}

	/**
	 * Processes all registered texture overrides.
	 *
	 * @param manager Reference to texture cache
	 */
	@Environment(EnvType.CLIENT)
	public static void RegisterAllTextureOverrides(TextureManager manager) {
		ANIM_LIST.clear();
		Minecraft client = getMinecraftInstance();

		for (BaseMod mod : MOD_LIST) {
			mod.RegisterAnimation(client);
		}

		for (class_336 anim : ANIM_LIST) {
			manager.method_1087(anim);
		}

		for (Entry<Integer, Map<String, Integer>> overlay : overrides.entrySet()) {
			for (Entry<String, Integer> overlayEntry : overlay.getValue().entrySet()) {
				String overlayPath = overlayEntry.getKey();
				int index = overlayEntry.getValue();
				int dst = overlay.getKey();

				try {
					BufferedImage im = loadImage(manager, overlayPath);
					class_336 anim = new ModTextureStatic(index, dst, im);
					manager.method_1087(anim);
				} catch (Exception e) {
					MOD_LOGGER.throwing("ModLoader", "RegisterAllTextureOverrides", e);
					ThrowException(e);
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * Adds block to list of blocks the player can use.
	 *
	 * @param block to add
	 */
	public static void RegisterBlock(Block block) {
		RegisterBlock(block, null);
	}

	/**
	 * Adds block to list of blocks the player can use. Includes the item to use for block (unsafely).
	 *
	 * @param block     to add
	 * @param itemClass class to use for block item
	 */
	@SuppressWarnings("unchecked")
	public static void RegisterBlock(@NotNull Block block, Class<? extends BlockItem> itemClass) {
		try {
			if (APRON.isClient()) {
				List<Block> list = (List<Block>) Session.field_871;
				list.add(block);
			}

			int id = block.id;

			BlockItem item;

			int newId = id - 256;

			if (FabricLoader.getInstance().isModLoaded("stationapi")) {
				newId = ((StAPIBlock) block).getOriginalBlockId() - 256;
			}

			if (itemClass != null) {
				item = itemClass.getConstructor(Integer.TYPE)
						.newInstance(newId);
			} else {
				item = new BlockItem(newId);
			}

			if (!FabricLoader.getInstance().isModLoaded("stationapi") && Block.BLOCKS[id] != null && Item.ITEMS[id] == null) {
				Item.ITEMS[id] = item;
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException
					| InvocationTargetException | NoSuchMethodException e) {
			MOD_LOGGER.throwing("ModLoader", "RegisterBlock", e);
			ThrowException(e);
		}
	}

	/**
	 * Registers an entity ID.
	 *
	 * @param entityClass type of entity to register
	 * @param entityName  name of entity
	 * @param entityId    an arbitrary number that <b>cannot</b> be reused for other entities
	 */
	public static void RegisterEntityID(Class<? extends Entity> entityClass, String entityName, int entityId) {
		try {
			EntityRegistry.register(entityClass, entityName, entityId);
			LifecycleUtils.MOD_ENTITIES.add(entityName);
		} catch (IllegalArgumentException e) {
			MOD_LOGGER.throwing("ModLoader", "RegisterEntityID", e);
			ThrowException(e);
		}
	}

	/**
	 * Use this to add an assignable key to the options screen.
	 *
	 * @param mod         The mod which will use this. 99% of the time you should pass <code>this</code>
	 * @param keyBinding  reference to the key to register. Define this in your mod file
	 * @param allowRepeat when true the command will repeat. When false, only called once per press
	 */
	@Environment(EnvType.CLIENT)
	public static void RegisterKey(BaseMod mod, KeyBinding keyBinding, boolean allowRepeat) {
		Map<KeyBinding, boolean[]> keyMap = keyList.get(mod);

		if (keyMap == null) {
			keyMap = new HashMap<>();
		}

		keyMap.put(keyBinding, new boolean[] {allowRepeat, false});
		keyList.put(mod, keyMap);
	}

	/**
	 * Registers a block entity.
	 *
	 * @param blockEntityClass Class of block entity to register
	 * @param id               The given name of entity. Used for saving
	 */
	public static void RegisterTileEntity(Class<? extends BlockEntity> blockEntityClass, String id) {
		if (APRON.isClient()) {
			RegisterTileEntity(blockEntityClass, id, null);
		} else {
			BlockEntity.method_1067(blockEntityClass, id);
			LifecycleUtils.MOD_BLOCK_ENTITIES.add(id);
		}
	}

	/**
	 * Registers a block entity.
	 *
	 * @param blockEntityClass Class of block entity to register
	 * @param id               The given name of entity. Used for saving
	 * @param renderer         Block entity renderer to assign this block entity
	 */
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("unchecked")
	public static void RegisterTileEntity(Class<? extends BlockEntity> blockEntityClass, String id, BlockEntityRenderer renderer) {
		try {
			BlockEntity.method_1067(blockEntityClass, id);

			if (renderer != null) {
				BlockEntityRenderDispatcher ref = BlockEntityRenderDispatcher.INSTANCE;
				Map<Class<? extends BlockEntity>, BlockEntityRenderer> renderers = (Map<Class<? extends BlockEntity>, BlockEntityRenderer>) ref.field_1564;
				renderers.put(blockEntityClass, renderer);
				renderer.setDispatcher(ref);
			}
			LifecycleUtils.MOD_BLOCK_ENTITIES.add(id);
		} catch (IllegalArgumentException e) {
			MOD_LOGGER.throwing("ModLoader", "RegisterTileEntity", e);
			ThrowException(e);
		}
	}

	/**
	 * Remove entity from spawn list for all biomes except Hell.
	 *
	 * @param entityClass Class of entity to spawn
	 * @param spawnGroup  The spawn group to remove entity from. (Monster, Creature, or Water)
	 */
	public static void RemoveSpawn(Class<? extends LivingEntity> entityClass, class_238 spawnGroup) {
		RemoveSpawn(entityClass, spawnGroup, (Biome[]) null);
	}

	/**
	 * Remove entity from spawn list for selected biomes.
	 *
	 * @param entityClass Class of entity to spawn
	 * @param spawnGroup  The spawn group to remove the entity from (Monster, Creature, or Water)
	 * @param biomes      Array of biomes to remove entity spawning from
	 */
	@SuppressWarnings("unchecked")
	public static void RemoveSpawn(Class<? extends LivingEntity> entityClass, class_238 spawnGroup, Biome... biomes) {
		if (entityClass == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if (spawnGroup == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if (biomes == null) {
				biomes = APRON.getStandardBiomes();
			}

			for (Biome biome : biomes) {
				List<class_288> list = (List<class_288>) biome.method_789(spawnGroup);

				if (list != null) {
					list.removeIf(entry -> entry.field_1142 == entityClass);
				}
			}
		}
	}

	/**
	 * Remove entity from spawn list for all biomes except Hell.
	 *
	 * @param entityName Name of entity to remove
	 * @param spawnGroup The spawn group to remove the entity from (Monster, Creature, or Water)
	 */
	public static void RemoveSpawn(String entityName, class_238 spawnGroup) {
		RemoveSpawn(entityName, spawnGroup, (Biome[]) null);
	}

	/**
	 * Remove entity from spawn list for selected biomes.
	 *
	 * @param entityName Name of entity to remove
	 * @param spawnGroup The spawn group to remove the entity from (Monster, Creature, or Water)
	 * @param biomes     Array of biomes to remove entity spawning from
	 */
	@SuppressWarnings("unchecked")
	public static void RemoveSpawn(String entityName, class_238 spawnGroup, Biome... biomes) {
		Class<? extends Entity> entityClass = (Class<? extends Entity>) EntityRegistry.idToClass.get(entityName);

		if (entityClass != null && LivingEntity.class.isAssignableFrom(entityClass)) {
			RemoveSpawn((Class<? extends LivingEntity>) entityClass, spawnGroup, biomes);
		}
	}

	/**
	 * Determines how the block should be rendered.
	 *
	 * @param modelID ID of block model
	 * @return true if block should be rendered using {@link #RenderInvBlock(BlockRenderManager, Block, int, int)}
	 */
	@Environment(EnvType.CLIENT)
	public static boolean RenderBlockIsItemFull3D(int modelID) {
		if (!BLOCK_SPECIAL_INV.containsKey(modelID)) {
			return modelID == 16;
		} else {
			return BLOCK_SPECIAL_INV.get(modelID);
		}
	}

	/**
	 * Renders a block in inventory.
	 *
	 * @param renderer parent renderer; Methods and fields may be referenced from here.
	 * @param block    reference to block to render
	 * @param metadata of block; Damage on an item
	 * @param modelID  ID of block model to render
	 */
	@Environment(EnvType.CLIENT)
	public static void RenderInvBlock(BlockRenderManager renderer, Block block, int metadata, int modelID) {
		BaseMod mod = BLOCK_MODELS.get(modelID);

		if (mod != null) {
			mod.RenderInvBlock(renderer, block, metadata, modelID);
		}
	}

	/**
	 * Renders a block in the world.
	 *
	 * @param renderer parent renderer; Methods and fields may be referenced from here
	 * @param world    to render block in
	 * @param x        x position in world
	 * @param y        y position in world
	 * @param z        z position in world
	 * @param block    reference to block to render
	 * @param modelID  ID of block model to render
	 * @return true if model was rendered
	 */
	@Environment(EnvType.CLIENT)
	public static boolean RenderWorldBlock(BlockRenderManager renderer, BlockView world, int x, int y, int z, Block block, int modelID) {
		BaseMod mod = BLOCK_MODELS.get(modelID);
		return mod != null && mod.RenderWorldBlock(renderer, world, x, y, z, block, modelID);
	}

	/**
	 * Saves properties to the config file.
	 */
	public static void saveConfig() throws IOException {
		if ((CONFIG_DIR.exists() || CONFIG_DIR.mkdir()) && (CONFIG_FILE.exists() || CONFIG_FILE.createNewFile())) {
			if (CONFIG_FILE.canWrite()) {
				OutputStream out = Files.newOutputStream(CONFIG_FILE.toPath());
				props.store(out, "ModLoader Config");
				out.close();
			}
		}
	}

	/**
	 * Enable or disable {@link BaseMod#OnTickInGame(net.minecraft.client.Minecraft)}.
	 *
	 * @param mod      to set
	 * @param enable   whether to add or remove from list
	 * @param useClock if true will only run once each tick on game clock, if false once every render frame
	 */
	public static void SetInGameHook(BaseMod mod, boolean enable, boolean useClock) {
		if (enable) {
			inGameHooks.put(mod, useClock);
		} else {
			inGameHooks.remove(mod);
		}
	}

	/**
	 * Enable or disable {@link BaseMod#OnTickInGUI(Minecraft, Screen)}.
	 *
	 * @param mod      to set
	 * @param enable   whether to add or remove from list
	 * @param useClock if true will only run once each tick on game clock, if false once every render frame
	 */
	@Environment(EnvType.CLIENT)
	public static void SetInGUIHook(BaseMod mod, boolean enable, boolean useClock) {
		if (enable) {
			inGUIHooks.put(mod, useClock);
		} else {
			inGUIHooks.remove(mod);
		}
	}

	/**
	 * Used for setting value of private fields.
	 *
	 * @param instanceClass Class to use with instance
	 * @param instance      Object to get private field from
	 * @param fieldIndex    Offset of field in class
	 * @param value         Value to set
	 * @param <T>           Type of instance
	 * @param <E>           Type of value
	 * @throws IllegalArgumentException if instance isn't compatible with instanceClass
	 * @throws SecurityException        if the thread is not allowed to access field
	 */
	@SuppressWarnings("unused")
	public static <T, E> void setPrivateValue(Class<? super T> instanceClass, T instance, int fieldIndex, E value) throws IllegalArgumentException, SecurityException {
		try {
			Field f = instanceClass.getDeclaredFields()[fieldIndex];
			f.setAccessible(true);

			//			if (field_modifiers == null) {
			//				field_modifiers = Field.class.getDeclaredField("modifiers");
			//				field_modifiers.setAccessible(true);
			//			}
			//
			//			int modifiers = field_modifiers.getInt(f);
			//
			//			if ((modifiers & 16) != 0) {
			//				field_modifiers.setInt(f, modifiers & -17);
			//			}

			f.set(instance, value);
		} catch (IllegalAccessException e) {
			MOD_LOGGER.throwing("ModLoader", "setPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
		}
	}

	/**
	 * Used for setting value of private fields.
	 *
	 * @param instanceClass Class to use with instance
	 * @param instance      Object to get private field from
	 * @param fieldName     Name of the field
	 * @param value         Value to set
	 * @param <T>           Type of instance
	 * @param <E>           Type of value
	 * @throws IllegalArgumentException if instance isn't compatible with instanceClass
	 * @throws SecurityException        if the thread is not allowed to access field
	 * @throws NoSuchFieldException     if field does not exist
	 */
	public static <T, E> void setPrivateValue(Class<? super T> instanceClass, T instance, String fieldName, E value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = ReflectionUtils.getField(instanceClass, new String[]{fieldName});

			//			if (field_modifiers == null) {
			//				field_modifiers = Field.class.getDeclaredField("modifiers");
			//				field_modifiers.setAccessible(true);
			//			}
			//
			//			int modifiers = field_modifiers.getInt(f);
			//
			//			if ((modifiers & 16) != 0) {
			//				field_modifiers.setInt(f, modifiers & -17);
			//			}

			f.setAccessible(true);
			f.set(instance, value);
		} catch (NullPointerException | IllegalAccessException e) {
			MOD_LOGGER.throwing("ModLoader", "setPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
		}
	}

	private static void setupProperties(Class<? extends BaseMod> mod) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException {
		Properties modprops = new Properties();
		File modcfgfile = new File(CONFIG_DIR, Apron.getOriginalClassName(mod.getName()) + ".cfg");

		if (modcfgfile.exists() && modcfgfile.canRead()) {
			modprops.load(Files.newInputStream(modcfgfile.toPath()));
		}

		StringBuilder helptext = new StringBuilder();

		for (Field field : mod.getFields()) {
			if ((field.getModifiers() & 8) != 0 && field.isAnnotationPresent(MLProp.class)) {
				Class<?> type = field.getType();
				MLProp annotation = field.getAnnotation(MLProp.class);
				String key = annotation.name().length() == 0 ? field.getName() : annotation.name();
				Object currentvalue = field.get(null);
				StringBuilder range = new StringBuilder();

				if (annotation.min() != Double.NEGATIVE_INFINITY) {
					range.append(String.format(",>=%.1f", annotation.min()));
				}

				if (annotation.max() != Double.POSITIVE_INFINITY) {
					range.append(String.format(",<=%.1f", annotation.max()));
				}

				StringBuilder info = new StringBuilder();

				if (annotation.info().length() > 0) {
					info.append(" -- ");
					info.append(annotation.info());
				}

				helptext.append(String.format("%s (%s:%s%s)%s\n", key, type.getName(), currentvalue, range, info));

				if (modprops.containsKey(key)) {
					String property = modprops.getProperty(key);
					Object value = null;

					if (type.isAssignableFrom(String.class)) {
						value = property;
					} else if (type.isAssignableFrom(Integer.TYPE)) {
						value = Integer.parseInt(property);
					} else if (type.isAssignableFrom(Short.TYPE)) {
						value = Short.parseShort(property);
					} else if (type.isAssignableFrom(Byte.TYPE)) {
						value = Byte.parseByte(property);
					} else if (type.isAssignableFrom(Boolean.TYPE)) {
						value = Boolean.parseBoolean(property);
					} else if (type.isAssignableFrom(Float.TYPE)) {
						value = Float.parseFloat(property);
					} else if (type.isAssignableFrom(Double.TYPE)) {
						value = Double.parseDouble(property);
					}

					if (value != null) {
						if (value instanceof Number) {
							double num = ((Number) value).doubleValue();
							if (annotation.min() != Double.NEGATIVE_INFINITY && num < annotation.min()
									|| annotation.max() != Double.POSITIVE_INFINITY && num > annotation.max()) {
								continue;
							}
						}

						LOGGER.debug(key + " set to " + value);

						if (!value.equals(currentvalue)) {
							field.set(null, value);
						}
					}
				} else {
					LOGGER.debug(key + " not in config, using default: " + currentvalue);
					modprops.setProperty(key, currentvalue.toString());
				}
			}
		}

		if (!modprops.isEmpty() && (modcfgfile.exists() || modcfgfile.createNewFile()) && modcfgfile.canWrite()) {
			modprops.store(Files.newOutputStream(modcfgfile.toPath()), helptext.toString());
		}
	}

	/**
	 * Is called when an item is picked up from crafting result slot.
	 *
	 * @param player that took the item
	 * @param item   that was taken
	 */
	public static void TakenFromCrafting(PlayerEntity player, ItemStack item) {
		for (BaseMod mod : MOD_LIST) {
			mod.TakenFromCrafting(player, item);
		}
	}

	/**
	 * Is called when an item is picked up from furnace result slot.
	 *
	 * @param player that took the item
	 * @param item   that was taken
	 */
	public static void TakenFromFurnace(PlayerEntity player, ItemStack item) {
		for (BaseMod mod : MOD_LIST) {
			mod.TakenFromFurnace(player, item);
		}
	}

	/**
	 * Used for catching an error and generating an error report.
	 *
	 * @param message the title of the error
	 * @param e       the error to show
	 */
	public static void ThrowException(String message, Throwable e) {
		LOGGER.error(message, e);

		if (APRON.isClient()) {
			Minecraft client = getMinecraftInstance();

			if (client != null) {
				client.method_2102(new class_447(message, e));
			} else {
				throw new RuntimeException(message, e);
			}
		} else {
			throw new RuntimeException(message, e);
		}
	}

	private static void ThrowException(Throwable e) {
		ThrowException("Exception occured in ModLoader", e);
	}

	public ModLoader() {
	}

	@Environment(EnvType.SERVER)
	public static void Init(MinecraftServer server) {
		init();
	}

	@Environment(EnvType.SERVER)
	public static void OpenGUI(PlayerEntity player, int i, Inventory inventory, ScreenHandler container) {
		if (!hasInit) {
			init();
		}

		if (player instanceof ServerPlayerEntity) {
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity) player;
			entityplayermp.method_314();
			int j = entityplayermp.field_260;
			entityplayermp.field_255.method_835(new OpenScreenS2CPacket(j, i, inventory.getName(), inventory.size()));
			entityplayermp.container = container;
			entityplayermp.container.syncId = j;
			entityplayermp.container.addListener(entityplayermp);
		}
	}
}
