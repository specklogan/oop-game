package org.gooseapple.level;

import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.EventManager;

/**
 * Base class for any world, contains functions like ticking, rendering
 * and allows easy world creation.
 */
public abstract class Level implements EventListener {
    private boolean enabled = false;

    public Level() {
        EventManager.getInstance().addListener(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}