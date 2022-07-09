package guiapi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class ModAction implements Runnable, PropertyChangeListener {
	public Object dataRef;
	protected Object[] defaultArguments;
	protected ArrayList<ModAction> mergedActions = new ArrayList<>();
	protected String methodName;
	protected Class<?>[] methodParams = new Class[0];
	public String nameRef;
	protected Object objectRef;

	public ModAction(Object o, String method, Class<?>... params) {
		this.nameRef = method;
		this.methodParams = params;
		this.setupHandler(o, method);
	}

	public ModAction(Object o, String method, Object data, Class<?>... params) {
		this(o, method, params);
		this.dataRef = data;
	}

	public ModAction(Object o, String method, String name, Class<?>... params) {
		this(o, method, params);
		this.nameRef = name;
	}

	public ModAction(Object o, String method, String name, Object data, Class<?>... params) {
		this(o, method, params);
		this.nameRef = name;
		this.dataRef = data;
	}

	protected ModAction(String name) {
		this.nameRef = name;
	}

	protected Boolean argsMatch(Class<?>[] classTypes, Object[] arguments) {
		if (classTypes.length != arguments.length) {
			return false;
		} else {
			for (int i = 0; i < classTypes.length; ++i) {
				if (!classTypes[i].isAssignableFrom(arguments[i].getClass())) {
					return false;
				}
			}

			return true;
		}
	}

	public Object[] call(Object... args) throws Exception {
		try {
			if (this.mergedActions.isEmpty()) {
				return new Object[]{this.callInt(args)};
			} else {
				Object[] returnvals = new Object[this.mergedActions.size()];

				for (int i = 0; i < returnvals.length; ++i) {
					returnvals[i] = this.mergedActions.get(i).callInt(args);
				}

				return returnvals;
			}
		} catch (Exception var4) {
			var4.printStackTrace();
			throw new Exception("error calling callback '" + this.nameRef + "'.", var4);
		}
	}

	protected Object callInt(Object... args) throws Exception {
		if (!this.argsMatch(this.methodParams, args) && this.defaultArguments != null) {
			args = this.defaultArguments;
		}

		try {
			Method meth = this.GetMethodRecursively(this.objectRef, this.methodName);
			return meth.invoke(this.objectRef instanceof Class ? null : this.objectRef, args);
		} catch (Exception var3) {
			throw new Exception("error calling callback '" + this.nameRef + "'.", var3);
		}
	}

	protected Method GetMethodRecursively(Object o, String method) {
		Class<?> currentclass = o instanceof Class ? (Class<?>) o : o.getClass();

		while (true) {
			try {
				if (currentclass == null) {
					return null;
				}

				Method returnval = currentclass.getDeclaredMethod(method, this.methodParams);

				if (returnval != null) {
					returnval.setAccessible(true);
					return returnval;
				}
			} catch (Throwable var5) {
			}

			currentclass = currentclass.getSuperclass();
		}
	}

	public ModAction mergeAction(ModAction newAction) {
		if (this.mergedActions.isEmpty()) {
			ModAction merged = new ModAction("Merged ModAction");
			merged.mergedActions.add(this);
			merged.mergedActions.add(newAction);
			return merged;
		} else {
			this.mergedActions.add(newAction);
			return this;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
		if (this.methodParams.length == 1 && this.methodParams[0] == PropertyChangeEvent.class) {
			try {
				this.call(paramPropertyChangeEvent);
			} catch (Exception var3) {
				var3.printStackTrace();
				throw new RuntimeException("Error when calling PropertyChangeListener callback. Modaction is '" + this.nameRef + "'.", var3);
			}
		} else {
			throw new RuntimeException("invalid method parameters for a PropertyChangeListener callback. Modaction is '" + this.nameRef + "'.");
		}
	}

	@Override
	public void run() {
		try {
			this.call();
		} catch (Exception var2) {
			var2.printStackTrace();
			throw new RuntimeException("Error when calling Runnable callback. Modaction is '" + this.nameRef + "'.", var2);
		}
	}

	public ModAction setDefaultArguments(Object... Arguments) {
		if (!this.argsMatch(this.methodParams, Arguments)) {
			throw new InvalidParameterException("Arguments do not match the parameters.");
		} else {
			this.defaultArguments = Arguments;
			return this;
		}
	}

	protected void setupHandler(Object o, String method) {
		try {
			this.GetMethodRecursively(o, method);
		} catch (Exception var4) {
			var4.printStackTrace();
			throw new RuntimeException("Could not locate Method with included information.");
		}

		this.methodName = method;
		this.objectRef = o;
	}
}
