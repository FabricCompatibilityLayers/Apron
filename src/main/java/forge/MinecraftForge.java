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

import net.minecraft.block.Block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class MinecraftForge {
	private static LinkedList<IBucketHandler> bucketHandlers = new LinkedList();
	public static boolean disableVersionCheckCrash = false;
	private static LinkedList<IOreHandler> oreHandlers = new LinkedList();
	private static TreeMap<String, List<ItemStack>> oreDict = new TreeMap();

	public MinecraftForge() {
	}

	/** @deprecated */
	public static void registerCustomBucketHander(forge.IBucketHandler handler) {
		bucketHandlers.add(handler);
	}

	public static void registerCustomBucketHandler(forge.IBucketHandler handler) {
		bucketHandlers.add(handler);
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

	public static ItemStack fillCustomBucket(World w, int i, int j, int k) {
		for(forge.IBucketHandler handler : bucketHandlers) {
			ItemStack stack = handler.fillCustomBucket(w, i, j, k);
			if (stack != null) {
				return stack;
			}
		}

		return null;
	}

	public static void setToolClass(Item tool, String tclass, int hlevel) {
		ForgeHooks.initTools();
		ForgeHooks.toolClasses.put(tool.id, Arrays.asList(tclass, hlevel));
	}

	public static void setBlockHarvestLevel(Block bl, int md, String tclass, int hlevel) {
		ForgeHooks.initTools();
		List key = Arrays.asList(bl.id, md, tclass);
		ForgeHooks.toolHarvestLevels.put(key, hlevel);
		ForgeHooks.toolEffectiveness.add(key);
	}

	public static void removeBlockEffectiveness(Block bl, int md, String tclass) {
		ForgeHooks.initTools();
		List key = Arrays.asList(bl.id, md, tclass);
		ForgeHooks.toolEffectiveness.remove(key);
	}

	public static void setBlockHarvestLevel(Block bl, String tclass, int hlevel) {
		ForgeHooks.initTools();

		for(int md = 0; md < 16; ++md) {
			List key = Arrays.asList(bl.id, md, tclass);
			ForgeHooks.toolHarvestLevels.put(key, hlevel);
			ForgeHooks.toolEffectiveness.add(key);
		}

	}

	public static void removeBlockEffectiveness(Block bl, String tclass) {
		ForgeHooks.initTools();

		for(int md = 0; md < 16; ++md) {
			List key = Arrays.asList(bl.id, md, tclass);
			ForgeHooks.toolEffectiveness.remove(key);
		}

	}

	public static void addPickaxeBlockEffectiveAgainst(Block block) {
		setBlockHarvestLevel(block, "pickaxe", 0);
	}

	public static void killMinecraft(String modname, String msg) {
		throw new RuntimeException(modname + ": " + msg);
	}

	public static void versionDetect(String modname, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			System.out
					.println(
							modname + ": Forge version detect was called, but strict crashing is disabled. Expected version " + major + "." + minor + "." + revision
					);
		} else {
			if (major != 1) {
				killMinecraft(modname, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != 0) {
				if (minor > 0) {
					killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					System.out
							.println(modname + ": MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x, may lead to unexpected behavior");
				}
			} else if (revision > 6) {
				killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
			}

		}
	}

	public static void versionDetectStrict(String modname, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			System.out
					.println(
							modname + ": Forge version detect was called, but strict crashing is disabled. Expected version " + major + "." + minor + "." + revision
					);
		} else {
			if (major != 1) {
				killMinecraft(modname, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != 0) {
				if (minor > 0) {
					killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					killMinecraft(modname, "MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x");
				}
			} else if (revision > 6) {
				killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
			}

		}
	}

	public static void registerOreHandler(forge.IOreHandler handler) {
		oreHandlers.add(handler);

		for(String key : oreDict.keySet()) {
			for(ItemStack ist : oreDict.get(key)) {
				handler.registerOre(key, ist);
			}
		}

	}

	public static void registerOre(String oreClass, ItemStack ore) {
		List<ItemStack> orelist = oreDict.get(oreClass);
		if (orelist == null) {
			orelist = new ArrayList();
			oreDict.put(oreClass, orelist);
		}

		orelist.add(ore);

		for(forge.IOreHandler ioh : oreHandlers) {
			ioh.registerOre(oreClass, ore);
		}

	}

	public static List<ItemStack> getOreClass(String oreClass) {
		return (List<ItemStack>)oreDict.get(oreClass);
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
			LinkedList itering = new LinkedList();
			LinkedList output = new LinkedList();

			private OreQueryIterator() {
				for(int i = 0; i < OreQuery.this.proto.length; ++i) {
					if (OreQuery.this.proto[i] instanceof Collection) {
						Iterator it = ((Collection)OreQuery.this.proto[i]).iterator();
						if (!it.hasNext()) {
							this.output = null;
							break;
						}

						this.itering.addLast(it);
						this.output.addLast(it.next());
					} else {
						this.itering.addLast(OreQuery.this.proto[i]);
						this.output.addLast(OreQuery.this.proto[i]);
					}
				}

			}

			public boolean hasNext() {
				return this.output != null;
			}

			public Object[] next() {
				Object[] tr;
				for(tr = this.output.toArray(); this.itering.size() != 0; this.itering.removeLast()) {
					Object to = this.itering.getLast();
					this.output.removeLast();
					if (to instanceof Iterator) {
						Iterator it = (Iterator)to;
						if (it.hasNext()) {
							this.output.addLast(it.next());

							for(int var5 = this.itering.size(); var5 < OreQuery.this.proto.length; ++var5) {
								if (OreQuery.this.proto[var5] instanceof Collection) {
									Iterator itx = ((Collection)OreQuery.this.proto[var5]).iterator();
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
