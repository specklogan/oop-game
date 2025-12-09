package org.gooseapple.core.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;

/**
 * Wrapper for the javafx sound object, loopable
 */
public class Sound {
    private final Media sound;
    private double volume = 1.0;
    private boolean loop = false;

    /**
     * Sound path, as "/sound/yourfile.mp3"
     * @param soundPath
     */
    public Sound(String soundPath) {
        this.sound = new Media(getClass().getResource(soundPath).toExternalForm());
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void play() {
        MediaPlayer player = new MediaPlayer(sound);
        player.setVolume(volume);

        if (loop) {
            player.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
            player.setOnEndOfMedia(() -> {
                player.dispose();
            });
        }

        player.play();
    }
}
