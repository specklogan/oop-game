package org.gooseapple.core.dispatcher;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.game.Game;

public class LoopDispatcher {
    private int targetFPS = 60;
    private int targetTPS = 5;

    private double accumulator = 0.0;
    private double lastUpdateTime;
    private final long startTime = System.nanoTime();

    private double frameAccumulator = 0.0;
    private double frameLastUpdateTime;

    private double now() {
        return (System.nanoTime() - startTime) / 1_000_000.0;
    }


    public LoopDispatcher(Game game) {
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

        AnimationTimer frameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now()-frameLastUpdateTime);
                frameLastUpdateTime += deltaTime;
                frameAccumulator += deltaTime;

                if (canFrame()) {
                    //Clear screen
                    game.getGraphicsContext().clearRect(0,0,1920,1080);

                    frameAccumulator -= (1000d/targetFPS);

                    RenderEvent event = new RenderEvent(game.getGraphicsContext(), deltaTime);
                    event.dispatch();
                }
            }
        };

        frameTimer.start();
        timer.start();
    }

    private boolean canFrame() {return (frameAccumulator > (1000d/targetFPS));}

    private boolean canTick() {
        return (accumulator > (1000d/targetTPS));
    }
}
