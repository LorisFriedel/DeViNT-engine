package polytech.devint.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to create method event handlers.
 *
 * @author Günther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventHandler {

}
