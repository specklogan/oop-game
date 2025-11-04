package org.gooseapple.core.render;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.EventManager;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.core.math.Vector2;

public class Rectangle extends AbstractRenderable {
    private Vector2 size;
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private double opacity = 1;

    public Rectangle(Vector2 size, Vector2 position) {
        super();
        this.size = size;
        this.position = position;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    @EventHandler
    public void tick(TickEvent event) {
        if (this.velocity != null) {
            this.position.add(this.velocity);
        }
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        var gc = event.getGraphicsContext();

        if (opacity != 1) {
            gc.save();
            gc.setGlobalAlpha(opacity);
        }

        if (texture == null) {
            gc.setFill(Color.BLACK);
            gc.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
        } else {
            gc.drawImage(texture.getImage(), position.getX(), position.getY(), size.getX(), size.getY());
        }

        if (opacity != 1) {
            gc.restore();
        }
    }

    @Override
    public boolean isInView(Vector2 screenSize) {
        double left = position.getX();
        double right = position.getX() + size.getX();
        double top = position.getY();
        double bottom = position.getY() + size.getY();

        double screenLeft = 0;
        double screenRight = screenSize.getX();
        double screenTop = 0;
        double screenBottom = screenSize.getY();

        boolean hVis = right > screenLeft && left < screenRight;
        boolean vVis = bottom > screenTop && top < screenBottom;

        return hVis && vVis;
    }
}
