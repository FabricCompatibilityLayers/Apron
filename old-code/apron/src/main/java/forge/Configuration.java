/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;

import net.minecraft.block.Block;

import io.github.betterthanupdates.Legacy;

@SuppressWarnings("unused")
@Legacy
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
			this.configBlocks = new boolean[Block.BLOCKS.length];

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

			if (Block.BLOCKS[defaultId] == null && !this.configBlocks[defaultId]) {
				property.value = Integer.toString(defaultId);
				this.configBlocks[defaultId] = true;
				return property;
			} else {
				for (int j = Block.BLOCKS.length - 1; j >= 0; --j) {
					if (Block.BLOCKS[j] == null && !this.configBlocks[j]) {
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
		} catch (NumberFormatException ignored) {
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

					if (line == null) break;

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
		} catch (IOException e) {
			e.printStackTrace();
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
		} catch (IOException e) {
			e.printStackTrace();
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
