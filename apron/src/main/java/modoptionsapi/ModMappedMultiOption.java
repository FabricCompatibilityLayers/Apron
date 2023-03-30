package modoptionsapi;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModMappedMultiOption extends ModOption<Integer> {
	private LinkedHashMap<Integer, String> values = new LinkedHashMap<>();

	public ModMappedMultiOption(String name) {
		this.name = name;
	}

	public ModMappedMultiOption(String name, Integer[] keys, String[] labels) {
		if (keys.length != labels.length) {
			throw new IndexOutOfBoundsException("Keys and labels must have same # of entries");
		} else {
			this.name = name;

			for (int x = 0; x < keys.length; ++x) {
				this.addValue(keys[x], labels[x]);
			}
		}
	}

	public ModMappedMultiOption(String name, int[] keys, String[] labels) {
		if (keys.length != labels.length) {
			throw new IndexOutOfBoundsException("Keys and labels must have same # of entries");
		} else {
			this.name = name;

			for (int x = 0; x < keys.length; ++x) {
				this.addValue(Integer.valueOf(keys[x]), labels[x]);
			}
		}
	}

	public void addValue(Integer key, String value) {
		if (this.values.size() == 0) {
			this.value = key;
			this.localValue = key;
		}

		this.values.put(key, value);
	}

	public void addValue(int intKey, String value) {
		this.addValue(Integer.valueOf(intKey), value);
	}

	public String getStringValue(Integer key) {
		return this.values.get(key);
	}

	public String getStringValue(int key) {
		return this.values.get(key);
	}

	public Integer getNextValue(Integer i) {
		Integer cur = null;
		boolean found = false;
		boolean written = false;
		boolean firstFound = false;
		Integer firstKey = null;

		for (Map.Entry<Integer, String> entry : this.values.entrySet()) {
			if (!firstFound) {
				firstKey = entry.getKey();
				firstFound = true;
			}

			if (!written) {
				if (found) {
					cur = entry.getKey();
					written = true;
				}

				if (entry.getKey().equals(i)) {
					found = true;
				}
			}
		}

		if (!written) {
			cur = firstKey;
		}

		return cur;
	}

	public Integer getNextValue(int i) {
		return this.getNextValue(Integer.valueOf(i));
	}
}
