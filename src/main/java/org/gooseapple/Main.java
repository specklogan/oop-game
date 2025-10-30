package org.gooseapple;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.gooseapple.core.dispatcher.LoopDispatcher;
import org.gooseapple.game.Game;

public class Main extends Application {
    private LoopDispatcher dispatcher;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //All of the calculations is held on this game object
        Game game = new Game();

        primaryStage.setScene(game.getScene());

        //Physics and UI thread listening happen here
        dispatcher = new LoopDispatcher(game);

        primaryStage.show();
    }
}