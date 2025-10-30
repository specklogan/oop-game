package org.gooseapple.core.event.events;

import org.gooseapple.core.math.Vector2;

public class MouseClickEvent extends Event{
    private Vector2 mousePosition;
    private MouseClickType clickType;

    public MouseClickEvent(Vector2 mousePosition, MouseClickType clickType) {
        this.mousePosition = mousePosition;
        this.clickType = clickType;
    }

    public Vector2 getMousePosition() {
        return this.mousePosition;
    }

    public MouseClickType getClickType() {
        return this.clickType;
    }

    public enum MouseClickType {
        LEFT, RIGHT, MIDDLE
    }
}
