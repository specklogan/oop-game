package org.gooseapple.game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.level.Level;

public class Game extends Level {
    private GridPane window;
    private Canvas gameCanvas;
    private Scene scene;
    private GraphicsContext graphicsContext;

    private int screenHeight = 400;
    private int screenWidth = 500;

    public Game() {
        this.window = new GridPane(screenWidth,screenHeight);
        this.gameCanvas = new Canvas(screenWidth,screenHeight);
        this.graphicsContext = this.gameCanvas.getGraphicsContext2D();

        this.window.getChildren().add(this.gameCanvas);

        this.scene = new Scene(this.window,screenWidth,screenHeight);

        this.setEnabled(true);
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public Scene getScene() {
        return this.scene;
    }

    @EventHandler
    public void HandleTick(TickEvent event) {
    }

    private int i = 0;
    @EventHandler
    public void HandleFrame(RenderEvent event) {
        this.graphicsContext.setFill(Color.PINK);
        i += 1;
        this.graphicsContext.fillRect(40+i, 40+i, 100, 100);
    }
}
