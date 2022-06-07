package modloader;

import io.github.betterthanupdates.forge.BabricatedForge;
import io.github.betterthanupdates.forge.mixin.babricated.client.render.entity.BlockEntityRenderDispatcherAccessor;
import io.github.betterthanupdates.forge.mixin.babricated.client.render.entity.PlayerRendererAccessor;
import io.github.betterthanupdates.forge.mixin.babricated.client.texture.TextureManagerAccessor;
import io.github.betterthanupdates.forge.mixin.babricated.entity.BlockEntityAccessor;
import io.github.betterthanupdates.forge.mixin.babricated.entity.EntityRegistryAccessor;
import io.github.betterthanupdates.forge.mixin.babricated.recipe.RecipeRegistryAccessor;
import io.github.betterthanupdates.forge.mixin.babricated.stat.StatsAccessor;
import io.github.betterthanupdates.forge.mixininterface.AchievementAccessor;
import io.github.betterthanupdates.forge.mixininterface.StatAccessor;
import io.github.betterthanupdates.forge.mixininterface.TranslationStorageAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.client.GameStartupError;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StatEntity;
import net.minecraft.client.TexturePackManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.block.BlockEntityRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Session;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.stat.Stats;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.HellBiome;
import net.minecraft.world.biome.SkyBiome;
import net.minecraft.world.source.WorldSource;
import org.lwjgl.input.Keyboard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModLoader {
	private static final List<TextureBinder> animList = new LinkedList<>();
	private static final Map<Integer, BaseMod> blockModels = new HashMap<>();
	private static final Map<Integer, Boolean> blockSpecialInv = new HashMap<>();
	private static final File cfgdir = new File(Minecraft.getGameDirectory(), "/config/");
	private static final File cfgfile = new File(cfgdir, "ModLoader.cfg");
	public static Level cfgLoggingLevel = Level.FINER;
	private static long clock = 0L;
	public static final boolean DEBUG = false;
	private static Field field_modifiers = null;
	private static boolean hasInit = false;
	private static int highestEntityId = 3000;
	private static final Map<BaseMod, Boolean> inGameHooks = new HashMap<>();
	private static final Map<BaseMod, Boolean> inGUIHooks = new HashMap<>();
	private static Minecraft instance = null;
	private static int itemSpriteIndex = 0;
	private static int itemSpritesLeft = 0;
	private static final Map<BaseMod, Map<KeyBinding, boolean[]>> keyList = new HashMap<>();
	private static final File logfile = new File(Minecraft.getGameDirectory(), "ModLoader.txt");
	private static final Logger logger = Logger.getLogger("ModLoader");
	private static FileHandler logHandler = null;
	private static final File modDir = new File(Minecraft.getGameDirectory(), "/mods/");
	private static final LinkedList<BaseMod> modList = new LinkedList<>();
	private static int nextBlockModelID = 1000;
	private static final Map<Integer, Map<String, Integer>> overrides = new HashMap<>();
	public static final Properties props = new Properties();
	private static Biome[] standardBiomes;
	private static int terrainSpriteIndex = 0;
	private static int terrainSpritesLeft = 0;
	private static String texPack = null;
	private static boolean texturesAdded = false;
	private static final boolean[] usedItemSprites = new boolean[256];
	private static final boolean[] usedTerrainSprites = new boolean[256];
	public static final String VERSION = "ModLoader Beta 1.7.3";
	
	/**
	 * Used to give your achievement a readable name and description.
	 * @param achievement the entry to be described
	 * @param name the name of the entry
	 * @param description the description of the entry
	 */
	public static void AddAchievementDesc(Achievement achievement, String name, String description) {
		try {
			if (achievement.name.contains(".")) {
				String[] split = achievement.name.split("\\.");
				if (split.length == 2) {
					String key = split[1];
					AddLocalization("achievement." + key, name);
					AddLocalization("achievement." + key + ".desc", description);
					((StatAccessor)achievement).setName(TranslationStorage.getInstance().translate("achievement." + key));
					((AchievementAccessor)achievement).setDescription(TranslationStorage.getInstance().translate("achievement." + key + ".desc"));
				} else {
					((StatAccessor)achievement).setName(name);
					((AchievementAccessor)achievement).setDescription(description);
				}
			} else {
				((StatAccessor)achievement).setName(name);
				((AchievementAccessor)achievement).setDescription(description);
			}
		} catch (IllegalArgumentException | SecurityException var5) {
			logger.throwing("ModLoader", "AddAchievementDesc", var5);
			ThrowException(var5);
		}
	}
	
	/**
	 * Used for adding new sources of fuel to the furnace.
	 * @param id the item to be used as fuel.
	 * @return the fuel ID assigned to the item.
	 */
	public static int AddAllFuel(int id) {
		logger.finest("Finding fuel for " + id);
		int result = 0;
		Iterator<BaseMod> iter = modList.iterator();

		while(iter.hasNext() && result == 0) {
			result = iter.next().AddFuel(id);
		}

		if (result != 0) {
			logger.finest("Returned " + result);
		}

		return result;
	}
	
	/**
	 * Used to add all mod entity renderers.
	 * @param rendererMap
	 */
	public static void AddAllRenderers(Map<Class<? extends Entity>, EntityRenderer> rendererMap) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		for(BaseMod mod : modList) {
			mod.AddRenderer(rendererMap);
		}
	}
	
	/**
	 * Registers one animation instance.
	 * @param anim
	 */
	public static void addAnimation(TextureBinder anim) {
		logger.finest("Adding animation " + anim.toString());

		for(TextureBinder oldAnim : animList) {
			if (oldAnim.renderMode == anim.renderMode && oldAnim.index == anim.index) {
				animList.remove(anim);
				break;
			}
		}

		animList.add(anim);
	}
	
	/**
	 * Use this when you need the player to have new armor skin.
	 * @param armor
	 * @return
	 */
	public static int AddArmor(String armor) {
		try {
			String[] existingArmor = PlayerRendererAccessor.getArmorTypes();
			List<String> existingArmorList = Arrays.asList(existingArmor);
			List<String> combinedList = new ArrayList();
			combinedList.addAll(existingArmorList);
			if (!combinedList.contains(armor)) {
				combinedList.add(armor);
			}

			int index = combinedList.indexOf(armor);
			PlayerRendererAccessor.setArmorTypes(combinedList.toArray(new String[0]));
			return index;
		} catch (IllegalArgumentException var5) {
			logger.throwing("ModLoader", "AddArmor", var5);
			ThrowException("An impossible error has occured!", var5);
		}

		return -1;
	}
	
	/**
	 * Method for adding raw strings to the translation table.
	 * @param key
	 * @param value
	 */
	public static void AddLocalization(String key, String value) {
		Properties props = ((TranslationStorageAccessor)TranslationStorage.getInstance()).getTranslations();

		if (props != null) {
			props.put(key, value);
		}
	}

	private static void addMod(ClassLoader loader, String filename) {
		try {
			String name = filename.split("\\.")[0];
			if (name.contains("$")) {
				return;
			}

			if (props.containsKey(name) && (props.getProperty(name).equalsIgnoreCase("no") || props.getProperty(name).equalsIgnoreCase("off"))) {
				return;
			}

//			Package pack = ModLoader.class.getPackage();
//			if (pack != null) {
//				name = pack.getName() + "." + name;
//			}

			Class<?> instclass = loader.loadClass(name);
			if (!BaseMod.class.isAssignableFrom(instclass)) {
				return;
			}

			setupProperties((Class<? extends BaseMod>) instclass);
			BaseMod mod = (BaseMod)instclass.newInstance();
			if (mod != null) {
				modList.add(mod);
				logger.fine("Mod Loaded: \"" + mod.toString() + "\" from " + filename);
				System.out.println("Mod Loaded: " + mod.toString());
			}
		} catch (Throwable var6) {
			logger.fine("Failed to load mod from \"" + filename + "\"");
			System.out.println("Failed to load mod from \"" + filename + "\"");
			logger.throwing("ModLoader", "addMod", var6);
			ThrowException(var6);
		}

	}

	/**
	 * This method will allow adding name to item in inventory.
	 * @param instance
	 * @param name
	 */
	public static void AddName(Object instance, String name) {
		String tag = null;
		if (instance instanceof Item) {
			Item item = (Item)instance;
			if (item.getTranslationKey() != null) {
				tag = item.getTranslationKey() + ".name";
			}
		} else if (instance instanceof Block) {
			Block block = (Block)instance;
			if (block.getTranslationKey() != null) {
				tag = block.getTranslationKey() + ".name";
			}
		} else if (instance instanceof ItemStack) {
			ItemStack stack = (ItemStack)instance;
			if (stack.getTranslationKey() != null) {
				tag = stack.getTranslationKey() + ".name";
			}
		} else {
			Exception e = new Exception(instance.getClass().getName() + " cannot have name attached to it!");
			logger.throwing("ModLoader", "AddName", e);
			ThrowException(e);
		}

		if (tag != null) {
			AddLocalization(tag, name);
		} else {
			Exception e = new Exception(instance + " is missing name tag!");
			logger.throwing("ModLoader", "AddName", e);
			ThrowException(e);
		}
	}

	/**
	 * Use this to add custom images for your items and blocks.
	 * @param fileToOverride
	 * @param fileToAdd
	 * @return
	 */
	public static int addOverride(String fileToOverride, String fileToAdd) {
		try {
			int i = getUniqueSpriteIndex(fileToOverride);
			addOverride(fileToOverride, fileToAdd, i);
			return i;
		} catch (Throwable var3) {
			logger.throwing("ModLoader", "addOverride", var3);
			ThrowException(var3);
			throw new RuntimeException(var3);
		}
	}

	/**
	 * Registers one texture override to be done.
	 * @param path
	 * @param overlayPath
	 * @param index
	 */
	public static void addOverride(String path, String overlayPath, int index) {
		int dst = -1;
		int left = 0;
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

		System.out.println("Overriding " + path + " with " + overlayPath + " @ " + index + ". " + left + " left.");
		logger.finer("addOverride(" + path + "," + overlayPath + "," + index + "). " + left + " left.");
		Map<String, Integer> overlays = overrides.computeIfAbsent(dst, k -> new HashMap<>());

		overlays.put(overlayPath, index);
	}

	/**
	 * Add recipe to crafting list.
	 * @param output
	 * @param ingredients
	 */
	public static void AddRecipe(ItemStack output, Object... ingredients) {
		((RecipeRegistryAccessor)RecipeRegistry.getInstance()).callAddShapedRecipe(output, ingredients);
	}

	/**
	 * Add recipe to crafting list.
	 * @param output
	 * @param ingredients
	 */
	public static void AddShapelessRecipe(ItemStack output, Object... ingredients) {
		((RecipeRegistryAccessor)RecipeRegistry.getInstance()).callAddShapelessRecipe(output, ingredients);
	}

	/**
	 * Used to add smelting recipes to the furnace.
	 * @param input
	 * @param output
	 */
	public static void AddSmelting(int input, ItemStack output) {
		SmeltingRecipeRegistry.getInstance().addSmeltingRecipe(input, output);
	}

	/**
	 * Add entity to spawn list for all biomes except Hell.
	 * @param entityClass
	 * @param weightedProb
	 * @param spawnGroup
	 */
	public static void AddSpawn(Class<? extends LivingEntity> entityClass, int weightedProb, SpawnGroup spawnGroup) {
		AddSpawn(entityClass, weightedProb, spawnGroup, (Biome) null);
	}

	/**
	 * Add entity to spawn list for selected biomes.
	 * @param entityClass
	 * @param weightedProb
	 * @param spawnGroup
	 * @param biomes
	 */
	public static void AddSpawn(Class<? extends LivingEntity> entityClass, int weightedProb, SpawnGroup spawnGroup, Biome... biomes) {
		if (entityClass == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if (spawnGroup == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if (biomes == null) {
				biomes = standardBiomes;
			}

			for (Biome biome : biomes) {
				List<EntityEntry> list = biome.getSpawnList(spawnGroup);
				if (list != null) {
					boolean exists = false;

					for (EntityEntry entry : list) {
						if (entry.entryClass == entityClass) {
							entry.rarity = weightedProb;
							exists = true;
							break;
						}
					}

					if (!exists) {
						list.add(new EntityEntry(entityClass, weightedProb));
					}
				}
			}
		}
	}
	
	/**
	 * Add entity to spawn list for all biomes except Hell.
	 * @param entityName
	 * @param weightedProb
	 * @param spawnGroup
	 */
	public static void AddSpawn(String entityName, int weightedProb, SpawnGroup spawnGroup) {
		AddSpawn(entityName, weightedProb, spawnGroup, (Biome) null);
	}
	
	/**
	 * Add entity to spawn list for selected biomes.
	 * @param entityName
	 * @param weightedProb
	 * @param spawnGroup
	 * @param biomes
	 */
	@SuppressWarnings("unchecked")
	public static void AddSpawn(String entityName, int weightedProb, SpawnGroup spawnGroup, Biome... biomes) {
		Class<? extends Entity> entityClass = EntityRegistryAccessor.getSTRING_ID_TO_CLASS().get(entityName);
		if (entityClass != null && LivingEntity.class.isAssignableFrom(entityClass)) {
			AddSpawn((Class<? extends LivingEntity>) entityClass, weightedProb, spawnGroup, biomes);
		}
	}
	
	/**
	 * Dispenses the entity associated with the selected item.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param xVel
	 * @param zVel
	 * @param stack
	 * @return
	 */
	public static boolean DispenseEntity(World world, double x, double y, double z, int xVel, int zVel, ItemStack stack) {
		boolean result = false;
		Iterator<BaseMod> iter = modList.iterator();

		while(iter.hasNext() && !result) {
			result = iter.next().DispenseEntity(world, x, y, z, xVel, zVel, stack);
		}

		return result;
	}
	
	/**
	 * Use this method if you need a list of loaded mods.
	 * @return
	 */
	public static List<BaseMod> getLoadedMods() {
		return Collections.unmodifiableList(modList);
	}
	
	/**
	 * Use this to get a reference to the logger ModLoader uses.
	 * @return
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	/**
	 * Use this method to get a reference to Minecraft instance.
	 * Method was changed from original.
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraftInstance() {
		if (instance == null) {
			instance = (Minecraft) FabricLoader.getInstance().getGameInstance();
		}
		return instance;
	}
	
	/**
	 * Used for getting value of private fields.
	 * @param instanceClass
	 * @param instance
	 * @param fieldIndex
	 * @param <T>
	 * @param <E>
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> T getPrivateValue(Class<? super E> instanceClass, E instance, int fieldIndex) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = instanceClass.getDeclaredFields()[fieldIndex];
			f.setAccessible(true);
			return (T)f.get(instance);
		} catch (IllegalAccessException var4) {
			logger.throwing("ModLoader", "getPrivateValue", var4);
			ThrowException("An impossible error has occured!", var4);
			return null;
		}
	}
	
	/**
	 * Used for getting value of private fields.
	 * @param instanceClass
	 * @param instance
	 * @param fieldName
	 * @param <T>
	 * @param <E>
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> T getPrivateValue(Class<? super E> instanceClass, E instance, String fieldName) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = instanceClass.getDeclaredField(fieldName);
			f.setAccessible(true);
			return (T)f.get(instance);
		} catch (IllegalAccessException var4) {
			logger.throwing("ModLoader", "getPrivateValue", var4);
			ThrowException("An impossible error has occured!", var4);
			return null;
		}
	}
	
	/**
	 * Assigns a model id for blocks to use for the given mod.
	 * @param mod
	 * @param full3DItem
	 * @return
	 */
	public static int getUniqueBlockModelID(BaseMod mod, boolean full3DItem) {
		int id = nextBlockModelID++;
		blockModels.put(id, mod);
		blockSpecialInv.put(id, full3DItem);
		return id;
	}
	
	/**
	 * Gets next Entity ID to use.
	 * @return
	 */
	public static int getUniqueEntityId() {
		return highestEntityId++;
	}

	private static int getUniqueItemSpriteIndex() {
		while(itemSpriteIndex < usedItemSprites.length) {
			if (!usedItemSprites[itemSpriteIndex]) {
				usedItemSprites[itemSpriteIndex] = true;
				--itemSpritesLeft;
				return itemSpriteIndex++;
			}

			++itemSpriteIndex;
		}

		Exception e = new Exception("No more empty item sprite indices left!");
		logger.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
		ThrowException(e);
		return 0;
	}

	/**
	 * Gets next available index for this sprite map.
	 * @param path
	 * @return
	 */
	public static int getUniqueSpriteIndex(String path) {
		if (path.equals("/gui/items.png")) {
			return getUniqueItemSpriteIndex();
		} else if (path.equals("/terrain.png")) {
			return getUniqueTerrainSpriteIndex();
		} else {
			Exception e = new Exception("No registry for this texture: " + path);
			logger.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
			ThrowException(e);
			return 0;
		}
	}

	private static int getUniqueTerrainSpriteIndex() {
		while(terrainSpriteIndex < usedTerrainSprites.length) {
			if (!usedTerrainSprites[terrainSpriteIndex]) {
				usedTerrainSprites[terrainSpriteIndex] = true;
				--terrainSpritesLeft;
				return terrainSpriteIndex++;
			}

			++terrainSpriteIndex;
		}

		Exception e = new Exception("No more empty terrain sprite indices left!");
		logger.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
		ThrowException(e);
		return 0;
	}

	private static void init() {
		hasInit = true;
		String usedItemSpritesString = "1111111111111111111111111111111111111101111111011111111111111001111111111111111111111111111011111111100110000011111110000000001111111001100000110000000100000011000000010000001100000000000000110000000000000000000000000000000000000000000000001100000000000000";
		String usedTerrainSpritesString = "1111111111111111111111111111110111111111111111111111110111111111111111111111000111111011111111111111001111111110111111111111100011111111000010001111011110000000111111000000000011111100000000001111000000000111111000000000001101000000000001111111111111000011";

		for(int i = 0; i < 256; ++i) {
			usedItemSprites[i] = usedItemSpritesString.charAt(i) == '1';
			if (!usedItemSprites[i]) {
				++itemSpritesLeft;
			}

			usedTerrainSprites[i] = usedTerrainSpritesString.charAt(i) == '1';
			if (!usedTerrainSprites[i]) {
				++terrainSpritesLeft;
			}
		}

		try {
			instance = getMinecraftInstance();
			instance.gameRenderer = new EntityRendererProxy(instance);
			field_modifiers = Field.class.getDeclaredField("modifiers");
			field_modifiers.setAccessible(true);
			Field[] fieldArray = Biome.class.getDeclaredFields();
			List<Biome> biomes = new LinkedList<>();

			for (Field field : fieldArray) {
				Class<?> fieldType = field.getType();
				if ((field.getModifiers() & 8) != 0 && fieldType.isAssignableFrom(Biome.class)) {
					Biome biome = (Biome) field.get(null);
					if (!(biome instanceof HellBiome) && !(biome instanceof SkyBiome)) {
						biomes.add(biome);
					}
				}
			}

			standardBiomes = biomes.toArray(new Biome[0]);
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException var10) {
			logger.throwing("ModLoader", "init", var10);
			ThrowException(var10);
			throw new RuntimeException(var10);
		}

		try {
			loadConfig();
			if (props.containsKey("loggingLevel")) {
				cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
			}

			logger.setLevel(cfgLoggingLevel);
			if ((logfile.exists() || logfile.createNewFile()) && logfile.canWrite() && logHandler == null) {
				logHandler = new FileHandler(logfile.getPath());
				logHandler.setFormatter(new SimpleFormatter());
				logger.addHandler(logHandler);
			}

			logger.fine("ModLoader Beta 1.7.3 Initializing...");
			System.out.println("ModLoader Beta 1.7.3 Initializing...");
			File source = new File(ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			modDir.mkdirs();
			readFromModFolder(BabricatedForge.MOD_CACHE_FOLDER);
			readFromClassPath(source);
			System.out.println("Done.");
			props.setProperty("loggingLevel", cfgLoggingLevel.getName());

			for(BaseMod mod : modList) {
				mod.ModsLoaded();
				if (!props.containsKey(mod.getClass().getName())) {
					props.setProperty(mod.getClass().getName(), "on");
				}
			}

			instance.options.keyBindings = RegisterAllKeys(instance.options.keyBindings);
			instance.options.load();
			initStats();
			saveConfig();
		} catch (Throwable var9) {
			logger.throwing("ModLoader", "init", var9);
			ThrowException("ModLoader has failed to initialize.", var9);
			if (logHandler != null) {
				logHandler.close();
			}

			throw new RuntimeException(var9);
		}
	}

	@SuppressWarnings("unchecked")
	private static void initStats() {
		for(int id = 0; id < Block.BY_ID.length; ++id) {
			if (!StatsAccessor.getIdMap().containsKey(16777216 + id) && Block.BY_ID[id] != null && Block.BY_ID[id].isStatEnabled()) {
				String str = TranslationStorage.getInstance().translate("stat.mineBlock", Block.BY_ID[id].getTranslatedName());
				Stats.mineBlock[id] = new StatEntity(16777216 + id, str, id).register();
				Stats.blocksMinedList.add(Stats.mineBlock[id]);
			}
		}

		for(int id = 0; id < Item.byId.length; ++id) {
			if (!StatsAccessor.getIdMap().containsKey(16908288 + id) && Item.byId[id] != null) {
				String str = TranslationStorage.getInstance().translate("stat.useItem", Item.byId[id].getTranslatedName());
				Stats.useItem[id] = new StatEntity(16908288 + id, str, id).register();
				if (id >= Block.BY_ID.length) {
					Stats.useStatList.add(Stats.useItem[id]);
				}
			}

			if (!StatsAccessor.getIdMap().containsKey(16973824 + id) && Item.byId[id] != null && Item.byId[id].hasDurability()) {
				String str = TranslationStorage.getInstance().translate("stat.breakItem", Item.byId[id].getTranslatedName());
				Stats.breakItem[id] = new StatEntity(16973824 + id, str, id).register();
			}
		}

		HashSet<Integer> idHashSet = new HashSet<>();

		for(Object result : RecipeRegistry.getInstance().getRecipes()) {
			idHashSet.add(((Recipe)result).getOutput().itemId);
		}

		for(Object result : SmeltingRecipeRegistry.getInstance().getRecipes().values()) {
			idHashSet.add(((ItemStack)result).itemId);
		}

		for(int id : idHashSet) {
			if (!StatsAccessor.getIdMap().containsKey(16842752 + id) && Item.byId[id] != null) {
				String str = TranslationStorage.getInstance().translate("stat.craftItem", Item.byId[id].getTranslatedName());
				Stats.timesCrafted[id] = new StatEntity(16842752 + id, str, id).register();
			}
		}
	}
	
	/**
	 * Use this method to check if GUI is opened for the player.
	 * @param gui
	 * @return
	 */
	public static boolean isGUIOpen(Class<? extends Screen> gui) {
		Minecraft game = getMinecraftInstance();
		if (gui == null) {
			return game.currentScreen == null;
		} else {
			return game.currentScreen == null && gui != null ? false : gui.isInstance(game.currentScreen);
		}
	}
	
	/**
	 * Checks if a mod is loaded.
	 * @param modName
	 * @return
	 */
	public static boolean isModLoaded(String modName) {
		Class<?> chk = null;

		try {
			chk = Class.forName(modName);
		} catch (ClassNotFoundException var4) {
			return false;
		}

		if (chk != null) {
			for(BaseMod mod : modList) {
				if (chk.isInstance(mod)) {
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Reads the config file and stores the contents in props.
	 * @throws IOException
	 */
	public static void loadConfig() throws IOException {
		cfgdir.mkdir();
		if (cfgfile.exists() || cfgfile.createNewFile()) {
			if (cfgfile.canRead()) {
				InputStream in = Files.newInputStream(cfgfile.toPath());
				props.load(in);
				in.close();
			}

		}
	}
	
	/**
	 * Loads an image from a file in the jar into a BufferedImage.
	 * @param textureManager
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage loadImage(TextureManager textureManager, String path) throws Exception {
		TexturePackManager pack = ((TextureManagerAccessor)textureManager).getTexturePackManager();
		InputStream input = pack.texturePack.getResourceAsStream(path);
		if (input == null) {
			throw new Exception("Image not found: " + path);
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
	 * @param player
	 * @param item
	 */
	public static void OnItemPickup(PlayerEntity player, ItemStack item) {
		for(BaseMod mod : modList) {
			mod.OnItemPickup(player, item);
		}
	}
	
	/**
	 * This method is called every tick while minecraft is running.
	 * @param minecraft
	 */
	public static void OnTick(Minecraft minecraft) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		if (texPack == null || !Objects.equals(minecraft.options.skin, texPack)) {
			texturesAdded = false;
			texPack = minecraft.options.skin;
		}

		if (!texturesAdded && minecraft.textureManager != null) {
			RegisterAllTextureOverrides(minecraft.textureManager);
			texturesAdded = true;
		}

		long newclock = 0L;
		if (minecraft.world != null) {
			newclock = minecraft.world.getWorldTime();
			Iterator<Entry<BaseMod, Boolean>> iter = inGameHooks.entrySet().iterator();

			while(iter.hasNext()) {
				Entry<BaseMod, Boolean> modSet = iter.next();
				if ((clock != newclock || !modSet.getValue()) && !modSet.getKey().OnTickInGame(minecraft)) {
					iter.remove();
				}
			}
		}

		if (minecraft.currentScreen != null) {
			Iterator<Entry<BaseMod, Boolean>> iter = inGUIHooks.entrySet().iterator();

			while(iter.hasNext()) {
				Entry<BaseMod, Boolean> modSet = iter.next();
				if ((clock != newclock || !(modSet.getValue() & minecraft.world != null)) && !modSet.getKey().OnTickInGUI(minecraft, minecraft.currentScreen)) {
					iter.remove();
				}
			}
		}

		if (clock != newclock) {
			for(Entry<BaseMod, Map<KeyBinding, boolean[]>> modSet : keyList.entrySet()) {
				for(Entry<KeyBinding, boolean[]> keySet : modSet.getValue().entrySet()) {
					boolean state = Keyboard.isKeyDown(keySet.getKey().key);
					boolean[] keyInfo = keySet.getValue();
					boolean oldState = keyInfo[1];
					keyInfo[1] = state;
					if (state && (!oldState || keyInfo[0])) {
						modSet.getKey().KeyboardEvent(keySet.getKey());
					}
				}
			}
		}

		clock = newclock;
	}
	
	/**
	 * Opens GUI for use with mods.
	 * @param player
	 * @param screen
	 */
	public static void OpenGUI(PlayerEntity player, Screen screen) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		Minecraft game = getMinecraftInstance();
		if (game.player == player) {
			if (screen != null) {
				game.openScreen(screen);
			}
		}
	}
	
	/**
	 * Used for generating new blocks in the world.
	 * @param worldSource
	 * @param chunkX
	 * @param chunkZ
	 * @param world
	 */
	public static void PopulateChunk(WorldSource worldSource, int chunkX, int chunkZ, World world) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		Random rnd = new Random(world.getSeed());
		long xSeed = rnd.nextLong() / 2L * 2L + 1L;
		long zSeed = rnd.nextLong() / 2L * 2L + 1L;
		rnd.setSeed((long)chunkX * xSeed + (long)chunkZ * zSeed ^ world.getSeed());

		for(BaseMod mod : modList) {
			if (worldSource.toString().equals("RandomLevelSource")) {
				mod.GenerateSurface(world, rnd, chunkX << 4, chunkZ << 4);
			} else if (worldSource.toString().equals("HellRandomLevelSource")) {
				mod.GenerateNether(world, rnd, chunkX << 4, chunkZ << 4);
			}
		}
	}

	private static void readFromClassPath(File source) throws IOException {
		logger.finer("Adding mods from " + source.getCanonicalPath());
		ClassLoader loader = ModLoader.class.getClassLoader();
		if (source.isFile() && (source.getName().endsWith(".jar") || source.getName().endsWith(".zip"))) {
			logger.finer("Zip found.");
			InputStream input = Files.newInputStream(source.toPath());
			ZipInputStream zip = new ZipInputStream(input);
			ZipEntry entry = null;

			while(true) {
				entry = zip.getNextEntry();
				if (entry == null) {
					input.close();
					break;
				}

				String name = entry.getName();
				if (!entry.isDirectory() && name.startsWith("mod_") && name.endsWith(".class")) {
					addMod(loader, name);
				}
			}
		} else if (source.isDirectory()) {
			Package pkg = ModLoader.class.getPackage();
			if (pkg != null) {
				String pkgdir = pkg.getName().replace('.', File.separatorChar);
				source = new File(source, pkgdir);
			}

			logger.finer("Directory found.");
			File[] files = source.listFiles();
			if (files != null) {
				for (File file : files) {
					String name = file.getName();
					if (file.isFile() && name.startsWith("mod_") && name.endsWith(".class")) {
						addMod(loader, name);
					}
				}
			}
		}
	}

	private static void readFromModFolder(File folder) throws IOException, IllegalArgumentException, SecurityException {
		ClassLoader loader = Minecraft.class.getClassLoader();
		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("folder must be a Directory.");
		} else {
			File[] sourcefiles = folder.listFiles();
			for (File source : sourcefiles) {
				if (source.isDirectory() || source.isFile() && (source.getName().endsWith(".jar") || source.getName().endsWith(".zip"))) {
					FabricLauncherBase.getLauncher().addToClassPath(source.toPath());
				}
			}

			for (File sourcefile : sourcefiles) {
				File source = sourcefile;
				if (source.isDirectory() || source.isFile() && (source.getName().endsWith(".jar") || source.getName().endsWith(".zip"))) {
					logger.finer("Adding mods from " + source.getCanonicalPath());
					if (!source.isFile()) {
						if (source.isDirectory()) {
							Package pkg = ModLoader.class.getPackage();
							if (pkg != null) {
								String pkgdir = pkg.getName().replace('.', File.separatorChar);
								source = new File(source, pkgdir);
							}

							logger.finer("Directory found.");
							File[] dirfiles = source.listFiles();
							if (dirfiles != null) {
								for (File dirfile : dirfiles) {
									String name = dirfile.getName();
									if (dirfile.isFile() && name.startsWith("mod_") && name.endsWith(".class")) {
										addMod(loader, name);
									}
								}
							}
						}
					} else {
						logger.finer("Zip found.");
						InputStream input = Files.newInputStream(source.toPath());
						ZipInputStream zip = new ZipInputStream(input);
						ZipEntry entry = null;

						while (true) {
							entry = zip.getNextEntry();
							if (entry == null) {
								zip.close();
								input.close();
								break;
							}

							String name = entry.getName();
							if (!entry.isDirectory() && name.startsWith("mod_") && name.endsWith(".class")) {
								addMod(loader, name);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Appends all mod key handlers to the given array and returns it.
	 * @param keyBindings
	 * @return
	 */
	public static KeyBinding[] RegisterAllKeys(KeyBinding[] keyBindings) {
		List<KeyBinding> combinedList = new LinkedList<>(Arrays.asList(keyBindings));

		for(Map<KeyBinding, boolean[]> keyMap : keyList.values()) {
			combinedList.addAll(keyMap.keySet());
		}

		return (KeyBinding[])combinedList.toArray(new KeyBinding[0]);
	}
	
	/**
	 * Processes all registered texture overrides.
	 * @param manager
	 */
	public static void RegisterAllTextureOverrides(TextureManager manager) {
		animList.clear();
		Minecraft game = getMinecraftInstance();

		for(BaseMod mod : modList) {
			mod.RegisterAnimation(game);
		}

		for(TextureBinder anim : animList) {
			manager.addTextureBinder(anim);
		}

		for(Entry<Integer, Map<String, Integer>> overlay : overrides.entrySet()) {
			for(Entry<String, Integer> overlayEntry : overlay.getValue().entrySet()) {
				String overlayPath = overlayEntry.getKey();
				int index = overlayEntry.getValue();
				int dst = overlay.getKey();

				try {
					BufferedImage im = loadImage(manager, overlayPath);
					TextureBinder anim = new ModTextureStatic(index, dst, im);
					manager.addTextureBinder(anim);
				} catch (Exception var11) {
					logger.throwing("ModLoader", "RegisterAllTextureOverrides", var11);
					ThrowException(var11);
					throw new RuntimeException(var11);
				}
			}
		}
	}
	
	/**
	 * Adds block to list of blocks the player can use.
	 * @param block
	 */
	public static void RegisterBlock(Block block) {
		RegisterBlock(block, null);
	}
	
	/**
	 * Adds block to list of blocks the player can use.
	 * @param block
	 * @param itemClass
	 */
	public static void RegisterBlock(Block block, Class<? extends BlockItem> itemClass) {
		try {
			if (block == null) {
				throw new IllegalArgumentException("block parameter cannot be null.");
			}

			List<Block> list = Session.defaultCreativeInventory;
			list.add(block);
			int id = block.id;
			BlockItem item = null;
			if (itemClass != null) {
				item = (BlockItem)itemClass.getConstructor(Integer.TYPE).newInstance(id - 256);
			} else {
				item = new BlockItem(id - 256);
			}

			if (Block.BY_ID[id] != null && Item.byId[id] == null) {
				Item.byId[id] = item;
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException |
				 InvocationTargetException | NoSuchMethodException var5) {
			logger.throwing("ModLoader", "RegisterBlock", var5);
			ThrowException(var5);
		}
	}
	
	/**
	 * Registers an entity ID.
	 * @param entityClass
	 * @param entityName
	 * @param id
	 */
	public static void RegisterEntityID(Class<? extends Entity> entityClass, String entityName, int id) {
		try {
			EntityRegistryAccessor.callRegister(entityClass, entityName, id);
		} catch (IllegalArgumentException var4) {
			logger.throwing("ModLoader", "RegisterEntityID", var4);
			ThrowException(var4);
		}
	}
	
	/**
	 * Use this to add an assignable key to the options menu.
	 * @param mod
	 * @param keyBinding
	 * @param allowRepeat
	 */
	public static void RegisterKey(BaseMod mod, KeyBinding keyBinding, boolean allowRepeat) {
		Map<KeyBinding, boolean[]> keyMap = keyList.get(mod);
		if (keyMap == null) {
			keyMap = new HashMap<>();
		}

		keyMap.put(keyBinding, new boolean[]{allowRepeat, false});
		keyList.put(mod, keyMap);
	}
	
	/**
	 * Registers a tile entity.
	 * @param blockEntityClass
	 * @param id
	 */
	public static void RegisterTileEntity(Class<? extends BlockEntity> blockEntityClass, String id) {
		RegisterTileEntity(blockEntityClass, id, null);
	}
	
	/**
	 * Registers a tile entity.
	 * @param blockEntityClass
	 * @param id
	 * @param renderer
	 */
	public static void RegisterTileEntity(Class<? extends BlockEntity> blockEntityClass, String id, BlockEntityRenderer renderer) {
		try {
			BlockEntityAccessor.callRegister(blockEntityClass, id);
			if (renderer != null) {
				BlockEntityRenderDispatcher ref = BlockEntityRenderDispatcher.INSTANCE;
				Map<Class<? extends BlockEntity>, BlockEntityRenderer> renderers = ((BlockEntityRenderDispatcherAccessor)ref).getCustomRenderers();
				renderers.put(blockEntityClass, renderer);
				renderer.setRenderDispatcher(ref);
			}
		} catch (IllegalArgumentException var5) {
			logger.throwing("ModLoader", "RegisterTileEntity", var5);
			ThrowException(var5);
		}
	}
	
	/**
	 * Remove entity from spawn list for all biomes except Hell.
	 * @param entityClass
	 * @param spawnGroup
	 */
	public static void RemoveSpawn(Class<? extends LivingEntity> entityClass, SpawnGroup spawnGroup) {
		RemoveSpawn(entityClass, spawnGroup, (Biome) null);
	}
	
	/**
	 * Remove entity from spawn list for selected biomes.
	 * @param entityClass
	 * @param spawnGroup
	 * @param biomes
	 */
	public static void RemoveSpawn(Class<? extends LivingEntity> entityClass, SpawnGroup spawnGroup, Biome... biomes) {
		if (entityClass == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if (spawnGroup == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if (biomes == null) {
				biomes = standardBiomes;
			}

			for (Biome biome : biomes) {
				List<EntityEntry> list = biome.getSpawnList(spawnGroup);
				if (list != null) {
					list.removeIf(entry -> entry.entryClass == entityClass);
				}
			}
		}
	}
	
	/**
	 * Remove entity from spawn list for all biomes except Hell.
	 * @param entityName
	 * @param spawnGroup
	 */
	public static void RemoveSpawn(String entityName, SpawnGroup spawnGroup) {
		RemoveSpawn(entityName, spawnGroup, (Biome) null);
	}
	
	/**
	 * Remove entity from spawn list for selected biomes.
	 * @param entityName
	 * @param spawnGroup
	 * @param biomes
	 */
	@SuppressWarnings("unchecked")
	public static void RemoveSpawn(String entityName, SpawnGroup spawnGroup, Biome... biomes) {
		Class<? extends Entity> entityClass = EntityRegistryAccessor.getSTRING_ID_TO_CLASS().get(entityName);
		if (entityClass != null && LivingEntity.class.isAssignableFrom(entityClass)) {
			RemoveSpawn((Class<? extends LivingEntity>) entityClass, spawnGroup, biomes);
		}
	}
	
	/**
	 * Determines how the block should be rendered.
	 * @param modelID
	 * @return
	 */
	public static boolean RenderBlockIsItemFull3D(int modelID) {
		if (!blockSpecialInv.containsKey(modelID)) {
			return modelID == 16;
		} else {
			return blockSpecialInv.get(modelID);
		}
	}
	
	/**
	 * Renders a block in inventory.
	 * @param renderer
	 * @param block
	 * @param metadata
	 * @param modelID
	 */
	public static void RenderInvBlock(BlockRenderer renderer, Block block, int metadata, int modelID) {
		BaseMod mod = blockModels.get(modelID);
		if (mod != null) {
			mod.RenderInvBlock(renderer, block, metadata, modelID);
		}
	}
	
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
	public static boolean RenderWorldBlock(BlockRenderer renderer, BlockView world, int x, int y, int z, Block block, int modelID) {
		BaseMod mod = (BaseMod)blockModels.get(modelID);
		return mod != null && mod.RenderWorldBlock(renderer, world, x, y, z, block, modelID);
	}
	
	/**
	 * Saves props to the config file.
	 * @throws IOException
	 */
	public static void saveConfig() throws IOException {
		cfgdir.mkdir();
		if (cfgfile.exists() || cfgfile.createNewFile()) {
			if (cfgfile.canWrite()) {
				OutputStream out = Files.newOutputStream(cfgfile.toPath());
				props.store(out, "ModLoader Config");
				out.close();
			}
		}
	}
	
	/**
	 * Enable or disable BaseMod.OnTickInGame(net.minecraft.client.Minecraft)
	 * @param mod
	 * @param enable
	 * @param useClock
	 */
	public static void SetInGameHook(BaseMod mod, boolean enable, boolean useClock) {
		if (enable) {
			inGameHooks.put(mod, useClock);
		} else {
			inGameHooks.remove(mod);
		}
	}
	
	/**
	 * Enable or disable BaseMod.OnTickInGUI(net.minecraft.client.Minecraft, da)
	 * @param mod
	 * @param enable
	 * @param useClock
	 */
	public static void SetInGUIHook(BaseMod mod, boolean enable, boolean useClock) {
		if (enable) {
			inGUIHooks.put(mod, useClock);
		} else {
			inGUIHooks.remove(mod);
		}
	}
	
	/**
	 * Used for setting value of private fields.
	 * @param instanceClass
	 * @param instance
	 * @param fieldIndex
	 * @param value
	 * @param <T>
	 * @param <E>
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 */
	public static <T, E> void setPrivateValue(Class<? super T> instanceClass, T instance, int fieldIndex, E value) throws IllegalArgumentException, SecurityException {
		try {
			Field f = instanceClass.getDeclaredFields()[fieldIndex];
			f.setAccessible(true);
			int modifiers = field_modifiers.getInt(f);
			if ((modifiers & 16) != 0) {
				field_modifiers.setInt(f, modifiers & -17);
			}

			f.set(instance, value);
		} catch (IllegalAccessException var6) {
			logger.throwing("ModLoader", "setPrivateValue", var6);
			ThrowException("An impossible error has occured!", var6);
		}

	}
	
	/**
	 * Used for setting value of private fields.
	 * @param instanceClass
	 * @param instance
	 * @param fieldName
	 * @param value
	 * @param <T>
	 * @param <E>
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static <T, E> void setPrivateValue(Class<? super T> instanceClass, T instance, String fieldName, E value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = instanceClass.getDeclaredField(fieldName);
			int modifiers = field_modifiers.getInt(f);
			if ((modifiers & 16) != 0) {
				field_modifiers.setInt(f, modifiers & -17);
			}

			f.setAccessible(true);
			f.set(instance, value);
		} catch (IllegalAccessException var6) {
			logger.throwing("ModLoader", "setPrivateValue", var6);
			ThrowException("An impossible error has occured!", var6);
		}

	}

	private static void setupProperties(Class<? extends BaseMod> mod) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException {
		Properties modprops = new Properties();
		File modcfgfile = new File(cfgdir, mod.getName() + ".cfg");
		if (modcfgfile.exists() && modcfgfile.canRead()) {
			modprops.load(Files.newInputStream(modcfgfile.toPath()));
		}

		StringBuilder helptext = new StringBuilder();

		Field[] var7;
		for(Field field : var7 = mod.getFields()) {
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
					String strvalue = modprops.getProperty(key);
					Object value = null;
					if (type.isAssignableFrom(String.class)) {
						value = strvalue;
					} else if (type.isAssignableFrom(Integer.TYPE)) {
						value = Integer.parseInt(strvalue);
					} else if (type.isAssignableFrom(Short.TYPE)) {
						value = Short.parseShort(strvalue);
					} else if (type.isAssignableFrom(Byte.TYPE)) {
						value = Byte.parseByte(strvalue);
					} else if (type.isAssignableFrom(Boolean.TYPE)) {
						value = Boolean.parseBoolean(strvalue);
					} else if (type.isAssignableFrom(Float.TYPE)) {
						value = Float.parseFloat(strvalue);
					} else if (type.isAssignableFrom(Double.TYPE)) {
						value = Double.parseDouble(strvalue);
					}

					if (value != null) {
						if (value instanceof Number) {
							double num = ((Number)value).doubleValue();
							if (annotation.min() != Double.NEGATIVE_INFINITY && num < annotation.min()
									|| annotation.max() != Double.POSITIVE_INFINITY && num > annotation.max()) {
								continue;
							}
						}

						logger.finer(key + " set to " + value);
						if (!value.equals(currentvalue)) {
							field.set(null, value);
						}
					}
				} else {
					logger.finer(key + " not in config, using default: " + currentvalue);
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
	 * @param player
	 * @param item
	 */
	public static void TakenFromCrafting(PlayerEntity player, ItemStack item) {
		for(BaseMod mod : modList) {
			mod.TakenFromCrafting(player, item);
		}
	}
	
	/**
	 * Is called when an item is picked up from furnace result slot.
	 * @param player
	 * @param item
	 */
	public static void TakenFromFurnace(PlayerEntity player, ItemStack item) {
		for(BaseMod mod : modList) {
			mod.TakenFromFurnace(player, item);
		}
	}
	
	/**
	 * Used for catching an error and generating an error report.
	 * @param message
	 * @param e
	 */
	public static void ThrowException(String message, Throwable e) {
		Minecraft game = getMinecraftInstance();
		if (game != null) {
			game.showGameStartupError(new GameStartupError(message, e));
		} else {
			throw new RuntimeException(e);
		}
	}

	private static void ThrowException(Throwable e) {
		ThrowException("Exception occured in ModLoader", e);
	}

	private ModLoader() {
	}
}
