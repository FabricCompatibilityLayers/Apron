package io.github.betterthanupdates.apron.stapi.dataconverter.fixer;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import io.github.betterthanupdates.apron.stapi.dataconverter.ModDatabase;

public class ItemIdFixer extends DataFix {
	private final ModDatabase database;
	public ItemIdFixer(ModDatabase database, Schema outputSchema) {
		super(outputSchema, false);
		this.database = database;
	}

	@Override
	protected TypeRewriteRule makeRule() {
		return writeFixAndRead(
				this.database.getName() + "_ItemStackIdFix",
				getInputSchema().getType(TypeReferences.ITEM_STACK),
				getOutputSchema().getType(TypeReferences.ITEM_STACK),
				dynamic -> dynamic.get(STATION_ID).result().<Dynamic<?>>map(
						value -> dynamic.set(STATION_ID, dynamic.createString(
								this.database.item(value.asString("minecraft:air"))
						))
				).orElse(dynamic)
		);
	}
}
