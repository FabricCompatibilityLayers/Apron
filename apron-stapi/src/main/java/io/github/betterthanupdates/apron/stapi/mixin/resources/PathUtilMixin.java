package io.github.betterthanupdates.apron.stapi.mixin.resources;

import com.mojang.serialization.DataResult;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.PathUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(PathUtil.class)
public abstract class PathUtilMixin {
	@Shadow(remap = false)
	public static boolean isFileNameValid(String name) {
		return false;
	}

	/**
	 * @author Cat Core
	 * @reason don't crash if the filename "is not valid"
	 */
	@Overwrite(remap = false)
	public static DataResult<List<String>> split(String path) {
		int i = path.indexOf(47);

		if (i == -1) {
			return switch (path) {
				case "", ".", ".." -> DataResult.error(() -> "Invalid path '" + path + "'");
				default ->
						!isFileNameValid(path) ? DataResult.error(() -> "Invalid path '" + path + "'") : DataResult.success(List.of(path));
			};
		} else {
			List<String> list = new ArrayList<>();
			int j = 0;
			boolean bl = false;

			while(true) {
				String string;

				switch (string = path.substring(j, i)) {
					case "", ".", ".." -> {
						return DataResult.error(() -> "Invalid segment '" + string + "' in path '" + path + "'");
					}
				}

				if (!isFileNameValid(string))
					if (hasUpperCaseChars(string)) {
						StationAPI.LOGGER.warn("Invalid segment '" + string + "' in path '" + path + "'");
					} else {
						return DataResult.error(() -> "Invalid segment '" + string + "' in path '" + path + "'");
					}

				list.add(string);

				if (bl) return DataResult.success(list);

				j = i + 1;
				i = path.indexOf(47, j);

				if (i == -1) {
					i = path.length();
					bl = true;
				}
			}
		}
	}

	/**
	 * @author Cat Core
	 * @reason don't crash if the filename "is not valid"
	 */
	@Overwrite(remap = false)
	public static void validatePath(String... paths) {
		if (paths.length == 0) throw new IllegalArgumentException("Path must have at least one element");
		else {
			int var2 = paths.length;

			for(int var3 = 0; var3 < var2; ++var3) {
				String string = paths[var3];

				if (string.equals("..") || string.equals(".")) {
					throw new IllegalArgumentException("Illegal segment " + string + " in path " + Arrays.toString(paths));
				} else if (!isFileNameValid(string)) {
					if (hasUpperCaseChars(string)) {
						StationAPI.LOGGER.warn("Illegal segment " + string + " in path " + Arrays.toString(paths));
					} else {
						throw new IllegalArgumentException("Illegal segment " + string + " in path " + Arrays.toString(paths));
					}
				}
			}

		}
	}

	private static boolean hasUpperCaseChars(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (Character.isUpperCase(string.charAt(i))) {
				return true;
			}
		}

		return false;
	}
}
