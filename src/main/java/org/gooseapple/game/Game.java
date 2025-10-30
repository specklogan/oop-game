package org.gooseapple.game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.level.Level;

public class Game extends Level {
    private GridPane window;
    private Canvas gameCanvas;
    private Scene scene;
    private GraphicsContext graphicsContext;

    public Game() {
        this.window = new GridPane(1920,1080);
        this.gameCanvas = new Canvas(1920,1080);
        this.graphicsContext = this.gameCanvas.getGraphicsContext2D();

        this.graphicsContext.setFill(Color.PINK);
        this.graphicsContext.fillRect(40, 40, 100, 100);

        this.window.getChildren().add(this.gameCanvas);

        this.scene = new Scene(this.window,1920,1080);

        this.setEnabled(true);
    }

    public Scene getScene() {
        return this.scene;
    }

    @EventHandler
    public void HandleTimer(TickEvent event) {
    }
}
