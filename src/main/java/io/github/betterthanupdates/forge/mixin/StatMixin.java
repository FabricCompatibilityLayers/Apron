package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.stat.BabricatedStat;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Stat.class)
public class StatMixin implements BabricatedStat {
    @Mutable
    @Shadow @Final public String name;

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
