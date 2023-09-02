package io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public class ClaySoldierItemFixer extends DataFix {
    private final String name;
    public ClaySoldierItemFixer(String name, Schema outputSchema) {
        super(outputSchema, false);
        this.name = name;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return writeFixAndRead(
                this.name,
                getInputSchema().getType(TypeReferences.ITEM_STACK),
                getOutputSchema().getType(TypeReferences.ITEM_STACK),
                dynamic -> dynamic.get(STATION_ID).result().<Dynamic<?>>map(
                        value -> dynamic.set(STATION_ID, dynamic.createString(
                                ClaySoldierSchema.convertItemId(value.asString("minecraft:air"))
                        ))
                ).orElse(dynamic)
        );
    }
}
