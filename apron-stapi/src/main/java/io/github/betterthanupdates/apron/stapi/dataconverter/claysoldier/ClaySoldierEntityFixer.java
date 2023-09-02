package io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;

import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public class ClaySoldierEntityFixer extends DataFix {
    private final String name;
    public ClaySoldierEntityFixer(String name, Schema outputSchema) {
        super(outputSchema, false);
        this.name = name;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return writeFixAndRead(
                this.name,
                getInputSchema().getType(TypeReferences.ENTITY),
                getOutputSchema().getType(TypeReferences.ENTITY),
                dynamic -> dynamic.get("id").result().<Dynamic<?>>map(
                        value -> dynamic.set("id", dynamic.createString(
                                ClaySoldierSchema.convertEntityId(value.asString("Pig"))
                        ))
                ).orElse(dynamic)
        );
    }
}
