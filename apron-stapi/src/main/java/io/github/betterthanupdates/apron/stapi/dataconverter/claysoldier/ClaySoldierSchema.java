package io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier;

import com.mojang.datafixers.schemas.Schema;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.HashMap;
import java.util.Map;

public class ClaySoldierSchema extends Schema {
    private static final Map<String, String> OLD_TO_NEW = new HashMap<>();

    private static void register(int oldId, String newId) {
        OLD_TO_NEW.put(ModID.of("mod_ClayMan").id(String.valueOf(oldId)).toString(), ModID.of("claysoldiers").id(newId).toString());
    }

    static {
        register(6849, "claydisruptor");
        register(6850, "claydoll");
        register(6851, "reddoll");
        register(6852, "yellowdoll");
        register(6853, "greendoll");
        register(6854, "bluedoll");
        register(6855, "horsedoll");
        register(6856, "orangedoll");
        register(6857, "purpledoll");
    }

    protected static String convertItemId(String id) {
        return OLD_TO_NEW.getOrDefault(id, id);
    }

    public ClaySoldierSchema(int versionKey, Schema parent) {
        super(versionKey, parent);
    }
}
