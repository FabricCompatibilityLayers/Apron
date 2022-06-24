package modloader;

import io.github.betterthanupdates.Legacy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Legacy
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MLProp {
	/**
	 * Overrides the field name for property key.
	 *
	 * @return field name override
	 */
	@Legacy
	String name() default "";

	/**
	 * Adds additional help to top of configuration file.
	 *
	 * @return description to add to the top of the config file
	 */
	@Legacy
	String info() default "";

	/**
	 * @return Minimum value allowed if field is a number
	 */
	@Legacy
	double min() default Double.NEGATIVE_INFINITY;

	/**
	 * @return Maximum value allowed if field is a number.
	 */
	@Legacy
	double max() default Double.POSITIVE_INFINITY;
}
