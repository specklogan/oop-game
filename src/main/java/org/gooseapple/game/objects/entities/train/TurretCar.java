package org.gooseapple.game.objects.entities.train;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.sound.Sound;
import org.gooseapple.game.event.BombEvent;

public class TurretCar extends Carriage {

    public TurretCar(Vector2 position) {
        super(position, "textures/train_car_turret.png");
    }

    @Override
    public void loadCarriage() {
        super.loadCarriage();

        setMaxHealth(200);

        var firstTurretLocation = new Vector2(getPosition().getX() + 25, getPosition().getY() + 10);
        var secondTurretLocation = new Vector2(getPosition().getX() + 55, getPosition().getY() + 10);

        Turret firstTurret = new Turret(firstTurretLocation);
        Turret secondTurret = new Turret(secondTurretLocation);

        var sound = new Sound("/sound/big_cannon_fire.mp3");
        sound.setVolume(0.12);

        firstTurret.setForce(9);
        secondTurret.setForce(9);
        firstTurret.setDamage(40);
        secondTurret.setDamage(40);
        firstTurret.setFireSound(sound);
        secondTurret.setFireSound(sound);

        addTurret(firstTurret);
        addTurret(secondTurret);
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        super.render(event);
    }

    @EventHandler
    @Override
    public void onHitWithBomb(BombEvent bomb) {
        super.onHitWithBomb(bomb);
    }
}