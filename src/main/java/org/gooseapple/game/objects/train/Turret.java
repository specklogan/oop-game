package org.gooseapple.game.objects.train;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.sound.Sound;
import org.gooseapple.game.objects.Bullet;

import java.util.Random;

public class Turret {
    private Vector2 position;
    private Bullet bullet;

    private Sound fireSound;

    private double damage = 10;
    private double reloadRate = 5;
    private long lastFired = 0;
    private double force = 7;

    public Turret(Vector2 position) {
        this.position = position;

        this.fireSound = new Sound("/sound/flak_fire.mp3");
        this.fireSound.setVolume(0.25);
    }

    public void setFireSound(Sound fireSound) {
        this.fireSound = fireSound;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void fire(Vector2 target) {
        if (System.currentTimeMillis() - lastFired > (reloadRate * 1000)) {
            lastFired = System.currentTimeMillis();
            fireSound.play();
            Random random = new Random();
            Vector2 worldPosition = position.clone();
            Vector2 direction = target.subtract(worldPosition).normalize();
            double speed = random.nextDouble(force, force + 0.25);
            Vector2 velocity = direction.multiply(speed);
            Bullet bullet = new Bullet(worldPosition.clone());
            bullet.setDamage(this.damage);
            bullet.getPhysicsBody().setVelocity(velocity);
        }
    }


    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public double getReloadRate() {
        return reloadRate;
    }

    public void setReloadRate(double reloadRate) {
        this.reloadRate = reloadRate;
    }

    public void setForce(int i) {
        this.force = i;
    }
}
