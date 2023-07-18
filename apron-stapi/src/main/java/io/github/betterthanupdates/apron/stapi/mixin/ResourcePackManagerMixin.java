package io.github.betterthanupdates.apron.stapi.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import net.modificationstation.stationapi.impl.resource.ResourcePackManager;
import net.modificationstation.stationapi.impl.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.betterthanupdates.apron.stapi.MLModPackProvider;

@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSet;copyOf([Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;", remap = false))
	private ImmutableSet<ResourcePackProvider> addMLProvider(Object[] providers) {
		List<ResourcePackProvider> newList = new ArrayList<>();
		for (Object o : providers) {
			newList.add((ResourcePackProvider) o);
		}
		newList.add(new MLModPackProvider());

		return ImmutableSet.copyOf(newList);
	}
}
