/**
 * Minecraft Forge Public Licence
 *
 * ==============================
 *
 *
 * Version 1.1
 *
 *
 * 0. Definitions
 *
 * --------------
 *
 *
 * Minecraft: Denotes a copy of the Minecraft game licensed by Mojang AB
 *
 *
 * User: Anybody that interact with the software in one of the following ways:
 *
 *    - play
 *
 *    - decompile
 *
 *    - recompile or compile
 *
 *    - modify
 *
 *
 * Minecraft Forge: The Minecraft Forge code, in source form, class file form, as
 *
 * obtained in a standalone fashion or as part of a wider distribution.
 *
 *
 * Dependency: Code required to have Minecraft Forge working properly. That can
 *
 * include dependencies required to compile the code as well as modifications in
 *
 * the Minecraft sources that are required to have Minecraft Forge working.
 *
 *
 * 1. Scope
 *
 * --------
 *
 *
 * The present license is granted to any user of Minecraft Forge. As a
 *
 * prerequisite, a user of Minecraft Forge must own a legally aquired copy of
 *
 * Minecraft
 *
 *
 * 2. Play rights
 *
 * --------------
 *
 *
 * The user of Minecraft Forge is allowed to install the software on a client or
 *
 * a server and to play it without restriction.
 *
 *
 * 3. Modification rights
 *
 * ----------------------
 *
 *
 * The user has the right to decompile the source code, look at either the
 *
 * decompiled version or the original source code, and to modify it.
 *
 *
 * 4. Derivation rights
 *
 * --------------------
 *
 *
 * The user has the rights to derive code from Minecraft Forge, that is to say to
 *
 * write code that either extends Minecraft Forge class and interfaces,
 *
 * instantiate the objects declared or calls the functions. This code is known as
 *
 * "derived" code, and can be licensed with conditions different from Minecraft
 *
 * Forge.
 *
 *
 *
 * 5. Distribution rights
 *
 * ----------------------
 *
 *
 * The user of Minecraft Forge is allowed to redistribute Minecraft Forge in
 *
 * partially, in totality, or included in a distribution. When distributing
 *
 * binaries or class files, the user must provide means to obtain the sources of
 *
 * the distributed version of Minecraft Forge at no costs. This includes the
 *
 * files as well as any dependency that the code may rely on, including patches to
 *
 * minecraft original sources.
 *
 *
 * Modification of Minecraft Forge as well as dependencies, including patches to
 *
 * minecraft original sources, has to remain under the terms of the present
 *
 * license.
 */
package forge;

import java.util.Arrays;
import net.minecraft.block.Block;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;

public class Configuration {
    public static final int GENERAL_PROPERTY = 0;
    public static final int BLOCK_PROPERTY = 1;
    public static final int ITEM_PROPERTY = 2;
    public TreeMap<String, Property> blockProperties;
    public TreeMap<String, Property> itemProperties;
    public TreeMap<String, Property> generalProperties;
    private boolean[] configBlocks;
    File file;
    
    public Configuration(final File file) {
        this.configBlocks = null;
        this.blockProperties = new TreeMap<>();
        this.itemProperties = new TreeMap<>();
        this.generalProperties = new TreeMap<>();
        this.file = file;
    }
    
    public Property getOrCreateBlockIdProperty(final String key, final int defaultId) {
        if (this.configBlocks == null) {
            this.configBlocks = new boolean[Block.BY_ID.length];
            Arrays.fill(this.configBlocks, false);
        }
        if (this.blockProperties.containsKey(key)) {
            final Property property = this.getOrCreateIntProperty(key, 1, defaultId);
            this.configBlocks[Integer.parseInt(property.value)] = true;
            return property;
        }
        final Property property = new Property();
        this.blockProperties.put(key, property);
        property.name = key;
        if (Block.BY_ID[defaultId] == null && !this.configBlocks[defaultId]) {
            property.value = Integer.toString(defaultId);
            this.configBlocks[defaultId] = true;
            return property;
        }
        for (int j = Block.BY_ID.length - 1; j >= 0; --j) {
            if (Block.BY_ID[j] == null && !this.configBlocks[j]) {
                property.value = Integer.toString(j);
                this.configBlocks[j] = true;
                return property;
            }
        }
        throw new RuntimeException("No more block ids available for " + key);
    }
    
    public Property getOrCreateIntProperty(final String key, final int kind, final int defaultValue) {
        final Property prop = this.getOrCreateProperty(key, kind, Integer.toString(defaultValue));
        try {
            Integer.parseInt(prop.value);
            return prop;
        }
        catch (NumberFormatException e) {
            prop.value = Integer.toString(defaultValue);
            return prop;
        }
    }
    
    public Property getOrCreateBooleanProperty(final String key, final int kind, final boolean defaultValue) {
        final Property prop = this.getOrCreateProperty(key, kind, Boolean.toString(defaultValue));
        if ("true".equalsIgnoreCase(prop.value) || "false".equalsIgnoreCase(prop.value)) {
            return prop;
        }
        prop.value = Boolean.toString(defaultValue);
        return prop;
    }
    
    public Property getOrCreateProperty(final String key, final int kind, final String defaultValue) {
        TreeMap<String, Property> source = null;
        switch (kind) {
            case GENERAL_PROPERTY: {
                source = this.generalProperties;
                break;
            }
            case BLOCK_PROPERTY: {
                source = this.blockProperties;
                break;
            }
            case ITEM_PROPERTY: {
                source = this.itemProperties;
                break;
            }
        }
        if (source.containsKey(key)) {
            return source.get(key);
        }
        if (defaultValue != null) {
            final Property property = new Property();
            source.put(key, property);
            property.name = key;
            property.value = defaultValue;
            return property;
        }
        return null;
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
                final FileInputStream fileinputstream = new FileInputStream(this.file);
                final BufferedReader buffer = new BufferedReader(new InputStreamReader(fileinputstream, "8859_1"));
                TreeMap<String, Property> currentMap = null;
                while (true) {
                    final String line = buffer.readLine();
                    if (line == null) {
                        break;
                    }
                    int nameStart = -1;
                    int nameEnd = -1;
                    boolean skip = false;
                    for (int i = 0; i < line.length() && !skip; ++i) {
                        if (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '.') {
                            if (nameStart == -1) {
                                nameStart = i;
                            }
                            nameEnd = i;
                        }
                        else if (!Character.isWhitespace(line.charAt(i))) {
                            switch (line.charAt(i)) {
                                case '#': {
                                    skip = true;
                                    break;
                                }
                                case '{': {
                                    final String scopeName = line.substring(nameStart, nameEnd + 1);
                                    if (scopeName.equals("general")) {
                                        currentMap = this.generalProperties;
                                        break;
                                    }
                                    if (scopeName.equals("block")) {
                                        currentMap = this.blockProperties;
                                        break;
                                    }
                                    if (scopeName.equals("item")) {
                                        currentMap = this.itemProperties;
                                        break;
                                    }
                                    throw new RuntimeException("unknown section " + scopeName);
                                }
                                case '}': {
                                    currentMap = null;
                                    break;
                                }
                                case '=': {
                                    final String propertyName = line.substring(nameStart, nameEnd + 1);
                                    if (currentMap == null) {
                                        throw new RuntimeException("property " + propertyName + " has no scope");
                                    }
                                    final Property prop = new Property();
                                    prop.name = propertyName;
                                    prop.value = line.substring(i + 1);
                                    i = line.length();
                                    currentMap.put(propertyName, prop);
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("unknown character " + line.charAt(i));
                                }
                            }
                        }
                    }
                }
                buffer.close();
            }
        }
        catch (IOException e) {
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
                final FileOutputStream fileoutputstream = new FileOutputStream(this.file);
                final BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fileoutputstream, "8859_1"));
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeProperties(final BufferedWriter buffer, final Collection<Property> props) throws IOException {
        for (final Property property : props) {
            if (property.comment != null) {
                buffer.write("   # " + property.comment + "\r\n");
            }
            buffer.write("   " + property.name + "=" + property.value);
            buffer.write("\r\n");
        }
    }
}
