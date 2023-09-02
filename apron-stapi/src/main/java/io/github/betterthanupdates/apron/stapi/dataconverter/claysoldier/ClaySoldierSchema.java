package io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import io.github.betterthanupdates.apron.stapi.dataconverter.BaseSchema;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClaySoldierSchema extends BaseSchema {
    private static final Map<String, String> ITEMS = new HashMap<>();
    private static final Map<String, String> ENTITIES = new HashMap<>();

    private static void registerItem(int oldId, String newId) {
        ITEMS.put(ModID.of("mod_ClayMan").id(String.valueOf(oldId)).toString(), ModID.of("claysoldiers").id(newId).toString());
    }

    private static void registerEntity(String oldId, String newId) {
        ENTITIES.put(oldId, newId);
    }

    static {
        registerItem(256 + 6849, "claydisruptor");
        registerItem(256 + 6850, "claydoll");
        registerItem(256 + 6851, "reddoll");
        registerItem(256 + 6852, "yellowdoll");
        registerItem(256 + 6853, "greendoll");
        registerItem(256 + 6854, "bluedoll");
        registerItem(256 + 6855, "horsedoll");
        registerItem(256 + 6856, "orangedoll");
        registerItem(256 + 6857, "purpledoll");
        registerEntity("ClaySoldier", "claysoldier");
        registerEntity("DirtHorse", "dirthorse");
        registerEntity("GravelChunk", "gravelchunk");
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);

        registerSimple(map, "ClaySoldier");
        registerSimple(map, "DirtHorse");
        registerSimple(map, "GravelChunk");
        registerSimple(map, "claysoldier");
        registerSimple(map, "dirthorse");
        registerSimple(map, "gravelchunk");

        return map;
    }

    protected static String convertItemId(String id) {
        return ITEMS.getOrDefault(id, id);
    }

    protected static String convertEntityId(String id) {
        return ENTITIES.getOrDefault(id, id);
    }

    public ClaySoldierSchema(int versionKey, Schema parent) {
        super(versionKey, parent);
    }
}
