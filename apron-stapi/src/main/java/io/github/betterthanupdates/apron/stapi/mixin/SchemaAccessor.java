package io.github.betterthanupdates.apron.stapi.mixin;

import java.util.Map;
import java.util.function.Supplier;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TypeTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Schema.class)
public interface SchemaAccessor {
	@Mutable
	@Accessor(remap = false)
	void setTYPES(Map<String, Type<?>> map);
	@Accessor(remap = false)
	Map<String, Supplier<TypeTemplate>> getTYPE_TEMPLATES();
}
