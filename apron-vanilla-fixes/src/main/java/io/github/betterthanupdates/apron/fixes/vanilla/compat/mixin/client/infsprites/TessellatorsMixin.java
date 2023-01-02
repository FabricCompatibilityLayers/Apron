package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.infsprites;

import net.mine_diver.infsprites.render.Tessellators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Tessellators.class)
public class TessellatorsMixin {
	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "a"))
	private static String fixJoinWorld1(String constant) {
		return "field_2054";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "instance"))
	private static String fixJoinWorld2(String constant) {
		return "INSTANCE";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "s"))
	private static String fixJoinWorld3(String constant) {
		return "field_2072";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "t"))
	private static String fixJoinWorld4(String constant) {
		return "field_2073";
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(stringValue = "u"))
	private static String fixJoinWorld5(String constant) {
		return "field_2074";
	}
}
