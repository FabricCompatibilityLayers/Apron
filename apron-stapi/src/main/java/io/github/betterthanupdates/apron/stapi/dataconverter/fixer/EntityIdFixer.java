package io.github.betterthanupdates.apron.stapi.dataconverter.fixer;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import io.github.betterthanupdates.apron.stapi.dataconverter.ModDatabase;

public class EntityIdFixer extends DataFix {
	private final ModDatabase database;
	public EntityIdFixer(ModDatabase database, Schema outputSchema) {
		super(outputSchema, false);
		this.database = database;
	}

	@Override
	protected TypeRewriteRule makeRule() {
		return writeFixAndRead(
				this.database.getName() + "_EntityIdFix",
				getInputSchema().getType(TypeReferences.ENTITY),
				getOutputSchema().getType(TypeReferences.ENTITY),
				dynamic -> dynamic.get("id").result().<Dynamic<?>>map(
						value -> dynamic.set("id", dynamic.createString(
								this.database.entity(value.asString("Pig"))
						))
				).orElse(dynamic)
		);
	}
}
