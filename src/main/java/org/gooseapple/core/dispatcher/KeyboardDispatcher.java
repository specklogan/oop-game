package org.gooseapple.core.dispatcher;

import javafx.scene.input.KeyCode;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.events.KeyboardEvent;
import org.gooseapple.game.Game;

import java.util.ArrayList;

/**
 * Handles keycodes as well as special characters for use in our custom events
 */
public class KeyboardDispatcher implements EventListener {
    private Game game;

    public KeyboardDispatcher(Game game) {
        this.game = game;

        this.game.getScene().setOnKeyPressed(event -> {
            ArrayList<KeyCode> pressed = new  ArrayList<>();

            pressed.add(event.getCode());

            if (event.isAltDown()) {
                pressed.add(KeyCode.ALT);
            }

            if (event.isControlDown()) {
                pressed.add(KeyCode.CONTROL);
            }

            if (event.isShiftDown()) {
                pressed.add(KeyCode.SHIFT);
            }

            if  (event.isMetaDown()) {
                pressed.add(KeyCode.META);
            }

            KeyboardEvent keyboardEvent = new KeyboardEvent(pressed);
            keyboardEvent.dispatch();
        });
    }


}
