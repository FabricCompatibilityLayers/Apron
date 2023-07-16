package io.github.betterthanupdates.apron.stapi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModContentRegistry<T> {
	public final Map<Integer, Integer> originalToAuto = new HashMap<>();
	public final Map<Integer, Integer> autoToOriginal = new HashMap<>();
	public final Map<Integer, T> originalToInstance = new HashMap<>();
	public final Map<Integer, T> autoToInstance = new HashMap<>();
	public final Map<Integer, Integer> duplicates = new HashMap<>();
	public final Map<Integer, Integer> duplicatesInverted = new HashMap<>();
	public final Map<Integer, T> duplicatesInstances = new HashMap<>();
	public final Map<Integer, T> duplicatesInstancesAuto = new HashMap<>();

	public int register(int originalId, T instance, Supplier<Integer> idSupplier) {
		if (!originalToAuto.containsKey(originalId)) {
			originalToAuto.put(originalId, idSupplier.get());
			autoToOriginal.put(originalToAuto.get(originalId), originalId);
		}

		originalToInstance.put(originalId, instance);
		autoToInstance.put(originalToAuto.get(originalId), instance);

		int newId = originalToAuto.get(originalId);

		System.out.println("Registered " + instance + " original id: " + originalId + " new id: " + newId);

		return newId;
	}

	public void registerLate(int newId, T instance) {
		if (!autoToOriginal.containsKey(newId)) {
			throw new RuntimeException("No original id assotiated with new id: " + newId + " " + instance);
		}

		int originalId = autoToOriginal.get(newId);

		originalToInstance.put(originalId, instance);
		autoToInstance.put(newId, instance);

		System.out.println("Registered " + instance + " original id: " + originalId + " new id: " + newId);
	}

	public void registerLate(int originalId, int newId, T instance) {
		originalToAuto.put(originalId, newId);
		autoToOriginal.put(newId, originalId);

		originalToInstance.put(originalId, instance);
		autoToInstance.put(newId, instance);

		System.out.println("Registered " + instance + " original id: " + originalId + " new id: " + newId);
	}

	public int registerId(int originalId, Supplier<Integer> idSupplier) {
		if (!originalToAuto.containsKey(originalId)) {
			originalToAuto.put(originalId, idSupplier.get());
		} else {
			duplicates.put(originalId, originalToAuto.put(originalId, idSupplier.get()));
			duplicatesInverted.put(duplicates.get(originalId), originalId);
			autoToOriginal.remove(duplicates.get(originalId));

			duplicatesInstances.put(originalId, originalToInstance.remove(originalId));
			int newId = duplicates.get(originalId);
			duplicatesInstancesAuto.put(newId, autoToInstance.remove(newId));
		}

		return originalToAuto.get(originalId);
	}

	public void changeOriginalId(int oldId, int newId) {
		if (originalToAuto.containsKey(newId)) {
			duplicates.put(newId, originalToAuto.remove(newId));
			duplicatesInverted.put(duplicates.get(newId), autoToOriginal.remove(newId));

			duplicatesInstances.put(newId, originalToInstance.remove(newId));
			duplicatesInstancesAuto.put(duplicates.get(newId), autoToInstance.remove(duplicates.get(newId)));
		}

		originalToAuto.put(newId, originalToAuto.remove(oldId));

		autoToOriginal.put(originalToAuto.get(newId), newId);

		originalToInstance.put(newId, originalToInstance.remove(oldId));

		System.out.println("Changed original id of "+ originalToInstance.get(newId) + " from " + oldId + " to " + newId);
	}
}
