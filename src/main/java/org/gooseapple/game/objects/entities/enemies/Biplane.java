package org.gooseapple.game.objects.entities.enemies;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.game.event.BulletHitEvent;
import org.gooseapple.game.objects.Bomb;
import org.gooseapple.game.objects.Fire;
import org.gooseapple.game.objects.entities.Entity;

import java.util.Random;

public class Biplane extends Entity {

    public Biplane(Vector2 position) {
        super(new Vector2(150,90), position, "textures/entities/biplane.png");
        getPhysicsBody().setCollisionSize(new Vector2(120,40));
        getPhysicsBody().setAffectedByGravity(false);
        getPhysicsBody().setCollisionEnabled(true);
        setMaxHealth(60);
        setTextureOffset(new Vector2(-10, -25));
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        super.render(event);

        if (getPhysicsBody().getPosition().getY() > event.getScreenSize().getY() + 20) {
            remove();
        }
    }

    private long lastDrop = 0;
    private int dropDelay = 6;
    @EventHandler
    public void tick(TickEvent event) {
        Random rand = new Random();
        if (System.currentTimeMillis() - lastDrop > (dropDelay * 1000)) {
            dropDelay = rand.nextInt(4,12);
            lastDrop = System.currentTimeMillis();
            if (this.getPosition().getX() < event.getScreenSize().getX() - 200 && this.getHealth() > 0) {
                new Bomb(this.getPosition().clone());
            }
        }
    }

    @EventHandler
    public void onHitWithBullet(BulletHitEvent event) {
        if(event.getEntity() == this){
            damage(event.getBullet().getDamage());

            var random = new Random();
            if (random.nextInt(100) < 25) {
                Fire fire = new Fire(new Vector2(event.getBullet().getPosition().getX(), event.getBullet().getPosition().getY() - 25));
                fire.getPhysicsBody().setVelocity(this.getPhysicsBody().getVelocity());
            }

            if (getHealth() <= 0) {
                this.getPhysicsBody().setCollisionEnabled(false);
                this.getPhysicsBody().getVelocity().add(new  Vector2(0,1.25));
            }
        }
    }
}
