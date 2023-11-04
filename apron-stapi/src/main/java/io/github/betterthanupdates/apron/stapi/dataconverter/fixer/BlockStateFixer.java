package io.github.betterthanupdates.apron.stapi.dataconverter.fixer;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import io.github.betterthanupdates.apron.stapi.dataconverter.ModDatabase;

public class BlockStateFixer extends DataFix {
	private final ModDatabase database;
	public BlockStateFixer(ModDatabase database, Schema outputSchema) {
		super(outputSchema, false);
		this.database = database;
	}

	@Override
	protected TypeRewriteRule makeRule() {
		return writeFixAndRead(
				this.database.getName() + "_BlockStateFix",
				getInputSchema().getType(TypeReferences.BLOCK_STATE),
				getOutputSchema().getType(TypeReferences.BLOCK_STATE),
				dynamic -> {
					var dyn = dynamic.get("Name").result();

					String id = "minecraft:air";

					if (dyn.isPresent()) {
						id = dyn.get().asString("minecraft:air");
					}

					dynamic = dynamic.set("Name", dynamic.createString(this.database.block(id)));

					dynamic = this.database.blockState(id, dynamic);

					return dynamic;
				}
		);
	}
}
