package io.github.betterthanupdates.apron.stapi.blockhelper;

import java.util.ArrayList;
import java.util.List;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;

public class TooltipRegistrar {

	private static final List<TooltipBuilder> tooltipBuilders = new ArrayList<>();

	@EventListener
	public final void onBlocksInit(TooltipBuildEvent tooltipEvent) {
		tooltipBuilders.forEach(b -> b.handleStack(tooltipEvent.itemStack, tooltipEvent::add));
	}

	public static void registerTooltipBuilder(TooltipBuilder builder) {
		tooltipBuilders.add(builder);
	}

	public interface TooltipBuilder {
		void handleStack(ItemStack stack, LineAdder lineAdder);
	}

	/* Compat for Block Helper - its JDK target is 6... Too low for regular Consumer<String>s */
	public interface LineAdder {
		void addLine(String line);
	}

}
