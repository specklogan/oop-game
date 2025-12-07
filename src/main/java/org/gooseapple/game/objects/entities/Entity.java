package org.gooseapple.game.objects.entities;

import javafx.scene.paint.Color;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;

public class Entity extends Rectangle {
    private double health = 100;
    private double maxHealth = 100;
    private boolean renderHealth = true;

    public Entity(Vector2 size, Vector2 position, String texturePath) {
        super(size, position, true, texturePath);
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void damage(double amount) {
        this.health -= amount;
    }

    @Override
    public void render(RenderEvent event) {

        super.render(event);

        if (renderHealth && health > 0) {
            Vector2 topLeft = this.getPosition().clone();
            topLeft.add(new Vector2(20, -20));

            event.getGraphicsContext().setFill(Color.RED);
            event.getGraphicsContext().fillRect(topLeft.getX(), topLeft.getY(), 60, 15);

            event.getGraphicsContext().setFill(Color.GREEN);
            event.getGraphicsContext().fillRect(topLeft.getX(), topLeft.getY(), 60 * (health/maxHealth), 15);

        }
    }
}
