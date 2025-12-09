package org.gooseapple.core.dispatcher;

import javafx.scene.input.MouseButton;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.events.MouseEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.game.Game;

/**
 * Hooks into JFX mouse method to call our custom event system which is easily handled in the game class.
 */
public class MouseDispatcher {
    private Game game;

    public MouseDispatcher(Game game) {
        this.game = game;

        game.getScene().setOnMouseClicked(e -> {
            Vector2 mousePosition = new Vector2((int) e.getX(), (int) e.getY());
            MouseEvent.MouseClickType type = MouseEvent.MouseClickType.LEFT;
            if (e.getButton() == MouseButton.PRIMARY) {
                type = MouseEvent.MouseClickType.LEFT;
            } else if (e.getButton() == MouseButton.MIDDLE) {
                type = MouseEvent.MouseClickType.MIDDLE;
            } else if (e.getButton() == MouseButton.SECONDARY) {
                type = MouseEvent.MouseClickType.RIGHT;
            }

            MouseEvent event = new MouseEvent(mousePosition, type);
            event.dispatch();
        });
    }
}
