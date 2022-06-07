package modloader;

import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.block.BlockEntityRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Session;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.stat.Stat;
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
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ModLoader {
//	private static final List<TextureBinder> animList = new LinkedList<>();
	private static final Map<Integer, BaseMod> blockModels = new HashMap<>();
	private static final Map<Integer, Boolean> blockSpecialInv = new HashMap<>();
	private static final File cfgdir = new File(Minecraft.getGameDirectory(), "/config/");
	private static final File cfgfile = new File(cfgdir, "ModLoader.cfg");
	public static Level cfgLoggingWorld = Level.FINER;
	private static long clock = 0L;
	private static Field field_blockList = null;
	private static Field field_modifiers = null;
	private static boolean hasInit = false;
	private static int highestEntityId = 3000;
	private static final Map<BaseMod, Boolean> inGameHooks = new HashMap<>();
	private static final Map<BaseMod, Boolean> inGUIHooks = new HashMap<>();
	private static Minecraft instance = null;
	private static final Map<BaseMod, Map<KeyBinding, boolean[]>> keyList = new HashMap<>();
	private static final File LOG_FILE = new File(Minecraft.getGameDirectory(), "ModLoader.txt");
	private static final Logger LOGGER = Logger.getLogger("ModLoader");
	private static FileHandler logHandler = null;
	private static final LinkedList<BaseMod> modList = new LinkedList<>();
	private static int nextBlockModelID = 1000;
//	private static final Map<Integer, Map<String, Integer>> overrides = new HashMap<>();
	public static final Properties props = new Properties();
	private static Biome[] standardBiomes;
	private static String texPack = null;
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
				String[] nameParts = achievement.name.split("\\.");
				if (nameParts.length == 2) {
					String modID = nameParts[1];
					AddLocalization("achievement." + modID, name);
					AddLocalization("achievement." + modID + ".desc", description);
					setPrivateValue(Stat.class, achievement, 1, TranslationStorage.getInstance().translate("achievement." + modID));
					setPrivateValue(Achievement.class, achievement, 3, TranslationStorage.getInstance().translate("achievement." + modID + ".desc"));
				}
				else {
					setPrivateValue(Stat.class, achievement, 1, name);
					setPrivateValue(Achievement.class, achievement, 3, description);
				}
			}
			else {
				setPrivateValue(Stat.class, achievement, 1, name);
				setPrivateValue(Achievement.class, achievement, 3, description);
			}
		}
		catch (IllegalArgumentException | NoSuchFieldException | SecurityException v1) {
			LOGGER.throwing("ModLoader", "AddAchievementDesc", v1);
			ThrowException(v1);
		}
	}
	
	/**
	 * Used for adding new sources of fuel to the furnace.
	 * @param itemId the item to be used as fuel.
	 * @return the fuel ID assigned to the item.
	 */
	public static int AddAllFuel(int itemId) {
		LOGGER.finest("Finding fuel for " + itemId);
		int fuelID = 0;
		for (Iterator<BaseMod> iterator = modList.iterator(); iterator.hasNext() && fuelID == 0; ) {
			fuelID = iterator.next().AddFuel(itemId);
		}
		if (fuelID != 0) {
			LOGGER.finest("Returned " + fuelID);
		}
		return fuelID;
	}
	
	/**
	 * Used to add all mod entity renderers.
	 * @param rendererMap
	 */
	public static void AddAllRenderers(Map<Class<? extends Entity>, EntityRenderer> rendererMap) {
		for (BaseMod mod : modList) {
			mod.AddRenderer(rendererMap);
		}
	}
	
	/**
	 * Registers one animation instance.
	 * @param animation
	 */
	public static void addAnimation(TextureBinder animation) {
		LOGGER.finest("Adding animation " + animation.toString());
//		TexturesManager.addAnimation(animation); // TODO
	}
	
	/**
	 * Use this when you need the player to have new armor skin.
	 * @param armor
	 * @return
	 */
	public static int AddArmor(String armor) {
		try {
			String[] parts = (String[]) PlayerRenderer.armorTypes;
			List<String> stringList = Arrays.asList(parts);
			List<String> resultList = new ArrayList<>(stringList);
			if (!resultList.contains(armor)) {
				resultList.add(armor);
			}
			int armorIndex = resultList.indexOf(armor);
			PlayerRenderer.armorTypes = resultList.toArray(new String[0]);
			return armorIndex;
		}
		catch (IllegalArgumentException e) {
			LOGGER.throwing("ModLoader", "AddArmor", e);
			ThrowException("An impossible error has occurred!", e);
		}
		return -1;
	}
	
	/**
	 * Method for adding raw strings to the translation table.
	 * @param key
	 * @param value
	 */
	public static void AddLocalization(String key, String value) {
		Properties properties = null;
		try {
			properties = getPrivateValue(TranslationStorage.class, TranslationStorage.getInstance(), 1);
		}
		catch (SecurityException | NoSuchFieldException e) {
			LOGGER.throwing("ModLoader", "AddLocalization", e);
			ThrowException(e);
		}
		if (properties != null) {
			properties.put(key, value);
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

			Package pack = ModLoader.class.getPackage();
			if (pack != null) {
				name = pack.getName() + "." + name;
			}

			Class<?> modClass = loader.loadClass(name);
			if (!BaseMod.class.isAssignableFrom(modClass)) {
				return;
			}

			setupProperties((Class<? extends BaseMod>) modClass);
			BaseMod mod = (BaseMod)modClass.newInstance();
			if (mod != null) {
				modList.add(mod);
				LOGGER.fine("Mod Loaded: \"" + mod.toString() + "\" from " + filename);
				LOGGER.info("Mod Loaded: " + mod.toString());
			}
		} catch (Throwable var6) {
			LOGGER.fine("Failed to load mod from \"" + filename + "\"");
			LOGGER.throwing("ModLoader", "addMod", var6);
			ThrowException(var6);
		}
	}

	/**
	 * This method will allow adding name to item in inventory.
	 * @param instance
	 * @param name
	 */
	public static void AddName(Object instance, String name) {
		String translationKey = null;
		if (instance instanceof Item) {
			Item item = (Item) instance;
			if (item.getTranslationKey() != null) {
				translationKey = item.getTranslationKey() + ".name";
			}
		}
		else if (instance instanceof Block) {
			Block block = (Block) instance;
			if (block.getTranslationKey() != null) {
				translationKey = block.getTranslationKey() + ".name";
			}
		}
		else if (instance instanceof ItemStack) {
			ItemStack item = (ItemStack) instance;
			if (item.getTranslationKey() != null) {
				translationKey = item.getTranslationKey() + ".name";
			}
		}
		else {
			Exception e = new Exception(instance.getClass().getName() + " cannot have name attached to it!");
			LOGGER.throwing("ModLoader", "AddName", e);
			ThrowException(e);
		}
		if (translationKey != null) {
			AddLocalization(translationKey, name);
		}
		else {
			Exception e = new Exception(instance + " is missing name tag!");
			LOGGER.throwing("ModLoader", "AddName", e);
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
			int spriteIndex = -1;
			if (fileToOverride.equals("/terrain.png")) {
//				spriteIndex = TexturesManager.getBlockTexture(fileToAdd); // TODO
			}
			else if (fileToOverride.equals("/gui/items.png")) {
//				spriteIndex = TexturesManager.getBlockTexture(fileToAdd);
			}
			addOverride(fileToOverride, fileToAdd, spriteIndex);
			return spriteIndex;
		}
		catch (Throwable t) {
			LOGGER.throwing("ModLoader", "addOverride", t);
			ThrowException(t);
			throw new RuntimeException(t);
		}
	}

	/**
	 * Registers one texture override to be done.
	 * @param path
	 * @param overlayPath
	 * @param index
	 */
	public static void addOverride(String path, String overlayPath, int index) {
		// TODO: Textures manager
		if (path.equals("/terrain.png")) {
//			TexturesManager.setBlockTexture(index, overlayPath); // TODO
		}
		else if (path.equals("/gui/items.png")) {
//			TexturesManager.setItemTexture(index, overlayPath); // TODO
		}
	}

	/**
	 * Gets next available index for this sprite map.
	 * @param path
	 * @return
	 */
	public static int getUniqueSpriteIndex(String path) {
		if (path.equals("/gui/items.png")) {
//			return TexturesManager.pollItemTextureID(); // TODO
		}
		if (path.equals("/terrain.png")) {
//			return TexturesManager.pollBlockTextureID(); // TODO
		}
		Exception v1 = new Exception("No registry for this texture: " + path);
		LOGGER.throwing("ModLoader", "getUniqueItemSpriteIndex", v1);
		ThrowException(v1);
		return 0;
	}

	/**
	 * Add recipe to crafting list.
	 * @param output
	 * @param ingredients
	 */
	public static void AddRecipe(ItemStack output, Object... ingredients) {
		RecipeRegistry.getInstance().addShapedRecipe(output, ingredients);
	}

	/**
	 * Add recipe to crafting list.
	 * @param output
	 * @param ingredients
	 */
	public static void AddShapelessRecipe(ItemStack output, Object... ingredients) {
		RecipeRegistry.getInstance().addShapelessRecipe(output, ingredients);
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
		AddSpawn(entityClass, weightedProb, spawnGroup, (Biome[]) null);
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
		}
		if (spawnGroup == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		}
		if (biomes == null) {
			biomes = standardBiomes;
		}
		for (Biome biome : biomes) {
			@SuppressWarnings("unchecked")
			List<EntityEntry> spawnList = biome.getSpawnList(spawnGroup);
			if (spawnList != null) {
				boolean hasEntry = false;
				for (EntityEntry entry : spawnList) {
					if (entry.entryClass == entityClass) {
						entry.rarity = weightedProb;
						hasEntry = true;
						break;
					}
				}
				if (!hasEntry) {
					spawnList.add(new EntityEntry(entityClass, weightedProb));
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
		AddSpawn(entityName, weightedProb, spawnGroup, (Biome[]) null);
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
		Class<? extends Entity> entityClass = (Class<? extends Entity>) EntityRegistry.STRING_ID_TO_CLASS.get(entityName);
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
		boolean dispensed = false;
		for (Iterator<BaseMod> iterator = modList.iterator(); iterator.hasNext() && !dispensed; ) {
			dispensed = iterator.next().DispenseEntity(world, x, y, z, xVel, zVel, stack);
		}
		return dispensed;
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
		return LOGGER;
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
			Field field = instanceClass.getDeclaredFields()[fieldIndex];
			field.setAccessible(true);
			return (T) field.get(instance);
		}
		catch (IllegalAccessException e) {
			LOGGER.throwing("ModLoader", "getPrivateValue", e);
			ThrowException("An impossible error has occurred!", e);
		}
		return null;
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
	public static <T, E> T getPrivateValue(Class<? super E> instanceClass, E instance, String fieldName) throws IllegalArgumentException, SecurityException, NoSuchFieldException, IllegalAccessException {
		try {
			Field f = instanceClass.getDeclaredField(fieldName);
			f.setAccessible(true);
			return (T) f.get(instance);
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, "There was a problem getting field %s from %s", new Object[]{fieldName, instanceClass.getName()});
			throw e;
		}
	}
	
	/**
	 * Assigns a model id for blocks to use for the given mod.
	 * @param mod
	 * @param full3DItem
	 * @return
	 */
	public static int getUniqueBlockModelID(BaseMod mod, boolean full3DItem) {
		int index = nextBlockModelID++;
		blockModels.put(index, mod);
		blockSpecialInv.put(index, full3DItem);
		return index;
	}
	
	/**
	 * Gets next Entity ID to use.
	 * @return
	 */
	public static int getUniqueEntityId() {
		return highestEntityId++;
	}
	
	public static void init() {
		if (hasInit) {
			return;
		}
		
		hasInit = true;
		
		try {
			instance = getMinecraftInstance();
			field_modifiers = Field.class.getDeclaredField("modifiers");
			field_modifiers.setAccessible(true);
			field_blockList = Session.class.getDeclaredFields()[0];
			field_blockList.setAccessible(true);

			Field[] biomeFields = Biome.class.getDeclaredFields();
			
			List<Biome> biomeList = new LinkedList<>();
			for (Field biomeField : biomeFields) {
				Class<?> type = biomeField.getType();
				if (Modifier.isStatic(biomeField.getModifiers()) && type.isAssignableFrom(Biome.class)) {
					Biome biome = (Biome) biomeField.get(null);
					if (!(biome instanceof HellBiome) && !(biome instanceof SkyBiome)) {
						biomeList.add(biome);
					}
				}
			}
			standardBiomes = biomeList.toArray(new Biome[0]);
//			ModsStorage.loadingMod = null;
		}
		catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
			LOGGER.throwing("ModLoader", "init", e);
			ThrowException(e);
			throw new RuntimeException(e);
		}
		try {
			loadConfig();
			if (props.containsKey("loggingWorld")) {
				cfgLoggingWorld = Level.parse(props.getProperty("loggingLevel"));
			}
			LOGGER.setLevel(cfgLoggingWorld);
			if (((LOG_FILE.exists()) || (LOG_FILE.createNewFile())) && (LOG_FILE.canWrite()) && (logHandler == null)) {
				logHandler = new FileHandler(LOG_FILE.getPath());
				logHandler.setFormatter(new SimpleFormatter());
				LOGGER.addHandler(logHandler);
			}
			LOGGER.fine(VERSION + " Initializing...");

//			readFromModFolder(); // TODO
			LOGGER.info("Done.");
			
			props.setProperty("loggingWorld", cfgLoggingWorld.getName());
			for (BaseMod mod : modList) {
				mod.ModsLoaded();
				if (!props.containsKey(mod.getClass().getName())) {
					props.setProperty(mod.getClass().getName(), "on");
				}
			}
			LOGGER.info("Instance: " + instance);
			instance.options.keyBindings = RegisterAllKeys(instance.options.keyBindings);
			instance.options.load();
			
			initStats();
			
			saveConfig();
		}
		catch (Throwable t) {
			LOGGER.throwing("ModLoader", "init", t);
			ThrowException("ModLoader has failed to initialize.", t);
			if (logHandler != null) {
				logHandler.close();
			}
			throw new RuntimeException(t);
		}
		
		LOGGER.fine("Initialized");
	}

	@SuppressWarnings("unchecked")
	private static void initStats() {
		Map<Integer, Stat> map = Stats.idMap;
		for (int i = 0; i < Block.BY_ID.length; i++) {
			if ((!map.containsKey(16777216 + i)) && (Block.BY_ID[i] != null) && (Block.BY_ID[i].isStatEnabled())) {
				String v2 = TranslationStorage.getInstance().translate("stat.mineBlock", Block.BY_ID[i].getTranslatedName());
				Stats.mineBlock[i] = new StatEntity(16777216 + i, v2, i).register();
				Stats.blocksMinedList.add(Stats.mineBlock[i]);
			}
		}
		for (int i = 0; i < Item.byId.length; i++) {
			if ((!map.containsKey(16908288 + i)) && (Item.byId[i] != null)) {
				String v2 = TranslationStorage.getInstance().translate("stat.useItem", Item.byId[i].getTranslatedName());
				Stats.useItem[i] = new StatEntity(16908288 + i, v2, i).register();
				if (i >= Block.BY_ID.length) {
					Stats.useStatList.add(Stats.useItem[i]);
				}
			}
			if ((!map.containsKey(16973824 + i)) && (Item.byId[i] != null) && (Item.byId[i].hasDurability())) {
				String v2 = TranslationStorage.getInstance().translate("stat.breakItem", Item.byId[i].getTranslatedName());
				Stats.breakItem[i] = new StatEntity(16973824 + i, v2, i).register();
			}
		}
		HashSet<Integer> idMap = new HashSet<>();
		for (Object recipe : RecipeRegistry.getInstance().getRecipes()) {
			idMap.add(((Recipe) recipe).getOutput().itemId);
		}
		for (Object item : SmeltingRecipeRegistry.getInstance().getRecipes().values()) {
			idMap.add(((ItemStack) item).itemId);
		}
		for (int id : idMap) {
			if (!map.containsKey(16842752 + id) && Item.byId[id] != null) {
				String v4 = TranslationStorage.getInstance().translate("stat.craftItem", Item.byId[id].getTranslatedName());
				Stats.timesCrafted[id] = new StatEntity(16842752 + id, v4, id).register();
			}
		}
	}
	
	/**
	 * Use this method to check if GUI is opened for the player.
	 * @param gui
	 * @return
	 */
	public static boolean isGUIOpen(Class<? extends Screen> gui) {
		Minecraft minecraft = getMinecraftInstance();
		if (gui == null) {
			return minecraft.currentScreen == null;
		}
		if (minecraft.currentScreen == null) {
			return false;
		}
		return gui.isInstance(minecraft.currentScreen);
	}
	
	/**
	 * Checks if a mod is loaded.
	 * @param modName
	 * @return
	 */
	public static boolean isModLoaded(String modName) {
		Class<?> modClass;
		try {
			modClass = Class.forName(modName);
		}
		catch (ClassNotFoundException v2) {
			return false;
		}
		for (BaseMod mod : modList) {
			if (modClass.isInstance(mod)) {
				return true;
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
		if (!cfgfile.exists() && !cfgfile.createNewFile()) {
			return;
		}
		if (cfgfile.canRead()) {
			InputStream v1 = new FileInputStream(cfgfile);
			props.load(v1);
			v1.close();
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
		TexturePackManager manager = getPrivateValue(TextureManager.class, textureManager, 11);
		InputStream stream = manager.texturePack.getResourceAsStream(path);
		if (stream == null) {
			throw new Exception("Image not found: " + path);
		}
		BufferedImage image = ImageIO.read(stream);
		if (image == null) {
			throw new Exception("Image corrupted: " + path);
		}
		return image;
	}
	
	/**
	 * Is called when an item is picked up from the world.
	 * @param a1
	 * @param a2
	 */
	public static void OnItemPickup(PlayerEntity a1, ItemStack a2) {
		for (BaseMod v1 : modList) {
			v1.OnItemPickup(a1, a2);
		}
	}
	
	/**
	 * This method is called every tick while minecraft is running.
	 * @param minecraft
	 */
	public static void OnTick(Minecraft minecraft) {
		if (ModLoader.texPack == null || minecraft.options.skin != ModLoader.texPack) {
			ModLoader.texPack = minecraft.options.skin;
		}
		long time = 0L;
		if (minecraft.world != null) {
			time = minecraft.world.getWorldTime();
			final Iterator<Entry<BaseMod, Boolean>> iterator = ModLoader.inGameHooks.entrySet().iterator();
			while (iterator.hasNext()) {
				final Entry<BaseMod, Boolean> entry = iterator.next();
				if (ModLoader.clock == time && entry.getValue()) {
					continue;
				}
				if (entry.getKey().OnTickInGame(minecraft)) {
					continue;
				}
				iterator.remove();
			}
		}
		if (minecraft.currentScreen != null) {
			final Iterator<Entry<BaseMod, Boolean>> iterator = ModLoader.inGUIHooks.entrySet().iterator();
			while (iterator.hasNext()) {
				final Entry<BaseMod, Boolean> entry = iterator.next();
				if (ModLoader.clock == time && (entry.getValue() & minecraft.world != null)) {
					continue;
				}
				if (entry.getKey().OnTickInGUI(minecraft, minecraft.currentScreen)) {
					continue;
				}
				iterator.remove();
			}
		}
		if (ModLoader.clock != time) {
			for (final Entry<BaseMod, Map<KeyBinding, boolean[]>> keySet : ModLoader.keyList.entrySet()) {
				for (final Entry<KeyBinding, boolean[]> entry : keySet.getValue().entrySet()) {
					final boolean isDown = Keyboard.isKeyDown(entry.getKey().key);
					final boolean[] value = entry.getValue();
					final boolean compare = value[1];
					value[1] = isDown;
					if (isDown) {
						if (compare && !value[0]) {
							continue;
						}
						keySet.getKey().KeyboardEvent(entry.getKey());
					}
				}
			}
		}
		ModLoader.clock = time;
	}
	
	/**
	 * Opens GUI for use with mods.
	 * @param player
	 * @param screen
	 */
	public static void OpenGUI(PlayerEntity player, Screen screen) {
		Minecraft minecraft = getMinecraftInstance();
		if (minecraft.player != player) {
			return;
		}
		if (screen != null) {
			minecraft.openScreen(screen);
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
		Random random = new Random(world.getSeed());
		long offsetX = random.nextLong() / 2L * 2L + 1L;
		long offsetZ = random.nextLong() / 2L * 2L + 1L;
		random.setSeed(chunkX * offsetX + chunkZ * offsetZ ^ world.getSeed());
		for (BaseMod mod : modList) {
			if (worldSource.toString().equals("RandomWorldSource")) {
				mod.GenerateSurface(world, random, chunkX << 4, chunkZ << 4);
			}
			else if (worldSource.toString().equals("HellRandomWorldSource")) {
				mod.GenerateNether(world, random, chunkX << 4, chunkZ << 4);
			}
		}
	}
	
	/**
	 * Appends all mod key handlers to the given array and returns it.
	 * @param keyBindings
	 * @return
	 */
	public static KeyBinding[] RegisterAllKeys(KeyBinding[] keyBindings) {
		List<KeyBinding> v1 = new LinkedList<>(Arrays.asList(keyBindings));
		for (Map<KeyBinding, boolean[]> v2 : keyList.values()) {
			v1.addAll(v2.keySet());
		}
		return v1.toArray(new KeyBinding[0]);
	}
	
	/**
	 * Processes all registered texture overrides.
	 * @param manager
	 */
	public static void RegisterAllTextureOverrides(TextureManager manager) {
		/*animList.clear();
		Minecraft minecraft = getMinecraftInstance();
		for (final BaseMod mod : modList) {
			mod.RegisterAnimation(minecraft);
		}
		for (TextureBinder textureBinder : animList) {
			manager.addTextureBinder(textureBinder);
		}
		for (final Map.Entry<Integer, Map<String, Integer>> override : overrides.entrySet()) {
			for (final Map.Entry<String, Integer> entry : override.getValue().entrySet()) {
				final String key = entry.getKey();
				final int textureIndex = entry.getValue();
				final int textureType = override.getKey();
				try {
					final BufferedImage image = loadImage(manager, key);
					final TextureBinder textureStatic = new ModTextureStatic(textureIndex, textureType, image);
					manager.addTextureBinder(textureStatic);
				}
				catch (Exception e) {
					ModLoader.logger.throwing("ModLoader", "RegisterAllTextureOverrides", e);
					ThrowException(e);
					throw new RuntimeException(e);
				}
			}
		}*/
	}
	
	/**
	 * Adds block to list of blocks the player can use.
	 * @param a1
	 */
	public static void RegisterBlock(Block a1) {
		RegisterBlock(a1, null);
	}
	
	/**
	 * Adds block to list of blocks the player can use.
	 * @param block
	 * @param itemClass
	 */
	public static void RegisterBlock(Block block, Class<? extends Item> itemClass) {
		try {
			if (block == null) {
				throw new IllegalArgumentException("block parameter cannot be null.");
			}
			@SuppressWarnings("unchecked")
			List<Block> v1 = (List<Block>) field_blockList.get(null);
			v1.add(block);
			
			Item item;
			int blockID = block.id;
			if (itemClass != null) {
				item = itemClass.getConstructor(Integer.TYPE).newInstance(blockID - 256);
			}
			else {
				item = new Item(blockID - 256);
			}
			if ((Block.BY_ID[blockID] != null) && (Item.byId[blockID] == null)) {
				Item.byId[blockID] = item;
			}
		}
		catch (IllegalArgumentException | NoSuchMethodException | InvocationTargetException | InstantiationException | SecurityException | IllegalAccessException e) {
			LOGGER.throwing("ModLoader", "RegisterBlock", e);
			ThrowException(e);
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
			EntityRegistry.register(entityClass, entityName, id);
		}
		catch (IllegalArgumentException e) {
			LOGGER.throwing("ModLoader", "RegisterEntityID", e);
			ThrowException(e);
		}
	}
	
	/**
	 * Use this to add an assignable key to the options menu.
	 * @param mod
	 * @param keyBinding
	 * @param allowRepeat
	 */
	public static void RegisterKey(BaseMod mod, KeyBinding keyBinding, boolean allowRepeat) {
		Map<KeyBinding, boolean[]> keyBindingMap = keyList.get(mod);
		if (keyBindingMap == null) {
			keyBindingMap = new HashMap<>();
		}
		keyBindingMap.put(keyBinding, new boolean[] {allowRepeat, false});
		keyList.put(mod, keyBindingMap);
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
			BlockEntity.register(blockEntityClass, id);
			if (renderer != null) {
				BlockEntityRenderDispatcher dispatcher = BlockEntityRenderDispatcher.INSTANCE;
				@SuppressWarnings("unchecked")
				Map<Class<? extends BlockEntity>, BlockEntityRenderer> v2 = (Map<Class<? extends BlockEntity>, BlockEntityRenderer>) dispatcher.customRenderers;
				v2.put(blockEntityClass, renderer);
				renderer.setRenderDispatcher(dispatcher);
			}
		}
		catch (IllegalArgumentException e) {
			LOGGER.throwing("ModLoader", "RegisterTileEntity", e);
			ThrowException(e);
		}
	}
	
	/**
	 * Remove entity from spawn list for all biomes except Hell.
	 * @param entityClass
	 * @param spawnGroup
	 */
	public static void RemoveSpawn(Class<? extends LivingEntity> entityClass, SpawnGroup spawnGroup) {
		RemoveSpawn(entityClass, spawnGroup, (Biome[]) null);
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
		}
		if (spawnGroup == null) {
			throw new IllegalArgumentException("spawnGroup cannot be null");
		}
		if (biomes == null) {
			biomes = standardBiomes;
		}
		for (Biome biome : biomes) {
			@SuppressWarnings("unchecked")
			List<EntityEntry> spawnList = biome.getSpawnList(spawnGroup);
			if (spawnList != null) {
				spawnList.removeIf(entry -> entry.entryClass == entityClass);
			}
		}
	}
	
	/**
	 * Remove entity from spawn list for all biomes except Hell.
	 * @param entityName
	 * @param spawnGroup
	 */
	public static void RemoveSpawn(String entityName, SpawnGroup spawnGroup) {
		RemoveSpawn(entityName, spawnGroup, (Biome[]) null);
	}
	
	/**
	 * Remove entity from spawn list for selected biomes.
	 * @param entityName
	 * @param spawnGroup
	 * @param biomes
	 */
	@SuppressWarnings("unchecked")
	public static void RemoveSpawn(String entityName, SpawnGroup spawnGroup, Biome... biomes) {
		Class<? extends Entity> entityClass = (Class<? extends Entity>) EntityRegistry.STRING_ID_TO_CLASS.get(entityName);
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
		}
		return blockSpecialInv.get(modelID);
	}
	
	/**
	 * Renders a block in inventory.
	 * @param blockRenderer
	 * @param block
	 * @param meta
	 * @param modelID
	 */
	public static void RenderInvBlock(BlockRenderer blockRenderer, Block block, int meta, int modelID) {
		BaseMod mod = blockModels.get(modelID);
		if (mod == null) {
			return;
		}
		mod.RenderInvBlock(blockRenderer, block, meta, modelID);
	}
	
	/**
	 * Renders a block in the world.
	 * @param blockRenderer
	 * @param view
	 * @param x
	 * @param y
	 * @param z
	 * @param block
	 * @param modelID
	 * @return
	 */
	public static boolean RenderWorldBlock(BlockRenderer blockRenderer, BlockView view, int x, int y, int z, Block block, int modelID) {
		BaseMod mod = blockModels.get(modelID);
		if (mod == null) {
			return false;
		}
		return mod.RenderWorldBlock(blockRenderer, view, x, y, z, block, modelID);
	}
	
	/**
	 * Saves props to the config file.
	 * @throws IOException
	 */
	public static void saveConfig() throws IOException {
		cfgdir.mkdir();
		if ((!cfgfile.exists()) && (!cfgfile.createNewFile())) {
			return;
		}
		if (cfgfile.canWrite()) {
			OutputStream v1 = new FileOutputStream(cfgfile);
			props.store(v1, "ModLoader Config");
			v1.close();
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
		}
		else {
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
		}
		else {
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
	 * @throws NoSuchFieldException
	 */
	public static <T, E> void setPrivateValue(Class<? super T> instanceClass, T instance, int fieldIndex, E value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field field = instanceClass.getDeclaredFields()[fieldIndex];
			field.setAccessible(true);
			int modifier = field_modifiers.getInt(field);
			if (Modifier.isFinal(modifier)) {
				field_modifiers.setInt(field, modifier & 0xFFFFFFEF);
			}
			field.set(instance, value);
		}
		catch (IllegalAccessException e) {
			LOGGER.throwing("ModLoader", "setPrivateValue", e);
			ThrowException("An impossible error has occurred!", e);
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
			Field field = instanceClass.getDeclaredField(fieldName);
			int modifier = field_modifiers.getInt(field);
			if (Modifier.isFinal(modifier)) {
				field_modifiers.setInt(field, modifier & 0xFFFFFFEF);
			}
			field.setAccessible(true);
			field.set(instance, value);
		}
		catch (IllegalAccessException e) {
			LOGGER.throwing("ModLoader", "setPrivateValue", e);
			ThrowException("An impossible error has occurred!", e);
		}
	}
	
	private static void setupProperties(Class<? extends BaseMod> modClass) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException {
		Properties properties = new Properties();
		
		File configFile = new File(cfgdir, modClass.getSimpleName() + ".cfg");
		if (configFile.exists() && configFile.canRead()) {
			properties.load(new FileInputStream(configFile));
		}
		StringBuilder builder = new StringBuilder();
		Field[] arrayOfField = modClass.getFields();
		for (Field field : arrayOfField) {
			if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(MLProp.class)) {
				Class<?> type = field.getType();
				MLProp annotation = field.getAnnotation(MLProp.class);
				String name = annotation.name().isEmpty() ? field.getName() : annotation.name();
				Object obj = field.get(null);

				StringBuilder builder1 = new StringBuilder();
				if (annotation.min() != Double.NEGATIVE_INFINITY) {
					builder1.append(String.format(",>=%.1f", annotation.min()));
				}
				if (annotation.max() != Double.POSITIVE_INFINITY) {
					builder1.append(String.format(",<=%.1f", annotation.max()));
				}
				StringBuilder builder2 = new StringBuilder();
				if (!annotation.info().isEmpty()) {
					builder2.append(" -- ");
					builder2.append(annotation.info());
				}
				builder.append(String.format("%s (%s:%s%s)%s\n", name, type.getName(), obj, builder1, builder2));
				if (properties.containsKey(name)) {
					String property = properties.getProperty(name);

					Object propertyValue = null;
					if (type.isAssignableFrom(String.class)) {
						propertyValue = property;
					} else if (type.isAssignableFrom(Integer.TYPE)) {
						propertyValue = Integer.parseInt(property);
					} else if (type.isAssignableFrom(Short.TYPE)) {
						propertyValue = Short.parseShort(property);
					} else if (type.isAssignableFrom(Byte.TYPE)) {
						propertyValue = Byte.parseByte(property);
					} else if (type.isAssignableFrom(Boolean.TYPE)) {
						propertyValue = Boolean.parseBoolean(property);
					} else if (type.isAssignableFrom(Float.TYPE)) {
						propertyValue = Float.parseFloat(property);
					} else if (type.isAssignableFrom(Double.TYPE)) {
						propertyValue = Double.parseDouble(property);
					}
					if (propertyValue != null) {
						if (propertyValue instanceof Number) {
							double doubleValue = ((Number) propertyValue).doubleValue();
							if (annotation.min() != Double.NEGATIVE_INFINITY && doubleValue < annotation.min()) {
								continue;
							}
							if (annotation.max() != Double.POSITIVE_INFINITY && doubleValue > annotation.max()) {
								continue;
							}
						} else {
							LOGGER.finer(name + " set to " + propertyValue);
							if (!propertyValue.equals(obj)) {
								field.set(null, propertyValue);
							}
						}
					}
				} else {
					LOGGER.finer(name + " not in config, using default: " + obj);
					properties.setProperty(name, obj.toString());
				}
			}
		}
		if (!properties.isEmpty() && (configFile.exists() || configFile.createNewFile()) && configFile.canWrite()) {
			properties.store(new FileOutputStream(configFile), builder.toString());
		}
	}
	
	/**
	 * Is called when an item is picked up from crafting result slot.
	 * @param playerBase
	 * @param itemInstance
	 */
	public static void TakenFromCrafting(PlayerEntity playerBase, ItemStack itemInstance) {
		for (BaseMod v1 : modList) {
			v1.TakenFromCrafting(playerBase, itemInstance);
		}
	}
	
	/**
	 * Is called when an item is picked up from furnace result slot.
	 * @param playerBase
	 * @param itemInstance
	 */
	public static void TakenFromFurnace(PlayerEntity playerBase, ItemStack itemInstance) {
		for (BaseMod v1 : modList) {
			v1.TakenFromFurnace(playerBase, itemInstance);
		}
	}
	
	/**
	 * Used for catching an error and generating an error report.
	 * @param message
	 * @param e
	 */
	public static void ThrowException(String message, Throwable e) {
		Minecraft minecraft = getMinecraftInstance();
		if (minecraft != null) {
			minecraft.showGameStartupError(new GameStartupError(message, e));
		}
		else {
			throw new RuntimeException(e);
		}
	}
	
	private static void ThrowException(Throwable a1) {
		ThrowException("Exception occurred in ModLoader", a1);
	}
}
