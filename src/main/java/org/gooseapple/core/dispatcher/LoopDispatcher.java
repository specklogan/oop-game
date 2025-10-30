package org.gooseapple.core.dispatcher;

import javafx.animation.AnimationTimer;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.events.TickEvent;

public class LoopDispatcher {
    private int targetTPS = 1;

    private double accumulator = 0.0;
    private double lastUpdateTime;
    private final long startTime = System.nanoTime();

    private double now() {
        return (System.nanoTime() - startTime) / 1_000_000.0;
    }

    public LoopDispatcher() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now()-lastUpdateTime);
                lastUpdateTime += deltaTime;
                accumulator += deltaTime;

                if (canTick()) {
                    accumulator -= (1000d/targetTPS);

                    TickEvent event = new TickEvent();
                    event.dispatch();
                }
            }
        };

        timer.start();
    }

    private boolean canTick() {
        return (accumulator > (1000d/targetTPS));
    }
}
