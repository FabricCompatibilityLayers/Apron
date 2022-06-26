/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import reforged.ReforgedHooks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;

@SuppressWarnings("unused")
@Legacy
public class MinecraftForge {
	public static final Logger LOGGER = ApronApi.getLogger("Minecraft Forge");

	private static final LinkedList<IBucketHandler> BUCKET_HANDLERS = new LinkedList<>();
	public static boolean disableVersionCheckCrash = false;
	private static final LinkedList<IOreHandler> ORE_HANDLERS = new LinkedList<>();
	private static final TreeMap<String, List<ItemStack>> ORE_DICT = new TreeMap<>();

	public MinecraftForge() {
	}

	/**
	 * @deprecated
	 */
	public static void registerCustomBucketHander(IBucketHandler handler) {
		BUCKET_HANDLERS.add(handler);
	}

	public static void registerCustomBucketHandler(IBucketHandler handler) {
		BUCKET_HANDLERS.add(handler);
	}

	public static void registerSleepHandler(ISleepHandler handler) {
		ForgeHooks.sleepHandlers.add(handler);
	}

	public static void registerDestroyToolHandler(IDestroyToolHandler handler) {
		ForgeHooks.destroyToolHandlers.add(handler);
	}

	public static void registerCraftingHandler(ICraftingHandler handler) {
		ForgeHooks.craftingHandlers.add(handler);
	}

	public static ItemStack fillCustomBucket(World world, int x, int y, int z) {
		for (IBucketHandler handler : BUCKET_HANDLERS) {
			ItemStack stack = handler.fillCustomBucket(world, x, y, z);

			if (stack != null) {
				return stack;
			}
		}

		return null;
	}

	public static void setToolClass(Item tool, String toolType, int harvestLevel) {
		ForgeHooks.initTools();
		ForgeHooks.toolClasses.put(tool.id, Arrays.asList(toolType, harvestLevel));
	}

	public static void setBlockHarvestLevel(Block block, int meta, String toolType, int harvestLevel) {
		ForgeHooks.initTools();
		List<?> key = Arrays.asList(block.id, meta, toolType);
		ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
		ForgeHooks.toolEffectiveness.add(key);
	}

	public static void removeBlockEffectiveness(Block block, int meta, String toolType) {
		ForgeHooks.initTools();
		List<?> key = Arrays.asList(block.id, meta, toolType);
		ForgeHooks.toolEffectiveness.remove(key);
	}

	public static void setBlockHarvestLevel(Block block, String toolType, int harvestLevel) {
		ForgeHooks.initTools();

		for (int meta = 0; meta < 16; ++meta) {
			List<?> key = Arrays.asList(block.id, meta, toolType);
			ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
			ForgeHooks.toolEffectiveness.add(key);
		}
	}

	public static void removeBlockEffectiveness(Block block, String toolType) {
		ForgeHooks.initTools();

		for (int meta = 0; meta < 16; ++meta) {
			List<?> key = Arrays.asList(block.id, meta, toolType);
			ForgeHooks.toolEffectiveness.remove(key);
		}
	}

	public static void addPickaxeBlockEffectiveAgainst(Block block) {
		setBlockHarvestLevel(block, "pickaxe", 0);
	}

	public static void killMinecraft(String modName, String msg) {
		throw new RuntimeException(modName + ": " + msg);
	}

	public static void versionDetect(String modName, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			LOGGER.info("%s: Forge version detect was called, but strict crashing is disabled. Expected version %d.%d.%d",
					modName, major, minor, revision);
		} else {
			if (major != ForgeHooks.majorVersion) {
				killMinecraft(modName, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != ForgeHooks.minorVersion) {
				if (minor > ForgeHooks.minorVersion) {
					killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					LOGGER.info("%s: MinecraftForge minor version mismatch, expecting %d.%d.x, may lead to unexpected behavior",
							modName, major, minor);
				}
			} else if (revision > ForgeHooks.revisionVersion) {
				killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
			}
		}
	}

	public static void versionDetectStrict(String modName, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			LOGGER.info("%s: Forge version detect was called, but strict crashing is disabled. Expected version %d.%d.%d",
					modName, major, minor, revision);
		} else {
			if (major != ForgeHooks.majorVersion) {
				killMinecraft(modName, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != ForgeHooks.minorVersion) {
				if (minor > ForgeHooks.minorVersion) {
					killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					killMinecraft(modName, "MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x");
				}
			} else if (revision > ForgeHooks.revisionVersion) {
				killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
			}
		}
	}

	public static void registerOreHandler(IOreHandler handler) {
		ORE_HANDLERS.add(handler);

		for (String key : ORE_DICT.keySet()) {
			for (ItemStack itemStack : ORE_DICT.get(key)) {
				handler.registerOre(key, itemStack);
			}
		}
	}

	public static void registerOre(String oreClass, ItemStack oreStack) {
		List<ItemStack> oreList = ORE_DICT.computeIfAbsent(oreClass, k -> new ArrayList<>());

		oreList.add(oreStack);

		for (IOreHandler oreHandler : ORE_HANDLERS) {
			oreHandler.registerOre(oreClass, oreStack);
		}
	}

	public static List<ItemStack> getOreClass(String oreClass) {
		return ORE_DICT.get(oreClass);
	}

	public static MinecraftForge.OreQuery generateRecipes(Object... pattern) {
		return new MinecraftForge.OreQuery(pattern);
	}

	public static class OreQuery implements Iterable<Object[]> {
		Object[] proto;

		private OreQuery(Object[] pattern) {
			this.proto = pattern;
		}

		public Iterator<Object[]> iterator() {
			return new MinecraftForge.OreQuery.OreQueryIterator();
		}

		public class OreQueryIterator implements Iterator<Object[]> {
			LinkedList itering = new LinkedList<>();
			LinkedList output = new LinkedList<>();

			private OreQueryIterator() {
				for (Object o : OreQuery.this.proto) {
					if (o instanceof Collection) {
						Iterator it = ((Collection) o).iterator();

						if (!it.hasNext()) {
							this.output = null;
							break;
						}

						this.itering.addLast(it);
						this.output.addLast(it.next());
					} else {
						this.itering.addLast(o);
						this.output.addLast(o);
					}
				}
			}

			public boolean hasNext() {
				return this.output != null;
			}

			public Object[] next() {
				Object[] tr;

				for (tr = this.output.toArray(); this.itering.size() != 0; this.itering.removeLast()) {
					Object to = this.itering.getLast();
					this.output.removeLast();

					if (to instanceof Iterator) {
						Iterator it = (Iterator) to;

						if (it.hasNext()) {
							this.output.addLast(it.next());

							for (int var5 = this.itering.size(); var5 < OreQuery.this.proto.length; ++var5) {
								if (OreQuery.this.proto[var5] instanceof Collection) {
									Iterator itx = ((Collection) OreQuery.this.proto[var5]).iterator();

									if (!itx.hasNext()) {
										this.output = null;
										break;
									}

									this.itering.addLast(itx);
									this.output.addLast(itx.next());
								} else {
									this.itering.addLast(OreQuery.this.proto[var5]);
									this.output.addLast(OreQuery.this.proto[var5]);
								}
							}

							return tr;
						}
					}
				}

				this.output = null;
				return tr;
			}

			public void remove() {
			}
		}
	}
}
