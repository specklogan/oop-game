package org.gooseapple.core.event.events;

import org.gooseapple.core.math.Vector2;

public class TickEvent extends Event {

    private double deltaTime;
    private Vector2 screenSize;

    public TickEvent(double deltaTime, Vector2 screenSize) {
        this.deltaTime = deltaTime;
        this.screenSize = screenSize;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public Vector2 getScreenSize() {
        return screenSize;
    }
}
