package org.gooseapple.game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.gooseapple.core.collision.PhysicsService;
import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.events.KeyboardEvent;
import org.gooseapple.core.event.events.MouseEvent;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.render.Texture;
import org.gooseapple.core.sound.Sound;
import org.gooseapple.game.event.DestroyBulletEvent;
import org.gooseapple.game.objects.Bullet;
import org.gooseapple.game.objects.Fire;
import org.gooseapple.game.objects.FlakBurst;
import org.gooseapple.game.objects.entities.Biplane;
import org.gooseapple.game.objects.entities.Zeppelin;
import org.gooseapple.game.objects.train.Carriage;
import org.gooseapple.game.objects.train.Locomotive;
import org.gooseapple.game.objects.train.TurretCar;
import org.gooseapple.game.ui.background.BackgroundType;
import org.gooseapple.game.ui.background.Parallax;
import org.gooseapple.level.Level;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

public class Game extends Level {
    private GridPane window;
    private Canvas gameCanvas;
    private Scene scene;
    private GraphicsContext graphicsContext;
    private Sound drivingSound;
    private Sound flakBurst;

    private Zeppelin zeppelin;
    private Biplane biplane;

    private Vector2 screenSize = new Vector2(1300,400);

    private Locomotive locomotive;
    private Parallax parallax;

    private double speed = 0;       //Speed starting at zero gives us a stationary start. If we want to start moving, need to change (and have the speed update beyond key preseses)
    private double maxSpeed = 15; // 32.58 is approx 117.3 km/h (75mhp) can change if wanted, but 15 seemed better for background
    private double minSpeed = 0;
    private double acceleration = 0.125;

    private Random random = new Random();
    private int enemyRarity;

    public Game() {
        /**
         * TODO: Add background, maybe parallax for that 2d/3d aesthetic?
         * It may be cool to have multiple types of backgrounds, ie, desert, forest etc, but itll depend on how much time we have
         */

        this.window = new GridPane(screenSize.getX(),screenSize.getY());
        this.gameCanvas = new Canvas(screenSize.getX(),screenSize.getY());
        this.graphicsContext = this.gameCanvas.getGraphicsContext2D();

        this.window.getChildren().add(this.gameCanvas);

        this.scene = new Scene(this.window,screenSize.getX(),screenSize.getY());

        this.setEnabled(true);

        this.locomotive = new Locomotive( new Vector2(screenSize.getX() - 300, screenSize.getY() - 43), "textures/train1.png");
        this.locomotive.addCarriageToEnd(new TurretCar(new Vector2(0,0)));

        this.locomotive.loadCarriage();

        this.drivingSound = new Sound("/sound/train_drive.mp3");
        this.drivingSound.setVolume(0.025);
        this.drivingSound.setLoop(true);
        this.drivingSound.play();

        this.flakBurst = new Sound("/sound/flak_burst.mp3");
        this.flakBurst.setVolume(0.05);

        this.parallax = new Parallax(BackgroundType.PLAINS, screenSize);

        spawnEnemies(5);
        
    }

    private int enemiesActive = 0;
    public void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            enemyRarity = random.nextInt(100)+1;

            if(enemyRarity < 51){  //Zeppelin 50% spawn rate
                double x = random.nextDouble(screenSize.getX(), screenSize.getX() + 500);
                double y = random.nextDouble(40, screenSize.getY() - 200);
                Zeppelin zeppelin = new Zeppelin(new Vector2(x, y));
                zeppelin.getPhysicsBody().setVelocity(new Vector2(-0.4,0));
            }
            else if(enemyRarity >= 51){     //Biplane 50% spawn rate
                double x = random.nextDouble(screenSize.getX(), screenSize.getX() + 500);
                double y = random.nextDouble(40, screenSize.getY() - 200);
                Biplane biplane = new Biplane(new Vector2(x, y));
                biplane.getPhysicsBody().setVelocity(new Vector2(-0.4,0));
            }
            enemiesActive++;
            //else if{}   for future enemy types
        }
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public Scene getScene() {
        return this.scene;
    }

    @EventHandler
    public void HandleMouseClick(MouseEvent event) {
        if (event.getClickType() == MouseEvent.MouseClickType.LEFT) {
            this.locomotive.fireTurrets(event.getMousePosition());
        }
    }

    @EventHandler
    public void HandleBulletDestroyEvent(DestroyBulletEvent event) {
        flakBurst.play();
        new FlakBurst(event.getBullet().center());
    }

    @EventHandler
    public void handleDeceleration(TickEvent event) {
        if(speed>acceleration/10)
            this.speed -=acceleration/10;
            this.parallax.setSpeed(this.speed);
    }

    @EventHandler
    public void HandleKeyboardPress(KeyboardEvent event) {
        if (event.keyCode(KeyCode.W) || event.keyCode(KeyCode.RIGHT)) {  //Changed keys, so can use left and right arrows too
            if(this.speed >=maxSpeed-acceleration) return;      // Caps max speed, can change if wanted
            this.speed += acceleration;
            this.parallax.setSpeed(this.speed);
        } 
        else if (event.keyCode(KeyCode.S) || event.keyCode(KeyCode.LEFT)) {
            if (this.speed <=minSpeed+acceleration) return;     // Prevents negative speed / reverse movement
            else{this.speed -= acceleration;}
            this.parallax.setSpeed(this.speed);
        }
        else if (event.keyCode(KeyCode.Z)) { //debug spawning zeppelin manual single spawn
            spawnEnemies(1);
        }
        else if (event.keyCode(KeyCode.M)) { //debug spawning zeppelin calling method to spawn multiple
            spawnEnemies(5);
        }
        else if (event.keyCode(KeyCode.UP)) { //debug acceleration up
            acceleration++;
            maxSpeed++;
            System.out.println("Acceleration Up: "+acceleration);
        }
        else if (event.keyCode(KeyCode.DOWN)) { //debug acceleration down
            acceleration--;
            maxSpeed--;
            System.out.println("Acceleration Down: "+acceleration);
        }
    }


    private double deltaTime = 0;
    private double distance = 0;
    private String sDDistance = "";
    private String sDSpeed = "";
    

    @EventHandler
    public void handleDevCounter(TickEvent event) {
        deltaTime = event.getDeltaTime();
        distance += speed * deltaTime * 1/10;
    }

    private int shortener;
    private double sDistance;
    
    @EventHandler
    public void handleEnemySpawns(TickEvent event) {
        shortener = (int)(distance*10.0);        //shortens number to one decimal point
        sDistance = ((double)shortener)/10.0;
        //System.out.println("Number is: "+sDistance);
        if(sDistance != 0){
            if((sDistance/50)%1 == 0){
                spawnEnemies(2); //2 enemies total each increment of 500 (every 33 sec at max)
                System.out.println("Auto spawning 500");
            }
            if((sDistance/100)%1 == 0){
                spawnEnemies(4); //6 enemies total each increment of 1,0000 (every 67 sec at max)
                System.out.println("Auto spawning 1,000");
            }
            if((sDistance/500)%1 == 0){
                spawnEnemies(6); //12 enemies total each increment of 5,000 (every 333 sec at max)
                System.out.println("Auto spawning 5,000");
            }
            if((sDistance/500)%1 == 0){
                spawnEnemies(8); //20 enemies total each increment of 10,000 (every 667 sec at max)
                System.out.println("Auto spawning 10,000");
            }
        }
    }

    @EventHandler
    public void handleDisplay(RenderEvent event) {
        //event.getGraphicsContext().fillText("Current deltaTime: " +  deltaTime, 15,45);

        event.getGraphicsContext().setFill(javafx.scene.paint.Color.rgb(174,197,205,0.8));  //want to move elsewhere later
        event.getGraphicsContext().fillRoundRect(10, 5, 200, 40, 10,10);
        event.getGraphicsContext().setFill(javafx.scene.paint.Color.rgb(2,2,2,1.0));

        sDDistance = String.format("%.1f", distance)+ "km"; //Makes distance down to one decimal place and "converts" it to kilo meters
        event.getGraphicsContext().fillText("Current Distance: " +  sDDistance, 15,20);
        sDSpeed = String.format("%.1f", speed * 9)+ "kph"; //Makes Speed down to one decimal place and "converts" it to kilo meters
        event.getGraphicsContext().fillText("Current Speed: " +  sDSpeed, 15,40);

        //event.getGraphicsContext().fillText("Total Spawned Enemy Count: " +  enemiesActive, 15,60);

    }


    public Vector2 getScreenSize() {
        return this.screenSize;
    }
}
