package modloader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MLProp {
	/**
	 * Overrides the field name for property key.
	 */
	String name() default "";
	
	/**
	 * Adds additional help to top of configuration file.
	 */
	String info() default "";
	
	/**
	 * Minimum value allowed if field is a number.
	 */
	double min() default Double.NEGATIVE_INFINITY;
	
	/**
	 * Maximum value allowed if field is a number.
	 */
	double max() default Double.POSITIVE_INFINITY;
}
