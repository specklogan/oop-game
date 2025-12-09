package org.gooseapple.game.objects;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.CollisionEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.render.Texture;
import org.gooseapple.game.event.BombEvent;
import org.gooseapple.game.event.BulletHitEvent;
import org.gooseapple.game.objects.entities.Entity;
import org.gooseapple.game.objects.train.Carriage;

public class Bomb extends Rectangle {
    private int damage = 20;

    public Bomb(Vector2 size, Vector2 position) {
        super(size, position, true, "textures/bomb.png");
    }

    public int getDamage() {
        return damage;
    }

    @EventHandler
    public void handleCollision(CollisionEvent event) {
        var firstBody = event.getFirstBody();
        var secondBody = event.getSecondBody();

        if (firstBody != getPhysicsBody() && secondBody != getPhysicsBody()) {
            return;
        }

        var otherBody = (firstBody == getPhysicsBody()) ? secondBody : firstBody;

        if (otherBody.getParent() instanceof Entity entity && !(otherBody.getParent() instanceof Carriage carriage)) {
            BombEvent bombEvent = new BombEvent(this, entity);
            bombEvent.dispatch();
            this.remove();
        }
    }
}
