package modoptionsapi;

public class MOFormatters {
	public static final MOFormatters.DefaultFormat defaultFormat = new MOFormatters.DefaultFormat();
	public static final MOFormatters.IntegerSliderFormat integerSlider = new MOFormatters.IntegerSliderFormat();
	public static final MOFormatters.NoFormat noFormat = new MOFormatters.NoFormat();

	public MOFormatters() {
	}

	private static final class DefaultFormat implements MODisplayString {
		private DefaultFormat() {
		}

		public String manipulate(String name, String value) {
			return name + ": " + value;
		}
	}

	public static final class IntegerSliderFormat implements MODisplayString {
		public IntegerSliderFormat() {
		}

		public String manipulate(String name, String value) {
			try {
				float f = Float.parseFloat(value);
				int i = (int) f;
				return "" + i;
			} catch (NumberFormatException var5) {
				System.out.println("(MdoOptionsAPI) Could not format " + value + " into an integer");
				return name + ": " + value;
			}
		}
	}

	private static final class NoFormat implements MODisplayString {
		private NoFormat() {
		}

		public String manipulate(String name, String value) {
			return value;
		}
	}

	public static final class SuffixFormat implements MODisplayString {
		private String suffix;

		public SuffixFormat(String suffix) {
			this.suffix = suffix;
		}

		public String manipulate(String name, String value) {
			return value + " " + this.suffix;
		}
	}
}
