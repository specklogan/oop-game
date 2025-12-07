package org.gooseapple.game.objects;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.ListenerPriority;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.render.Texture;

public class Fire extends Rectangle {
    private Rectangle smoke;

    public Fire(Vector2 position) {
        super(new Vector2(21,31), position);
        setTexture(new Texture("textures/fire/fire_red.gif"));

        smoke = new Rectangle(new Vector2(29,80), new Vector2(position.getX() - 3, position.getY() - 52), false, "textures/fire/smoke.gif");
        smoke.setTexture(new Texture("textures/fire/smoke.gif"));

        smoke.setTextureOffset(new Vector2(-3, -50));
        smoke.setPhysicsBody(this.getPhysicsBody());

    }

    @Override
    @EventHandler
    public void render(RenderEvent event) {
        smoke.render(event); //ensures that the fire is rendered ontop of the smoke
        super.render(event);
    }
}
