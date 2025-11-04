package org.gooseapple.game.objects;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.EventManager;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.render.Texture;

public class FlakBurst extends Rectangle {
    public FlakBurst(Vector2 position) {
        super(new Vector2(16,16), position);
        this.setTexture(new Texture("textures/flak_burst.png"));
    }

    @EventHandler
    @Override
    public void render(RenderEvent event) {
        this.setOpacity(this.getOpacity() - 0.05);
        if (this.getOpacity() < 0) {
            EventManager.getInstance().deleteListener(this);
        }
        super.render(event);
    }
}
