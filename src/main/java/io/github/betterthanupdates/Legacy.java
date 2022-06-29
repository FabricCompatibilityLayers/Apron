package io.github.betterthanupdates;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated member is legacy, and will not receive API updates.<br>
 * Private members may be updated in patches, but public members shall keep their method descriptors.<br>
 * Public members may be added for convenience, but never removed.
 *
 * @author halotroop2288
 * @since 1.7.3+build.1
 */
@Documented
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, PACKAGE})
@Retention(RetentionPolicy.SOURCE)
public @interface Legacy {
}
