/**
 * Minecraft Forge Public Licence
 * <p>
 * ==============================
 * <p>
 * <p>
 * Version 1.1
 * <p>
 * <p>
 * 0. Definitions
 * <p>
 * --------------
 * <p>
 * <p>
 * Minecraft: Denotes a copy of the Minecraft game licensed by Mojang AB
 * <p>
 * <p>
 * User: Anybody that interact with the software in one of the following ways:
 * <p>
 * - play
 * <p>
 * - decompile
 * <p>
 * - recompile or compile
 * <p>
 * - modify
 * <p>
 * <p>
 * Minecraft Forge: The Minecraft Forge code, in source form, class file form, as
 * <p>
 * obtained in a standalone fashion or as part of a wider distribution.
 * <p>
 * <p>
 * Dependency: Code required to have Minecraft Forge working properly. That can
 * <p>
 * include dependencies required to compile the code as well as modifications in
 * <p>
 * the Minecraft sources that are required to have Minecraft Forge working.
 * <p>
 * <p>
 * 1. Scope
 * <p>
 * --------
 * <p>
 * <p>
 * The present license is granted to any user of Minecraft Forge. As a
 * <p>
 * prerequisite, a user of Minecraft Forge must own a legally aquired copy of
 * <p>
 * Minecraft
 * <p>
 * <p>
 * 2. Play rights
 * <p>
 * --------------
 * <p>
 * <p>
 * The user of Minecraft Forge is allowed to install the software on a client or
 * <p>
 * a server and to play it without restriction.
 * <p>
 * <p>
 * 3. Modification rights
 * <p>
 * ----------------------
 * <p>
 * <p>
 * The user has the right to decompile the source code, look at either the
 * <p>
 * decompiled version or the original source code, and to modify it.
 * <p>
 * <p>
 * 4. Derivation rights
 * <p>
 * --------------------
 * <p>
 * <p>
 * The user has the rights to derive code from Minecraft Forge, that is to say to
 * <p>
 * write code that either extends Minecraft Forge class and interfaces,
 * <p>
 * instantiate the objects declared or calls the functions. This code is known as
 * <p>
 * "derived" code, and can be licensed with conditions different from Minecraft
 * <p>
 * Forge.
 * <p>
 * <p>
 * <p>
 * 5. Distribution rights
 * <p>
 * ----------------------
 * <p>
 * <p>
 * The user of Minecraft Forge is allowed to redistribute Minecraft Forge in
 * <p>
 * partially, in totality, or included in a distribution. When distributing
 * <p>
 * binaries or class files, the user must provide means to obtain the sources of
 * <p>
 * the distributed version of Minecraft Forge at no costs. This includes the
 * <p>
 * files as well as any dependency that the code may rely on, including patches to
 * <p>
 * minecraft original sources.
 * <p>
 * <p>
 * Modification of Minecraft Forge as well as dependencies, including patches to
 * <p>
 * minecraft original sources, has to remain under the terms of the present
 * <p>
 * license.
 */
package forge;

import net.minecraft.block.Block;

import java.io.*;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;

public class Configuration {
	private boolean[] configBlocks = null;
	public static final int GENERAL_PROPERTY = 0;
	public static final int BLOCK_PROPERTY = 1;
	public static final int ITEM_PROPERTY = 2;
	File file;
	public TreeMap<String, Property> blockProperties = new TreeMap<>();
	public TreeMap<String, Property> itemProperties = new TreeMap<>();
	public TreeMap<String, Property> generalProperties = new TreeMap<>();

	public Configuration(File file) {
		this.file = file;
	}

	public Property getOrCreateBlockIdProperty(String key, int defaultId) {
		if (this.configBlocks == null) {
			this.configBlocks = new boolean[Block.BY_ID.length];

			Arrays.fill(this.configBlocks, false);
		}

		if (this.blockProperties.containsKey(key)) {
			Property property = this.getOrCreateIntProperty(key, BLOCK_PROPERTY, defaultId);
			this.configBlocks[Integer.parseInt(property.value)] = true;
			return property;
		} else {
			Property property = new Property();
			this.blockProperties.put(key, property);
			property.name = key;
			if (Block.BY_ID[defaultId] == null && !this.configBlocks[defaultId]) {
				property.value = Integer.toString(defaultId);
				this.configBlocks[defaultId] = true;
				return property;
			} else {
				for (int j = Block.BY_ID.length - 1; j >= 0; --j) {
					if (Block.BY_ID[j] == null && !this.configBlocks[j]) {
						property.value = Integer.toString(j);
						this.configBlocks[j] = true;
						return property;
					}
				}

				throw new RuntimeException("No more block ids available for " + key);
			}
		}
	}

	public Property getOrCreateIntProperty(String key, int kind, int defaultValue) {
		Property prop = this.getOrCreateProperty(key, kind, Integer.toString(defaultValue));

		try {
			Integer.parseInt(prop.value);
			return prop;
		} catch (NumberFormatException var6) {
			prop.value = Integer.toString(defaultValue);
			return prop;
		}
	}

	public Property getOrCreateBooleanProperty(String key, int kind, boolean defaultValue) {
		Property prop = this.getOrCreateProperty(key, kind, Boolean.toString(defaultValue));
		if (!"true".equalsIgnoreCase(prop.value) && !"false".equalsIgnoreCase(prop.value)) {
			prop.value = Boolean.toString(defaultValue);
		}
		return prop;
	}

	public Property getOrCreateProperty(String key, int kind, String defaultValue) {
		TreeMap<String, Property> source = null;
		switch (kind) {
			case GENERAL_PROPERTY:
				source = this.generalProperties;
				break;
			case BLOCK_PROPERTY:
				source = this.blockProperties;
				break;
			case ITEM_PROPERTY:
				source = this.itemProperties;
		}

		if (source.containsKey(key)) {
			return source.get(key);
		} else if (defaultValue != null) {
			Property property = new Property();
			source.put(key, property);
			property.name = key;
			property.value = defaultValue;
			return property;
		} else {
			return null;
		}
	}

	public void load() {
		try {
			if (this.file.getParentFile() != null) {
				this.file.getParentFile().mkdirs();
			}

			if (!this.file.exists() && !this.file.createNewFile()) {
				return;
			}

			if (this.file.canRead()) {
				FileInputStream fileinputstream = new FileInputStream(this.file);
				BufferedReader buffer = new BufferedReader(new InputStreamReader(fileinputstream, "8859_1"));
				TreeMap<String, Property> currentMap = null;

				while (true) {
					String line = buffer.readLine();
					if (line == null) {
						break;
					}

					int nameStart = -1;
					int nameEnd = -1;
					boolean skip = false;

					for (int i = 0; i < line.length() && !skip; ++i) {
						if (!Character.isLetterOrDigit(line.charAt(i)) && line.charAt(i) != '.') {
							if (!Character.isWhitespace(line.charAt(i))) {
								switch (line.charAt(i)) {
									case '#':
										skip = true;
										break;
									case '=':
										String propertyName = line.substring(nameStart, nameEnd + 1);
										if (currentMap == null) {
											throw new RuntimeException("property " + propertyName + " has no scope");
										}

										Property prop = new Property();
										prop.name = propertyName;
										prop.value = line.substring(i + 1);
										i = line.length();
										currentMap.put(propertyName, prop);
										break;
									case '{':
										String scopeName = line.substring(nameStart, nameEnd + 1);
										if (scopeName.equals("general")) {
											currentMap = this.generalProperties;
										} else if (scopeName.equals("block")) {
											currentMap = this.blockProperties;
										} else {
											if (!scopeName.equals("item")) {
												throw new RuntimeException("unknown section " + scopeName);
											}

											currentMap = this.itemProperties;
										}
										break;
									case '}':
										currentMap = null;
										break;
									default:
										throw new RuntimeException("unknown character " + line.charAt(i));
								}
							}
						} else {
							if (nameStart == -1) {
								nameStart = i;
							}

							nameEnd = i;
						}
					}
				}
			}
		} catch (IOException var12) {
			var12.printStackTrace();
		}
	}

	public void save() {
		try {
			if (this.file.getParentFile() != null) {
				this.file.getParentFile().mkdirs();
			}

			if (!this.file.exists() && !this.file.createNewFile()) {
				return;
			}

			if (this.file.canWrite()) {
				FileOutputStream fileoutputstream = new FileOutputStream(this.file);
				BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fileoutputstream, "8859_1"));
				buffer.write("# Configuration file\r\n");
				buffer.write("# Generated on " + DateFormat.getInstance().format(new Date()) + "\r\n");
				buffer.write("\r\n");
				buffer.write("###########\r\n");
				buffer.write("# General #\r\n");
				buffer.write("###########\r\n\r\n");
				buffer.write("general {\r\n");
				this.writeProperties(buffer, this.generalProperties.values());
				buffer.write("}\r\n\r\n");
				buffer.write("#########\r\n");
				buffer.write("# Block #\r\n");
				buffer.write("#########\r\n\r\n");
				buffer.write("block {\r\n");
				this.writeProperties(buffer, this.blockProperties.values());
				buffer.write("}\r\n\r\n");
				buffer.write("########\r\n");
				buffer.write("# Item #\r\n");
				buffer.write("########\r\n\r\n");
				buffer.write("item {\r\n");
				this.writeProperties(buffer, this.itemProperties.values());
				buffer.write("}\r\n\r\n");
				buffer.close();
				fileoutputstream.close();
			}
		} catch (IOException var3) {
			var3.printStackTrace();
		}
	}

	private void writeProperties(BufferedWriter buffer, Collection<Property> props) throws IOException {
		for (Property property : props) {
			if (property.comment != null) {
				buffer.write("   # " + property.comment + "\r\n");
			}

			buffer.write("   " + property.name + "=" + property.value);
			buffer.write("\r\n");
		}
	}
}
