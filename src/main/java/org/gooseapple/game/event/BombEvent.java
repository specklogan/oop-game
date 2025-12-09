package org.gooseapple.game.event;

import org.gooseapple.core.event.events.Event;
import org.gooseapple.game.objects.Bomb;
import org.gooseapple.game.objects.Bullet;
import org.gooseapple.game.objects.entities.Entity;

public class BombEvent extends Event {
    private Bomb bomb;
    private Entity entity;

    public BombEvent(Bomb bomb,  Entity entity) {
        this.bomb = bomb;
        this.entity = entity;
    }

    public Bomb getBomb() {
        return this.bomb;
    }

    public Entity getEntity() {
        return entity;
    }
}
