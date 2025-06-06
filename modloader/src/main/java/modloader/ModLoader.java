package modloader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;

import io.github.fabriccompatibilitylayers.modloader.ApronModLoader;
import io.github.fabriccompatibilitylayers.modloader.mixin.common.AchievementAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader.BlockRenderManagerAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.common.CraftingRecipeManagerAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.common.EntityRegistryAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader.MinecraftAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.common.StatAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.common.StatsAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader.TextureManagerAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader.TranslationStorageAccessor;
import io.github.fabriccompatibilitylayers.modloader.mixin.server.ServerPlayerEntityAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
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
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.EntitySpawnGroup;
import net.minecraft.world.biome.HellBiome;
import net.minecraft.world.biome.SkyBiome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.NetherChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import org.lwjgl.input.Keyboard;

public final class ModLoader {
	@Environment(EnvType.CLIENT)
	private static List<DynamicTexture> animList;
	@Environment(EnvType.CLIENT)
	private static Map<Integer, BaseMod> blockModels;
	@Environment(EnvType.CLIENT)
	private static Map<Integer, Boolean> blockSpecialInv;
	private static final File cfgdir = FabricLoader.getInstance().getConfigDir().toFile();
	private static final File cfgfile;
	public static Level cfgLoggingLevel;
	@Environment(EnvType.CLIENT)
	private static Map<String, Class<? extends Entity>> classMap;
	private static long clock;
	public static final boolean DEBUG = false;
	private static Field field_modifiers;
	private static boolean hasInit;
	private static int highestEntityId;
	private static final Map<BaseMod, Boolean> inGameHooks;
	@Environment(EnvType.CLIENT)
	private static Map<BaseMod, Boolean> inGUIHooks;
	@Environment(EnvType.CLIENT)
	private static Minecraft clientInstance;
	@Environment(EnvType.SERVER)
	private static MinecraftServer serverInstance;
	private static int itemSpriteIndex;
	private static int itemSpritesLeft;
	@Environment(EnvType.CLIENT)
	private static Map<BaseMod, Map<KeyBinding, boolean[]>> keyList;
	private static File logfile;
	private static final Logger logger;
	private static FileHandler logHandler;
	private static final File modDir;
	private static final LinkedList<BaseMod> modList;
	private static int nextBlockModelID;
	private static final Map<Integer, Map<String, Integer>> overrides;
	public static final Properties props;
	private static Biome[] standardBiomes;
	private static int terrainSpriteIndex;
	private static int terrainSpritesLeft;
	@Environment(EnvType.CLIENT)
	private static String texPack;
	@Environment(EnvType.CLIENT)
	private static boolean texturesAdded;
	private static final boolean[] usedItemSprites;
	private static final boolean[] usedTerrainSprites;
	public static final String VERSION;

	static {
		cfgfile = new File(cfgdir, "ModLoader.cfg");
		cfgLoggingLevel = Level.FINER;
		clock = 0L;
		field_modifiers = null;
		hasInit = false;
		highestEntityId = 3000;
		inGameHooks = new HashMap<>();
		itemSpriteIndex = 0;
		itemSpritesLeft = 0;
		logger = Logger.getLogger("ModLoader");
		logHandler = null;
		logfile = FabricLoader.getInstance().getGameDir().resolve("ModLoader.txt").toFile();
		modDir = FabricLoader.getInstance().getGameDir().resolve("mods").toFile();
		modList = new LinkedList<>();
		nextBlockModelID = 1000;
		overrides = new HashMap<>();
		props = new Properties();
		terrainSpriteIndex = 0;
		terrainSpritesLeft = 0;
		usedItemSprites = new boolean[256];
		usedTerrainSprites = new boolean[256];

		if (ApronModLoader.IS_CLIENT) {
			VERSION = "ModLoader Beta 1.7.3";
			animList = new LinkedList<>();
			blockModels = new HashMap<>();
			blockSpecialInv = new HashMap<>();
			classMap = null;
			inGUIHooks = new HashMap<>();
			clientInstance = null;
			keyList = new HashMap<>();
			texPack = null;
			texturesAdded = false;
		} else {
			VERSION = "ModLoader Server Beta 1.6.6v4";
		}
	}

	public static void AddAchievementDesc(Achievement achievement, String name, String description) {
		try {
			if (achievement.stringId.contains(".")) {
				String[] split = achievement.stringId.split("\\.");
				if (split.length == 2 || !ApronModLoader.IS_CLIENT) {
					String key = split[1];

					if (ApronModLoader.IS_CLIENT) {
						AddLocalization("achievement." + key, name);
						AddLocalization("achievement." + key + ".desc", description);
					}

					((StatAccessor) achievement).setStringId(translate("achievement." + key));
					((AchievementAccessor) achievement).setTranslationKey(translate("achievement." + key + ".desc"));
				} else {
					((StatAccessor) achievement).setStringId(name);
					((AchievementAccessor) achievement).setTranslationKey(description);
				}
			} else {
				((StatAccessor) achievement).setStringId(name);
				((AchievementAccessor) achievement).setTranslationKey(description);
			}
		} catch (IllegalArgumentException | SecurityException e) {
			logger.throwing("ModLoader", "AddAchievementDesc", e);
			ThrowException(e);
		}
	}

	public static int AddAllFuel(int id) {
		logger.finest("Finding fuel for " + id);
		int result = 0;

		for(Iterator<BaseMod> iter = modList.iterator(); iter.hasNext() && result == 0; result = iter.next().AddFuel(id)) {
		}

		if (result != 0) {
			logger.finest("Returned " + result);
		}

		return result;
	}

	@Environment(EnvType.CLIENT)
	public static void AddAllRenderers(Map<Class<? extends Entity>, EntityRenderer> o) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		for(BaseMod mod : modList) {
			mod.AddRenderer(o);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void addAnimation(DynamicTexture anim) {
		logger.finest("Adding animation " + anim.toString());

		for(DynamicTexture oldAnim : animList) {
			if (oldAnim.atlas == anim.atlas && oldAnim.sprite == anim.sprite) {
				animList.remove(anim);
				break;
			}
		}

		animList.add(anim);
	}

	public static int AddArmor(String armor) {
		if (!ApronModLoader.IS_CLIENT) return -1;

		try {
			List<String> existingArmorList = Arrays.asList(PlayerEntityRenderer.armorTextureNames);
			List<String> combinedList = new ArrayList<>(existingArmorList);
			if (!combinedList.contains(armor)) {
				combinedList.add(armor);
			}

			int index = combinedList.indexOf(armor);
			PlayerEntityRenderer.armorTextureNames = combinedList.toArray(new String[0]);
			return index;
		} catch (IllegalArgumentException e) {
			logger.throwing("ModLoader", "AddArmor", e);
			ThrowException("An impossible error has occured!", e);
		}

		return -1;
	}

	@Environment(EnvType.CLIENT)
	public static void AddLocalization(String key, String value) {
		((TranslationStorageAccessor) TranslationStorage.getInstance())
				.getTranslations()
				.put(key, value);
	}

	private static void addMod(ClassLoader loader, String filename) {
		try {
			String name = filename.replace("/", ".").replace("\\", ".").replace(".class", "");
			if (name.contains("$")) {
				return;
			}

			if (props.containsKey(name) && (props.getProperty(name).equalsIgnoreCase("no") || props.getProperty(name).equalsIgnoreCase("off"))) {
				return;
			}

			Class<?> instclass = loader.loadClass(name);
			if (!BaseMod.class.isAssignableFrom(instclass)) {
				return;
			}

			setupProperties((Class<? extends BaseMod>) instclass);
			BaseMod mod = (BaseMod) instclass.newInstance();
			if (mod != null) {
				modList.add(mod);
				logger.fine("Mod Loaded: \"" + mod + "\" from " + filename);
				System.out.println("Mod Loaded: " + mod);
				if (!ApronModLoader.IS_CLIENT) MinecraftServer.LOGGER.info("Mod Loaded: " + mod);
			}
		} catch (Throwable e) {
			logger.fine("Failed to load mod from \"" + filename + "\"");
			System.out.println("Failed to load mod from \"" + filename + "\"");
			logger.throwing("ModLoader", "addMod", e);
			ThrowException(e);
		}

	}

	@Environment(EnvType.CLIENT)
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

	public static int addOverride(String fileToOverride, String fileToAdd) {
		try {
			int i = getUniqueSpriteIndex(fileToOverride);
			addOverride(fileToOverride, fileToAdd, i);
			return i;
		} catch (Throwable e) {
			logger.throwing("ModLoader", "addOverride", e);
			ThrowException(e);
			throw new RuntimeException(e);
		}
	}

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

	public static void AddRecipe(ItemStack output, Object... params) {
		((CraftingRecipeManagerAccessor) CraftingRecipeManager.getInstance()).invokeAddShapedRecipe(output, params);
	}

	public static void AddShapelessRecipe(ItemStack output, Object... params) {
		((CraftingRecipeManagerAccessor) CraftingRecipeManager.getInstance()).invokeAddShapelessRecipe(output, params);
	}

	public static void AddSmelting(int input, ItemStack output) {
		SmeltingRecipeManager.getInstance().addRecipe(input, output);
	}

	public static void AddSpawn(Class<? extends LivingEntity> entityClass, int weightedProb, SpawnGroup spawnList) {
		AddSpawn(entityClass, weightedProb, spawnList, (Biome[])null);
	}

	public static void AddSpawn(Class<? extends LivingEntity> entityClass, int weightedProb, SpawnGroup spawnList, Biome... biomes) {
		if (entityClass == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if (spawnList == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if (biomes == null) {
				biomes = standardBiomes;
			}

			for(int i = 0; i < biomes.length; ++i) {
				List<EntitySpawnGroup> list = biomes[i].getSpawnableEntities(spawnList);
				if (list != null) {
					boolean exists = false;

					for(EntitySpawnGroup entry : list) {
						if (entry.clazz == entityClass) {
							entry.amount = weightedProb;
							exists = true;
							break;
						}
					}

					if (!exists) {
						list.add(new EntitySpawnGroup(entityClass, weightedProb));
					}
				}
			}

		}
	}

	public static void AddSpawn(String entityName, int weightedProb, SpawnGroup spawnList) {
		AddSpawn(entityName, weightedProb, spawnList, (Biome[])null);
	}

	public static void AddSpawn(String entityName, int weightedProb, SpawnGroup spawnList, Biome... biomes) {
		Class<? extends Entity> entityClass = classMap.get(entityName);
		if (entityClass != null && LivingEntity.class.isAssignableFrom(entityClass)) {
			AddSpawn((Class<? extends LivingEntity>) entityClass, weightedProb, spawnList, biomes);
		}

	}

	public static boolean DispenseEntity(World world, double x, double y, double z, int xVel, int zVel, ItemStack item) {
		boolean result = false;

		for(Iterator<BaseMod> iter = modList.iterator(); iter.hasNext() && !result; result = iter.next().DispenseEntity(world, x, y, z, xVel, zVel, item)) {
		}

		return result;
	}

	public static List<BaseMod> getLoadedMods() {
		return Collections.unmodifiableList(modList);
	}

	public static Logger getLogger() {
		return logger;
	}

	@Environment(EnvType.CLIENT)
	public static Minecraft getMinecraftInstance() {
		if (clientInstance == null) {
			clientInstance = (Minecraft) FabricLoader.getInstance().getGameInstance();
		}

		return clientInstance;
	}

	@Environment(EnvType.SERVER)
	public static MinecraftServer getMinecraftServerInstance() {
		return serverInstance;
	}

	public static <T, E> T getPrivateValue(Class<? super E> instanceclass, E instance, int fieldindex) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = instanceclass.getDeclaredFields()[fieldindex];
			f.setAccessible(true);
			return (T)f.get(instance);
		} catch (IllegalAccessException e) {
			logger.throwing("ModLoader", "getPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
			return null;
		}
	}

	public static <T, E> T getPrivateValue(Class<? super E> instanceclass, E instance, String field) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = instanceclass.getDeclaredField(field);
			f.setAccessible(true);
			return (T)f.get(instance);
		} catch (IllegalAccessException e) {
			logger.throwing("ModLoader", "getPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
			return null;
		}
	}

	public static int getUniqueBlockModelID(BaseMod mod, boolean full3DItem) {
		int id = nextBlockModelID++;

		if (ApronModLoader.IS_CLIENT) {
			blockModels.put(id, mod);
			blockSpecialInv.put(id, full3DItem);
		}

		return id;
	}

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

		if (ApronModLoader.IS_CLIENT) {
			clientInstance = MinecraftAccessor.getInstance();
//			clientInstance.gameRenderer = new EntityRendererProxy(clientInstance);
			classMap = EntityRegistryAccessor.getIdToClassMap();
		}

		try {
			field_modifiers = Field.class.getDeclaredField("modifiers");
			field_modifiers.setAccessible(true);
			Field[] fieldArray = Biome.class.getDeclaredFields();
			List<Biome> biomes = new LinkedList<>();

			for (Field field : fieldArray) {
				Class<?> fieldType = field.getType();
				if ((field.getModifiers() & 8) != 0 && fieldType.isAssignableFrom(Biome.class)) {
					Biome biome = (Biome) field.get(null);
					if (!(biome instanceof HellBiome) && (!ApronModLoader.IS_CLIENT || !(biome instanceof SkyBiome))) {
						biomes.add(biome);
					}
				}
			}

			standardBiomes = biomes.toArray(new Biome[0]);
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
			logger.throwing("ModLoader", "init", e);
			ThrowException(e);
			throw new RuntimeException(e);
		}

		try {
			try {
				loadConfig();
			} catch (IOException ioexception) {
				if (!ioexception.getMessage().contains("No such file or directory")) {
					throw ioexception;
				}

				String s2 = "Error loading ModLoader config. Check the common problems section in the ModLoaderMP thread.";
				ThrowException(new RuntimeException(s2, ioexception));
			}

			if (props.containsKey("loggingLevel")) {
				cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
			}

			if (ApronModLoader.IS_CLIENT && props.containsKey("grassFix")) {
				BlockRenderManagerAccessor.setCfgGrassFix(Boolean.parseBoolean(props.getProperty("grassFix")));
			}

			logger.setLevel(cfgLoggingLevel);
			if ((logfile.exists() || logfile.createNewFile()) && logfile.canWrite() && logHandler == null) {
				logHandler = new FileHandler(logfile.getPath());
				logHandler.setFormatter(new SimpleFormatter());
				logger.addHandler(logHandler);
			}

			logger.fine(VERSION + " Initializing...");
			System.out.println(VERSION + " Initializing...");
			if (!ApronModLoader.IS_CLIENT) MinecraftServer.LOGGER.info(VERSION + " Initializing...");
			modDir.mkdirs();
			readFromModFolder(modDir);
			readFromClassPath();
			System.out.println("Done.");
			props.setProperty("loggingLevel", cfgLoggingLevel.getName());
			if (ApronModLoader.IS_CLIENT) props.setProperty("grassFix", Boolean.toString(BlockRenderManagerAccessor.getCfgGrassFix()));

			for(BaseMod mod : modList) {
				mod.ModsLoaded();
				if (!props.containsKey(mod.getClass().getName())) {
					props.setProperty(mod.getClass().getName(), "on");
				}
			}

			if (ApronModLoader.IS_CLIENT) {
				clientInstance.options.allKeys = RegisterAllKeys(clientInstance.options.allKeys);
				clientInstance.options.load();
			}

			initStats();
			saveConfig();
		} catch (Throwable e) {
			logger.throwing("ModLoader", "init", e);
			ThrowException("ModLoader has failed to initialize.", e);
			if (logHandler != null) {
				logHandler.close();
			}

			throw new RuntimeException(e);
		}
	}

	private static void initStats() {
		for(int id = 0; id < Block.BLOCKS.length; ++id) {
			if (!StatsAccessor.getIdToStatMap().containsKey(16777216 + id) && Block.BLOCKS[id] != null && Block.BLOCKS[id].isTrackingStatistics()) {
				String str = translate("stat.mineBlock", Block.BLOCKS[id].getTranslatedName());
				Stats.MINE_BLOCK[id] = (new ItemOrBlockStat(16777216 + id, str, id)).addStat();
				Stats.BLOCK_MINED_STATS.add(Stats.MINE_BLOCK[id]);
			}
		}

		for(int id = 0; id < Item.ITEMS.length; ++id) {
			if (!StatsAccessor.getIdToStatMap().containsKey(16908288 + id) && Item.ITEMS[id] != null) {
				String str = translate("stat.useItem", Item.ITEMS[id].getTranslatedName());
				Stats.USED[id] = (new ItemOrBlockStat(16908288 + id, str, id)).addStat();
				if (id >= Block.BLOCKS.length) {
					Stats.ITEM_STATS.add(Stats.USED[id]);
				}
			}

			if (!StatsAccessor.getIdToStatMap().containsKey(16973824 + id) && Item.ITEMS[id] != null && Item.ITEMS[id].isDamageable()) {
				String str = translate("stat.breakItem", Item.ITEMS[id].getTranslatedName());
				Stats.BROKEN[id] = (new ItemOrBlockStat(16973824 + id, str, id)).addStat();
			}
		}

		HashSet<Integer> idHashSet = new HashSet<>();

		for(Object result : CraftingRecipeManager.getInstance().getRecipes()) {
			idHashSet.add(((CraftingRecipe)result).getOutput().itemId);
		}

		for(Object result : SmeltingRecipeManager.getInstance().getRecipes().values()) {
			idHashSet.add(((ItemStack)result).itemId);
		}

		for(int id : idHashSet) {
			if (!StatsAccessor.getIdToStatMap().containsKey(16842752 + id) && Item.ITEMS[id] != null) {
				String str = translate("stat.craftItem", Item.ITEMS[id].getTranslatedName());
				Stats.CRAFTED[id] = (new ItemOrBlockStat(16842752 + id, str, id)).addStat();
			}
		}

	}

	@Environment(EnvType.CLIENT)
	public static boolean isGUIOpen(Class<? extends Screen> gui) {
		Minecraft game = getMinecraftInstance();
		if (gui == null) {
			return game.currentScreen == null;
		} else {
			return gui.isInstance(game.currentScreen);
		}
	}

	public static boolean isModLoaded(String modname) {
		Class<?> chk = null;

		try {
			chk = Class.forName(modname, false, ModLoader.class.getClassLoader());
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

	@Environment(EnvType.CLIENT)
	public static BufferedImage loadImage(TextureManager texCache, String path) throws Exception {
		InputStream input = ((TextureManagerAccessor) texCache).getTexturePacks().selected.getResource(path);
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

	public static void OnItemPickup(PlayerEntity player, ItemStack item) {
		for(BaseMod mod : modList) {
			mod.OnItemPickup(player, item);
		}

	}

	@Environment(EnvType.CLIENT)
	public static void OnTick(Minecraft game) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		if (texPack == null || !Objects.equals(game.options.skin, texPack)) {
			texturesAdded = false;
			texPack = game.options.skin;
		}

		if (!texturesAdded && game.textureManager != null) {
			RegisterAllTextureOverrides(game.textureManager);
			texturesAdded = true;
		}

		long newclock = 0L;
		if (game.world != null) {
			newclock = game.world.getTime();
			Iterator<Map.Entry<BaseMod, Boolean>> iter = inGameHooks.entrySet().iterator();

			while(iter.hasNext()) {
				Map.Entry<BaseMod, Boolean> modSet = iter.next();
				if ((clock != newclock || !(Boolean)modSet.getValue()) && !modSet.getKey().OnTickInGame(game)) {
					iter.remove();
				}
			}
		}

		if (game.currentScreen != null) {
			Iterator<Map.Entry<BaseMod, Boolean>> iter = inGUIHooks.entrySet().iterator();

			while(iter.hasNext()) {
				Map.Entry<BaseMod, Boolean> modSet = iter.next();
				if ((clock != newclock || !(modSet.getValue() & game.world != null)) && !modSet.getKey().OnTickInGUI(game, game.currentScreen)) {
					iter.remove();
				}
			}
		}

		if (clock != newclock) {
			for(Map.Entry<BaseMod, Map<KeyBinding, boolean[]>> modSet : keyList.entrySet()) {
				for(Map.Entry<KeyBinding, boolean[]> keySet : modSet.getValue().entrySet()) {
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

		clock = newclock;
	}

	@Environment(EnvType.CLIENT)
	public static void OpenGUI(PlayerEntity player, Screen gui) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		Minecraft game = getMinecraftInstance();
		if (game.player == player) {
			if (gui != null) {
				game.setScreen(gui);
			}

		}
	}

	@Environment(EnvType.SERVER)
	public static void OnTick(MinecraftServer minecraftserver) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		long l = 0L;
		if (minecraftserver.worlds != null && minecraftserver.worlds[0] != null) {
			l = minecraftserver.worlds[0].getTime();

			for(Map.Entry<BaseMod, Boolean> entry : inGameHooks.entrySet()) {
				if (clock != l || !(Boolean)entry.getValue()) {
					entry.getKey().OnTickInGame(minecraftserver);
				}
			}
		}

		clock = l;
	}

	public static void PopulateChunk(ChunkSource generator, int chunkX, int chunkZ, World world) {
		if (!hasInit) {
			init();
			logger.fine("Initialized");
		}

		if (ApronModLoader.IS_CLIENT) {
			Random rnd = new Random(world.getSeed());
			long xSeed = rnd.nextLong() / 2L * 2L + 1L;
			long zSeed = rnd.nextLong() / 2L * 2L + 1L;
			rnd.setSeed((long) chunkX * xSeed + (long) chunkZ * zSeed ^ world.getSeed());

			for (BaseMod mod : modList) {
				if (generator.getDebugInfo().equals("RandomLevelSource")) {
					mod.GenerateSurface(world, rnd, chunkX << 4, chunkZ << 4);
				} else if (generator.getDebugInfo().equals("HellRandomLevelSource")) {
					mod.GenerateNether(world, rnd, chunkX << 4, chunkZ << 4);
				}
			}
		} else {
			for(BaseMod basemod : modList) {
				if (generator instanceof OverworldChunkGenerator) {
					basemod.GenerateSurface(world, world.random, chunkX, chunkZ);
				} else if (generator instanceof NetherChunkGenerator) {
					basemod.GenerateNether(world, world.random, chunkX, chunkZ);
				}
			}
		}
	}

	private static void readFromClassPath() {
		FabricLoader.getInstance().getEntrypointContainers( "modloader:base_mod", BaseMod.class).forEach(mod -> {
			try {
				String[] parts = mod.getDefinition().split("\\.");
				String name = parts[parts.length - 1];

				if (props.containsKey(name) && (props.getProperty(name).equalsIgnoreCase("no") || props.getProperty(name).equalsIgnoreCase("off"))) {
					return;
				}

				Class<?> instclass = Class.forName(mod.getDefinition(), false, ModLoader.class.getClassLoader());
				if (!BaseMod.class.isAssignableFrom(instclass)) {
					return;
				}

				setupProperties((Class<? extends BaseMod>) instclass);
				BaseMod modInstance = mod.getEntrypoint();
				if (modInstance != null) {
					modList.add(modInstance);
					logger.fine("Mod Loaded: \"" + modInstance + "\" from mod " + mod.getProvider().getMetadata().getId());
					System.out.println("Mod Loaded: " + modInstance);
					if (!ApronModLoader.IS_CLIENT) MinecraftServer.LOGGER.info("Mod Loaded: " + mod);
				}
			} catch (Throwable e) {
				logger.fine("Failed to load mod from mod \"" + mod.getProvider().getMetadata().getId() + "\"");
				System.out.println("Failed to load mod from mod \"" + mod.getProvider().getMetadata().getId() + "\"");
				logger.throwing("ModLoader", "readFromClassPath", e);
				ThrowException(e);
			}
		});
	}

	private static void readFromModFolder(File folder) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		ClassLoader loader = ModLoader.class.getClassLoader();

		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("folder must be a Directory.");
		} else {
			File[] sourcefiles = folder.listFiles();

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
							String[] parts = name.replace("\\", "/").split("/");
							String fileName = parts[parts.length - 1];

							if (!entry.isDirectory() && fileName.startsWith("mod_") && fileName.endsWith(".class")) {
								addMod(loader, name);
							}
						}
					}
				}
			}

		}
	}

	@Environment(EnvType.CLIENT)
	public static KeyBinding[] RegisterAllKeys(KeyBinding[] w) {
		List<KeyBinding> combinedList = new LinkedList<>(Arrays.asList(w));

		for(Map<KeyBinding, boolean[]> keyMap : keyList.values()) {
			combinedList.addAll(keyMap.keySet());
		}

		return combinedList.toArray(new KeyBinding[0]);
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterAllTextureOverrides(TextureManager texCache) {
		animList.clear();
		Minecraft game = getMinecraftInstance();

		for(BaseMod mod : modList) {
			mod.RegisterAnimation(game);
		}

		for(DynamicTexture anim : animList) {
			texCache.addDynamicTexture(anim);
		}

		for(Map.Entry<Integer, Map<String, Integer>> overlay : overrides.entrySet()) {
			for(Map.Entry<String, Integer> overlayEntry : overlay.getValue().entrySet()) {
				String overlayPath = overlayEntry.getKey();
				int index = overlayEntry.getValue();
				int dst = overlay.getKey();

				try {
					BufferedImage im = loadImage(texCache, overlayPath);
					DynamicTexture anim = new ModTextureStatic(index, dst, im);
					texCache.addDynamicTexture(anim);
				} catch (Exception e) {
					logger.throwing("ModLoader", "RegisterAllTextureOverrides", e);
					ThrowException(e);
					throw new RuntimeException(e);
				}
			}
		}

	}

	public static void RegisterBlock(Block block) {
		RegisterBlock(block, null);
	}

	public static void RegisterBlock(Block block, Class<? extends BlockItem> itemclass) {
		try {
			if (block == null) {
				throw new IllegalArgumentException("block parameter cannot be null.");
			}

			if (ApronModLoader.IS_CLIENT) Session.CREATIVE_INVENTORY.add(block);
			int id = block.id;
			BlockItem item;
			if (itemclass != null) {
				item = itemclass.getConstructor(Integer.TYPE).newInstance(id - 256);
			} else {
				item = new BlockItem(id - 256);
			}

			if (Block.BLOCKS[id] != null && Item.ITEMS[id] == null) {
				Item.ITEMS[id] = item;
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException |
				 NoSuchMethodException | InvocationTargetException e) {
			logger.throwing("ModLoader", "RegisterBlock", e);
			ThrowException(e);
		}

	}

	public static void RegisterEntityID(Class<? extends Entity> entityClass, String entityName, int id) {
		try {
			EntityRegistry.register(entityClass, entityName, id);
		} catch (IllegalArgumentException e) {
			logger.throwing("ModLoader", "RegisterEntityID", e);
			ThrowException(e);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterKey(BaseMod mod, KeyBinding keyHandler, boolean allowRepeat) {
		Map<KeyBinding, boolean[]> keyMap = keyList.get(mod);
		if (keyMap == null) {
			keyMap = new HashMap<>();
		}

		keyMap.put(keyHandler, new boolean[]{allowRepeat, false});
		keyList.put(mod, keyMap);
	}

	public static void RegisterTileEntity(Class<? extends BlockEntity> tileEntityClass, String id) {
		if (ApronModLoader.IS_CLIENT) {
			RegisterTileEntity(tileEntityClass, id, null);
		} else {
			try {
				BlockEntity.create(tileEntityClass, id);
			} catch (IllegalArgumentException e) {
				logger.throwing("ModLoader", "RegisterTileEntity", e);
				ThrowException(e);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterTileEntity(Class<? extends BlockEntity> tileEntityClass, String id, BlockEntityRenderer renderer) {
		try {
			BlockEntity.create(tileEntityClass, id);
			if (renderer != null) {
				BlockEntityRenderDispatcher ref = BlockEntityRenderDispatcher.INSTANCE;
				ref.renderers.put(tileEntityClass, renderer);
				renderer.setDispatcher(ref);
			}
		} catch (IllegalArgumentException e) {
			logger.throwing("ModLoader", "RegisterTileEntity", e);
			ThrowException(e);
		}

	}

	public static void RemoveSpawn(Class<? extends LivingEntity> entityClass, SpawnGroup spawnList) {
		RemoveSpawn(entityClass, spawnList, (Biome[])null);
	}

	public static void RemoveSpawn(Class<? extends LivingEntity> entityClass, SpawnGroup spawnList, Biome... biomes) {
		if (entityClass == null) {
			throw new IllegalArgumentException("entityClass cannot be null");
		} else if (spawnList == null) {
			throw new IllegalArgumentException("spawnList cannot be null");
		} else {
			if (biomes == null) {
				biomes = standardBiomes;
			}

			for (Biome biome : biomes) {
				List<EntitySpawnGroup> list = biome.getSpawnableEntities(spawnList);
				if (list != null) {

					list.removeIf(entry -> entry.clazz == entityClass);
				}
			}

		}
	}

	public static void RemoveSpawn(String entityName, SpawnGroup spawnList) {
		RemoveSpawn(entityName, spawnList, (Biome[])null);
	}

	public static void RemoveSpawn(String entityName, SpawnGroup spawnList, Biome... biomes) {
		Class<? extends Entity> entityClass = classMap.get(entityName);
		if (entityClass != null && LivingEntity.class.isAssignableFrom(entityClass)) {
			RemoveSpawn((Class<? extends LivingEntity>) entityClass, spawnList, biomes);
		}

	}

	@Environment(EnvType.CLIENT)
	public static boolean RenderBlockIsItemFull3D(int modelID) {
		if (!blockSpecialInv.containsKey(modelID)) {
			return modelID == 16;
		} else {
			return blockSpecialInv.get(modelID);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void RenderInvBlock(BlockRenderManager renderer, Block block, int metadata, int modelID) {
		BaseMod mod = blockModels.get(modelID);
		if (mod != null) {
			mod.RenderInvBlock(renderer, block, metadata, modelID);
		}
	}

	@Environment(EnvType.CLIENT)
	public static boolean RenderWorldBlock(BlockRenderManager renderer, BlockView world, int x, int y, int z, Block block, int modelID) {
		BaseMod mod = blockModels.get(modelID);
		return mod != null && mod.RenderWorldBlock(renderer, world, x, y, z, block, modelID);
	}

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

	public static void SetInGameHook(BaseMod mod, boolean enable, boolean useClock) {
		if (enable) {
			inGameHooks.put(mod, useClock);
		} else {
			inGameHooks.remove(mod);
		}

	}

	@Environment(EnvType.CLIENT)
	public static void SetInGUIHook(BaseMod mod, boolean enable, boolean useClock) {
		if (enable) {
			inGUIHooks.put(mod, useClock);
		} else {
			inGUIHooks.remove(mod);
		}

	}

	public static <T, E> void setPrivateValue(Class<? super T> instanceclass, T instance, int fieldindex, E value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = instanceclass.getDeclaredFields()[fieldindex];
			f.setAccessible(true);
			int modifiers = field_modifiers.getInt(f);
			if ((modifiers & 16) != 0) {
				field_modifiers.setInt(f, modifiers & -17);
			}

			f.set(instance, value);
		} catch (IllegalAccessException e) {
			logger.throwing("ModLoader", "setPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
		}

	}

	public static <T, E> void setPrivateValue(Class<? super T> instanceclass, T instance, String field, E value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
		try {
			Field f = instanceclass.getDeclaredField(field);
			int modifiers = field_modifiers.getInt(f);
			if ((modifiers & 16) != 0) {
				field_modifiers.setInt(f, modifiers & -17);
			}

			f.setAccessible(true);
			f.set(instance, value);
		} catch (IllegalAccessException e) {
			logger.throwing("ModLoader", "setPrivateValue", e);
			ThrowException("An impossible error has occured!", e);
		}

	}

	private static void setupProperties(Class<? extends BaseMod> mod) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException {
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
				String key = annotation.name().isEmpty() ? field.getName() : annotation.name();
				Object currentvalue = field.get(null);
				StringBuilder range = new StringBuilder();
				if (annotation.min() != Double.NEGATIVE_INFINITY) {
					range.append(String.format(",>=%.1f", annotation.min()));
				}

				if (annotation.max() != Double.POSITIVE_INFINITY) {
					range.append(String.format(",<=%.1f", annotation.max()));
				}

				StringBuilder info = new StringBuilder();
				if (!annotation.info().isEmpty()) {
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
							if (annotation.min() != Double.NEGATIVE_INFINITY && num < annotation.min() || annotation.max() != Double.POSITIVE_INFINITY && num > annotation.max()) {
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

	public static void TakenFromCrafting(PlayerEntity player, ItemStack item) {
		for(BaseMod mod : modList) {
			mod.TakenFromCrafting(player, item);
		}

	}

	public static void TakenFromFurnace(PlayerEntity player, ItemStack item) {
		for(BaseMod mod : modList) {
			mod.TakenFromFurnace(player, item);
		}

	}

	public static void ThrowException(String message, Throwable e) {
		if (ApronModLoader.IS_CLIENT) {
			Minecraft game = getMinecraftInstance();
			if (game != null) {
				game.handleCrash(new CrashReport(message, e));
			} else {
				throw new RuntimeException(e);
			}
		} else {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected exception", e);
			MinecraftServer.LOGGER.throwing("ModLoader", message, e);
			throw new RuntimeException(message, e);
		}
	}

	private static void ThrowException(Throwable e) {
		ThrowException("Exception occured in ModLoader", e);
	}

	private ModLoader() {
	}

	@Environment(EnvType.SERVER)
	public static void Init(MinecraftServer minecraftserver) {
		serverInstance = minecraftserver;

		init();
	}

	@Environment(EnvType.SERVER)
	public static void OpenGUI(PlayerEntity entityplayer, int i, Inventory iinventory, ScreenHandler container) {
		if (!hasInit) {
			init();
		}

		if (entityplayer instanceof ServerPlayerEntity) {
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity)entityplayer;

			((ServerPlayerEntityAccessor) entityplayermp).invokeIncrementScreenHandlerSyncId();
			int j = ((ServerPlayerEntityAccessor) entityplayermp).getScreenHandlerSyncId();
			entityplayermp.networkHandler.sendPacket(new OpenScreenS2CPacket(j, i, iinventory.getName(), iinventory.size()));
			entityplayermp.currentScreenHandler = container;
			entityplayermp.currentScreenHandler.syncId = j;
			entityplayermp.currentScreenHandler.addListener(entityplayermp);
		}

	}

	// MERGED JAR SUPPORT

	private static String translate(String key) {
		if (ApronModLoader.IS_CLIENT) {
			return TranslationStorage.getInstance().get(key);
		} else {
			return I18n.getTranslation(key);
		}
	}

	private static String translate(String key, Object... format) {
		if (ApronModLoader.IS_CLIENT) {
			return TranslationStorage.getInstance().get(key, format);
		} else {
			return I18n.getTranslation(key, format);
		}
	}
}

