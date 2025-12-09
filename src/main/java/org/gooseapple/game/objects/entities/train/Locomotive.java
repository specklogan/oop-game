package org.gooseapple.game.objects.entities.train;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.game.event.BombEvent;

public class Locomotive extends Carriage {
    public Locomotive(Vector2 position, String texture) {
        super(position, texture);
    }

    @Override
    public void loadCarriage() {
        super.loadCarriage();

        setMaxHealth(600);

        var firstTurretLocation = new Vector2(getPosition().getX() + 20, getPosition().getY() + 9);
        var secondTurretLocation = new Vector2(getPosition().getX() + 70, getPosition().getY() + 9);

        var firstTurret = new Turret(firstTurretLocation);
        var secondTurret = new Turret(secondTurretLocation);

        firstTurret.setReloadRate(1);
        secondTurret.setReloadRate(1);

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
