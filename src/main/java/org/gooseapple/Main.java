package org.gooseapple;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.gooseapple.core.dispatcher.KeyboardDispatcher;
import org.gooseapple.core.dispatcher.LoopDispatcher;
import org.gooseapple.core.dispatcher.MouseDispatcher;
import org.gooseapple.game.Game;

public class Main extends Application {
    private LoopDispatcher loopDispatcher;
    private MouseDispatcher mouseDispatcher;
    private KeyboardDispatcher keyboardDispatcher;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //All the calculations is held on this game object
        Game game = new Game();

        primaryStage.setScene(game.getScene());

        //Physics and UI thread listening happen here
        loopDispatcher = new LoopDispatcher(game);
        mouseDispatcher = new MouseDispatcher(game);
        keyboardDispatcher = new KeyboardDispatcher(game);

        primaryStage.setTitle("Iron Convoy");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}