package io.github.betterthanupdates.apron.stapi.dataconverter.fixer;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import io.github.betterthanupdates.apron.stapi.dataconverter.ModDatabase;

public class BlockIdFixer extends DataFix {
	private final ModDatabase database;
	public BlockIdFixer(ModDatabase database, Schema outputSchema) {
		super(outputSchema, false);
		this.database = database;
	}

	@Override
	protected TypeRewriteRule makeRule() {
		return writeFixAndRead(
				this.database.getName() + "_BlockStateIdFix",
				getInputSchema().getType(TypeReferences.BLOCK_STATE),
				getOutputSchema().getType(TypeReferences.BLOCK_STATE),
				dynamic -> dynamic.get("Name").result().<Dynamic<?>>map(
						value -> dynamic.set("Name", dynamic.createString(
								this.database.block(value.asString("minecraft:air"))
						))
				).orElse(dynamic)
		);
	}
}
