package io.github.betterthanupdates.forge.mixin.babricated.stat;

import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Stats.class)
public interface StatsAccessor {

    @Accessor
    static Map getIdMap() {
        return null;
    }
}
