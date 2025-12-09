package org.gooseapple.game.objects;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.EventManager;
import org.gooseapple.core.event.events.CollisionEvent;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.game.event.BulletHitEvent;
import org.gooseapple.game.event.DestroyBulletEvent;
import org.gooseapple.game.objects.entities.Entity;
import org.gooseapple.game.objects.entities.train.Carriage;

import java.util.Random;

public class Bullet extends Rectangle {
    private int damage = 10;
    private int ranTicksBeforeExplosion;

    public Bullet(Vector2 position) {
        super(new Vector2(2,2), position);
        Random random = new Random();
        ranTicksBeforeExplosion = random.nextInt(10,60);

        getPhysicsBody().setAffectedByGravity(true);
        getPhysicsBody().setCollisionEnabled(true);
    }

    public int getDamage() {
        return damage;
    }

    @EventHandler
    public void tick(TickEvent event) {
        if (this.getPhysicsBody().getVelocity().getY() > 0) {
            ranTicksBeforeExplosion -= 1;
            if (ranTicksBeforeExplosion == 0) {
                this.remove();
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        DestroyBulletEvent bulletEvent = new DestroyBulletEvent(this);
        bulletEvent.dispatch();
    }

    @EventHandler
    public void handleCollision(CollisionEvent event) {
        var firstBody = event.getFirstBody();
        var secondBody = event.getSecondBody();

        if (firstBody != getPhysicsBody() && secondBody != getPhysicsBody()) {
            return;
        }

        var otherBody = (firstBody == getPhysicsBody()) ? secondBody : firstBody;

        //Make sure that we didn't hit a train
        if (otherBody.getParent() instanceof Entity entity && !(otherBody.getParent() instanceof Carriage)) {
            BulletHitEvent bulletEvent = new BulletHitEvent(this, entity);
            bulletEvent.dispatch();
            this.remove();
        }
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        super.render(event);
    }

    public void setDamage(double damage) {
        this.damage = (int) damage;
    }
}
