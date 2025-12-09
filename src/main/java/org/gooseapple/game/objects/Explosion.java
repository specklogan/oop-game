package org.gooseapple.game.objects;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.sound.Sound;

public class Explosion extends Rectangle {
    private long time = System.currentTimeMillis();

    public Explosion(Vector2 position) {
        super(new Vector2(32,32), position, true, "textures/explosion.gif");
        Sound sound = new Sound("/sound/explosion_bomb.mp3");
        sound.setVolume(0.25);
        sound.play();
    }

    //Manually delete after the explosion has ended (roughly 4.58 seconds)
    @EventHandler
    @Override
    public void render(RenderEvent event) {
        super.render(event);

        if (System.currentTimeMillis() - this.time > 4580) {
            remove();
        }
    }
}
