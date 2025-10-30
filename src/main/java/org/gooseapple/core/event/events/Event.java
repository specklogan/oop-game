package org.gooseapple.core.event.events;

import org.gooseapple.core.event.EventManager;
import org.gooseapple.core.event.ListenerPriority;

/**
 * Abstract event class, used in the other events
 */
public abstract class Event {
    private boolean cancelled;
    private long time = System.currentTimeMillis();

    public void dispatch() {
        //For every value in the ListenerPriority, fire its associated priority
        for (int i = 0; i < ListenerPriority.values().length; i++) {
            EventManager.getInstance().dispatchEvent(this, ListenerPriority.values()[i]);
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public double getTime() {
        return this.time;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}