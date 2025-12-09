package org.gooseapple.game.objects.train;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.render.Texture;
import org.gooseapple.game.objects.Bullet;
import org.gooseapple.game.objects.entities.Entity;

import java.util.ArrayList;

public class Carriage extends Entity {
    private Carriage previousCarriage;
    private Carriage nextCarriage;

    private ArrayList<Turret> turrets =  new ArrayList<Turret>();
    private boolean debugTurretPosition = false;

    private int health = 100;
    private int maxHealth = 100;

    public Carriage(Vector2 position, String texture) {
        super(new Vector2(90,40), position, texture);
    }

    public Carriage getPreviousCarriage() {
        return previousCarriage;
    }

    public void addCarriageToEnd(Carriage carriage) {
        carriage.setPosition(this.getPosition().clone());
        if (this.previousCarriage != null) {
            this.previousCarriage.addCarriageToEnd(carriage);
        } else {
            setPreviousCarriage(carriage);
        }
    }

    public void loadCarriage() {
        if (this.previousCarriage != null) {
            this.previousCarriage.loadCarriage();
        }
    }

    public boolean hasTurrets() {
        return turrets.size() > 0;
    }

    public void addTurret(Turret turret) {
        this.turrets.add(turret);
    }

    public void fireTurrets(Vector2 target) {
        for (Turret turret : turrets) {
            turret.fire(target);
        }

        if (this.previousCarriage != null) {
            this.previousCarriage.fireTurrets(target);
        }
    }

    public void setPreviousCarriage(Carriage previousCarriage) {
        this.previousCarriage = previousCarriage;
        this.previousCarriage.setNextCarriage(this);
    }

    public Carriage getNextCarriage() {
        return nextCarriage;
    }

    public void setNextCarriage(Carriage nextCarriage) {
        this.nextCarriage = nextCarriage;
        this.setPosition(new Vector2(this.getPosition().getX() - 90, this.getPosition().getY()));
        //Set the position of any previous carriages as well as this one
        if (this.previousCarriage != null) {
            this.previousCarriage.setNextCarriage(this);
        }
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        super.render(event);
        if (debugTurretPosition && turrets.size() > 0) {
            for(Turret turret : turrets) {
                event.getGraphicsContext().fillRect(turret.getPosition().getX(), turret.getPosition().getY(), 20, 20);

            }
        }
    }
}
