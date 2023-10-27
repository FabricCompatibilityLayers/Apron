package io.github.betterthanupdates.apron.stapi.dataconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.datafixer.DataFixers;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.registry.ModID;

import io.github.betterthanupdates.apron.stapi.dataconverter.fixer.BlockIdFixer;
import io.github.betterthanupdates.apron.stapi.dataconverter.fixer.EntityIdFixer;
import io.github.betterthanupdates.apron.stapi.dataconverter.fixer.ItemIdFixer;

public abstract class ModDatabase implements Function<Executor, DataFixer> {
	private final ModID original, target;

	private final Map<String, String>
			ITEMS = new HashMap<>(),
			BLOCKS = new HashMap<>(),
			ENTITIES = new HashMap<>();

	public ModDatabase(ModID original, ModID target) {
		this.original = original;
		this.target = target;
	}

	public Schema baseSchema(int version, Schema schema) {
		return new BaseSchema(version, this, schema);
	}

	@Override
	public DataFixer apply(Executor executor) {
		DataFixerBuilder builder = new DataFixerBuilder(this.getVersion());

		Schema schema = builder.addSchema(1, this::baseSchema);
		builder.addFixer(new ItemIdFixer(this, schema));
		builder.addFixer(new EntityIdFixer(this, schema));
		builder.addFixer(new BlockIdFixer(this, schema));

		return builder.buildOptimized(Set.of(TypeReferences.LEVEL), executor);
	}

	public void registerDataFixer() {
		DataFixers.registerFixer(this.original, this, this.getVersion());
	}

	public String getName() {
		return this.original.toString() + "_To_" + this.target.toString();
	}

	public boolean isTargetPresent() {
		return FabricLoader.getInstance().isModLoaded(this.target.toString());
	}

	public int getVersion() {
		return this.isTargetPresent() ? 1 : 0;
	}

	public abstract void register();

	public final Properties loadConfigFile() {
		return this.loadConfigFile(FabricLoader.getInstance().getConfigDir().resolve(this.original.toString() + ".cfg").toFile());
	}

	public final Properties loadConfigFile(File file) {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(file));
		} catch (IOException ignored) {
		}

		return properties;
	}

	public void item(int old, String id) {
		this.item(String.valueOf(old), id);
	}
	public void item(String old, String id) {
		this.ITEMS.put(
				this.original.id(old).toString(),
				this.target.id(id).toString()
		);
	}

	public String item(String old) {
		return this.ITEMS.getOrDefault(old, old);
	}

	public void block(int old, String id) {
		this.block(String.valueOf(old), id);
	}
	public void block(String old, String id) {
		this.BLOCKS.put(
				this.original.id(old).toString(),
				this.target.id(id).toString()
		);
	}

	public String block(String old) {
		return this.BLOCKS.getOrDefault(old, old);
	}

	public void blockAndItem(int old, String id) {
		this.blockAndItem(String.valueOf(old), id);
	}
	public void blockAndItem(String old, String id) {
		this.block(old, id);
		this.item(old, id);
	}

	public void entity(String old, String id) {
		this.ENTITIES.put(
				old,
				id
		);
	}

	public String entity(String old) {
		return this.ENTITIES.getOrDefault(old, old);
	}

	public void entitySchema(Schema schema, Map<String, Supplier<TypeTemplate>> map) {
		for (Map.Entry<String, String> entry: this.ENTITIES.entrySet()) {
			schema.registerSimple(map, entry.getKey());
			schema.registerSimple(map, entry.getValue());
		}
	}
}
