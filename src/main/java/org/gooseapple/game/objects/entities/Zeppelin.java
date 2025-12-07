package org.gooseapple.game.objects.entities;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.game.event.BulletHitEvent;

public class Zeppelin extends Entity {

    public Zeppelin(Vector2 position) {
        super(new Vector2(150,90), position, "textures/entities/zeppelin.png");
        getPhysicsBody().setCollisionSize(new Vector2(120,40));
        getPhysicsBody().setAffectedByGravity(false);
        getPhysicsBody().setCollisionEnabled(true);

        setTextureOffset(new Vector2(-10, -25));
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        super.render(event);
    }

    @EventHandler
    public void onHitWithBullet(BulletHitEvent event) {
        if(event.getEntity() == this){
            damage(event.getBullet().getDamage());

            if (getHealth() <= 0) {
                getPhysicsBody().setAffectedByGravity(true);
            }
        }
    }
}
