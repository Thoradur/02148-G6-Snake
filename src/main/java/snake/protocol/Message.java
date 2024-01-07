package snake.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Message {
    // The name of the message type
    String name() default "";

    // Whether the message is sent as the class
    // or as individual fields.
    // defaults to fields.
    boolean compact() default false;
}
