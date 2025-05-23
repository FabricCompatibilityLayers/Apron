package io.github.betterthanupdates.modloader.mixin.client;

import java.awt.*;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import modloader.BaseMod;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_622;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(class_622.class)
public abstract class GameStartupErrorPanelMixin extends Panel {
	private static StringWriter stringWriter = new StringWriter();

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/io/PrintWriter;<init>(Ljava/io/Writer;)V"))
	private Writer modloader$overwriteStringWriter(Writer out) {
		stringWriter = new StringWriter();
		return stringWriter;
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/awt/TextArea;<init>(Ljava/lang/String;III)V"))
	private String modloader$overwriteString(String text) {
		String str1 = stringWriter.toString();
		String str2 = "";
		String str3 = "";

		try {
			str3 = str3 + "Generated " + new SimpleDateFormat().format(new Date()) + "\n";
			str3 = str3 + "\n";
			str3 = str3 + "Minecraft: Minecraft Beta 1.7.3\n";
			str3 = str3
					+ "OS: "
					+ System.getProperty("os.name")
					+ " ("
					+ System.getProperty("os.arch")
					+ ") version "
					+ System.getProperty("os.version")
					+ "\n";
			str3 = str3 + "Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor") + "\n";
			str3 = str3
					+ "VM: "
					+ System.getProperty("java.vm.name")
					+ " ("
					+ System.getProperty("java.vm.info")
					+ "), "
					+ System.getProperty("java.vm.vendor")
					+ "\n";
			str3 = str3 + "LWJGL: " + Sys.getVersion() + "\n";
			str2 = GL11.glGetString(7936);
			str3 = str3 + "OpenGL: " + GL11.glGetString(7937) + " version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936) + "\n";
		} catch (Throwable e) {
			str3 = str3 + "[failed to get system properties (" + e + ")]\n";
		}

		str3 = str3 + "\n";
		str3 = str3 + str1;
		String str4 = "";
		str4 = str4 + "Mods loaded: " + (ModLoader.getLoadedMods().size() + 1) + "\n";
		str4 = str4 + "ModLoader Beta 1.7.3" + "\n";

		for (BaseMod mod : ModLoader.getLoadedMods()) {
			str4 += mod.getClass().getName() + " " + mod.Version() + "\n";
		}

		str4 = str4 + "\n";

		if (str1.contains("Pixel format not accelerated")) {
			str4 = str4 + "      Bad video card drivers!      \n";
			str4 = str4 + "      -----------------------      \n";
			str4 = str4 + "\n";
			str4 = str4 + "Minecraft was unable to start because it failed to find an accelerated OpenGL mode.\n";
			str4 = str4 + "This can usually be fixed by updating the video card drivers.\n";

			if (str2.toLowerCase().contains("nvidia")) {
				str4 = str4 + "\n";
				str4 = str4 + "You might be able to find drivers for your video card here:\n";
				str4 = str4 + "  http://www.nvidia.com/\n";
			} else if (str2.toLowerCase().contains("ati")) {
				str4 = str4 + "\n";
				str4 = str4 + "You might be able to find drivers for your video card here:\n";
				str4 = str4 + "  http://www.amd.com/\n";
			}
		} else {
			str4 = str4 + "      Minecraft has crashed!      \n";
			str4 = str4 + "      ----------------------      \n";
			str4 = str4 + "\n";
			str4 = str4 + "Minecraft has stopped running because it encountered a problem.\n";
			str4 = str4 + "\n";
			str4 = str4 + "If you wish to report this, please copy this entire text and email it to support@mojang.com.\n";
			str4 = str4 + "Please include a description of what you did when the error occured.\n";
		}

		str4 = str4 + "\n";
		str4 = str4 + "\n";
		str4 = str4 + "\n";
		str4 = str4 + "--- BEGIN ERROR REPORT " + Integer.toHexString(str4.hashCode()) + " --------\n";
		str4 = str4 + str3;
		str4 = str4 + "--- END ERROR REPORT " + Integer.toHexString(str4.hashCode()) + " ----------\n";
		str4 = str4 + "\n";
		str4 = str4 + "\n";

		return str4;
	}
}
