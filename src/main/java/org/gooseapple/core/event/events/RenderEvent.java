package org.gooseapple.core.event.events;

import javafx.scene.canvas.GraphicsContext;

public class RenderEvent extends Event{
    private GraphicsContext gc;
    private double fps;

    public RenderEvent(GraphicsContext gc, double fps) {
        this.gc = gc;
        this.fps = fps;
    }

    public double getFps() {
        return fps;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }
}
