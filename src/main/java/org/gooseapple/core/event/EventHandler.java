package org.gooseapple.core.event;

import java.lang.annotation.*;

/**
 * Eventhandler annotation, used to mark methods that are inside of a listener so they can be called upon an event
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    ListenerPriority priority() default ListenerPriority.NORMAL;
}
