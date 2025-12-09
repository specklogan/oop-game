package org.gooseapple.game.objects;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.CollisionEvent;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.render.Texture;
import org.gooseapple.core.sound.Sound;
import org.gooseapple.game.event.BombEvent;
import org.gooseapple.game.event.BulletHitEvent;
import org.gooseapple.game.objects.entities.Entity;
import org.gooseapple.game.objects.entities.train.Carriage;

public class Bomb extends Rectangle {
    private int damage = 15;
    private Sound sound = new Sound("/sound/bomb_whistle.mp3");

    public Bomb(Vector2 position) {
        super(new Vector2(11,11), position, true, "textures/bomb.png");
        getPhysicsBody().setCollisionEnabled(true);
        getPhysicsBody().setAffectedByGravity(true);
        sound.setVolume(0.015);
        sound.play();
    }

    public int getDamage() {
        return damage;
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        super.render(event);

        if (getPosition().getY() > event.getScreenSize().getY()) {
            this.remove();
        }
    }

    @EventHandler
    public void handleCollision(CollisionEvent event) {
        var firstBody = event.getFirstBody();
        var secondBody = event.getSecondBody();

        if (firstBody != getPhysicsBody() && secondBody != getPhysicsBody()) {
            return;
        }

        if (firstBody.getParent() instanceof Carriage c) {
            BombEvent bombEvent = new BombEvent(this, c);
            bombEvent.dispatch();
            this.remove();
        }

        if (secondBody.getParent() instanceof Carriage c) {
            BombEvent bombEvent = new BombEvent(this, c);
            bombEvent.dispatch();
            this.remove();
        }
    }
}
