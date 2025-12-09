package org.gooseapple.game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
import org.gooseapple.game.event.EnteredTownEvent;
import org.gooseapple.game.objects.Bullet;
import org.gooseapple.game.objects.Fire;
import org.gooseapple.game.objects.FlakBurst;
import org.gooseapple.game.objects.entities.enemies.Biplane;
import org.gooseapple.game.objects.entities.enemies.Zeppelin;
import org.gooseapple.game.objects.entities.train.Carriage;
import org.gooseapple.game.objects.entities.train.Locomotive;
import org.gooseapple.game.objects.entities.train.TurretCar;
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

    private boolean debugMode = false; //Manual Debug Toggle (In-game use "D" to enter debug mode)

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
        this.locomotive.addCarriageToEnd(new Carriage(new Vector2(0,0), "textures/train_car.png"));
        this.locomotive.addCarriageToEnd(new TurretCar(new Vector2(0,0)));
        this.locomotive.addCarriageToEnd(new Carriage(new Vector2(0,0), "textures/train_car_tank.png"));
        this.locomotive.addCarriageToEnd(new Carriage(new Vector2(0,0), "textures/train_car_container_blue.png"));
        this.locomotive.addCarriageToEnd(new TurretCar(new Vector2(0,0)));
        this.locomotive.addCarriageToEnd(new Carriage(new Vector2(0,0), "textures/train_car_container_blue.png"));


        this.locomotive.loadCarriage();

        this.drivingSound = new Sound("/sound/train_drive.mp3");
        this.drivingSound.setVolume(0.0125);
        this.drivingSound.setLoop(true);
        this.drivingSound.play();

        this.flakBurst = new Sound("/sound/flak_burst.mp3");
        this.flakBurst.setVolume(0.05);

        this.parallax = new Parallax(BackgroundType.PLAINS, screenSize, this);

        spawnEnemies(5);
    }

    private int enemiesActive = 0;
    public void spawnEnemies(int count){ //Helper, can either spawn random type of enemies or spesific
        spawnEnemies(count, -1);
    }
    public void spawnEnemies(int count, int enemyType) {
        for (int i = 0; i < count; i++) {
            if(enemyType == -1){
                enemyRarity = random.nextInt(100)+1;
            }
            else{
                enemyRarity = enemyType;
            }

            if(enemyRarity < 51){  //Zeppelin 50% spawn rate 1-50
                double x = random.nextDouble(screenSize.getX(), screenSize.getX() + 500);
                double y = random.nextDouble(40, screenSize.getY() - 200);
                Zeppelin zeppelin = new Zeppelin(new Vector2(x, y));
                zeppelin.getPhysicsBody().setVelocity(new Vector2(-0.4,0));
            }
            else if(enemyRarity >= 51){     //Biplane 50% spawn rate 51-100
                double x = random.nextDouble(screenSize.getX(), screenSize.getX() + 500);
                double y = random.nextDouble(40, screenSize.getY() - 200);
                Biplane biplane = new Biplane(new Vector2(x, y));
                biplane.getPhysicsBody().setVelocity(new Vector2(-0.6,0));
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

    private double lastTownDistance = 5;
    public boolean canSpawnTown() {
        Random random = new Random();
        double randomNumber = random.nextDouble(7,12);
        if (distance - lastTownDistance > randomNumber) {
            lastTownDistance += 8 * randomNumber;
            return true;
        }
        return false;
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
            if (this.locomotive != null && this.locomotive.getHealth() > 0) {
                if(this.speed >=maxSpeed-acceleration) return;      // Caps max speed, can change if wanted
                this.speed += acceleration;
                this.parallax.setSpeed(this.speed);
            }
        } 
        else if (event.keyCode(KeyCode.S) || event.keyCode(KeyCode.LEFT)) {
            if (this.speed <=minSpeed+acceleration) return;     // Prevents negative speed / reverse movement
            else{this.speed -= acceleration;}
            this.parallax.setSpeed(this.speed);
        }
        else if (event.keyCode(KeyCode.D)){
            if(debugMode)
                debugMode = false;
            else
                debugMode = true;
        }
        if(debugMode){
            if (event.keyCode(KeyCode.Z)) { //debug spawning Zepplen single spawn
                spawnEnemies(1, 50);
            }
            else if (event.keyCode(KeyCode.B)) { //debug spawning Biplane single spawn
                spawnEnemies(1, 51);
            }
            else if (event.keyCode(KeyCode.M)) { //debug spawning enemy multiple spawn
                spawnEnemies(5);
            }
            else if (event.keyCode(KeyCode.UP)) { //debug acceleration up
                if (this.locomotive != null && this.locomotive.getHealth() > 0) {
                    acceleration++;
                    maxSpeed++;
                }
            }
            else if (event.keyCode(KeyCode.DOWN)) { //debug acceleration down
                acceleration--;
                maxSpeed--;
            }
        }
    }


    private double deltaTime = 0;
    private double distance = 0;
    private String sDDistance = "";
    private String sDSpeed = "";
    private double timer = 0;
    

    @EventHandler
    public void handleDevCounter(TickEvent event) {
        deltaTime = event.getDeltaTime();
        distance += speed * deltaTime * 1/10;
        timer += deltaTime;
    }

    private int shortener;
    private double sDistance;
    private boolean resetTimer;
    
    @EventHandler
    public void handleEnemySpawns(TickEvent event) {
        shortener = (int)(distance*10.0);        //shortens number to one decimal point
        sDistance = ((double)shortener)/10.0;

        if(sDistance != 0 && timer > 10){
            if((sDistance/50)%1 == 0){
                spawnEnemies(2); //2 enemies total each increment of 500 (every 33 sec at max)
                if(debugMode)
                    System.out.println("Auto spawning 50");
                resetTimer = true;
            }
            if((sDistance/100)%1 == 0){
                spawnEnemies(4); //6 enemies total each increment of 1,0000 (every 67 sec at max)
                if(debugMode)
                    System.out.println("Auto spawning 100");
            }
            if((sDistance/500)%1 == 0){
                spawnEnemies(6); //12 enemies total each increment of 5,000 (every 333 sec at max)
                if(debugMode)
                    System.out.println("Auto spawning 500");
            }
            if((sDistance/1000)%1 == 0){
                spawnEnemies(8); //20 enemies total each increment of 10,000 (every 667 sec at max)
                if(debugMode)
                    System.out.println("Auto spawning 1000");
            }

            if(resetTimer){
                timer=0;
                resetTimer = false;
            }
        }
    }

    private long lastTownTime = 0;
    private int displayBubble = 3; //used to display that a town is being entered
    private String townName = "";
    @EventHandler
    public void enteredTown(EnteredTownEvent event) {
        lastTownTime = System.currentTimeMillis();
        townName = event.getTownName();
    }

    @EventHandler
    public void handleDisplay(RenderEvent event) {

        event.getGraphicsContext().setFill(javafx.scene.paint.Color.rgb(174,197,205,0.8));  //want to move elsewhere later
        event.getGraphicsContext().fillRoundRect(10, 5, 200, 40, 10,10);
        event.getGraphicsContext().setFill(javafx.scene.paint.Color.rgb(2,2,2,1.0));

        sDDistance = String.format("%.1f", distance)+ "km"; //Makes distance down to one decimal place and "converts" it to kilo meters
        event.getGraphicsContext().fillText("Current Distance: " +  sDDistance, 15,20);
        sDSpeed = String.format("%.1f", speed * 9)+ "kph"; //Makes Speed down to one decimal place and "converts" it to kilo meters
        event.getGraphicsContext().fillText("Current Speed: " +  sDSpeed, 15,40);

        if(debugMode){
            event.getGraphicsContext().fillText("Current deltaTime: " +  deltaTime, 15,60);
            event.getGraphicsContext().fillText("Total Spawned Enemy Count: " +  enemiesActive, 15,80);
            event.getGraphicsContext().fillText("Spawning Number is: " +  sDistance, 15,100);
            event.getGraphicsContext().fillText("Timer is: " +  timer, 15,120);

        }

        if (System.currentTimeMillis() - lastTownTime < (displayBubble * 1000)) {
            event.getGraphicsContext().save();
            event.getGraphicsContext().setFill(Color.YELLOW);
            event.getGraphicsContext().setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
            event.getGraphicsContext().fillText("Entering " + townName, (event.getScreenSize().getX() / 2) - 100, 200);
            event.getGraphicsContext().restore();
        }

        if (this.locomotive != null && this.locomotive.getHealth() < 0) {
            event.getGraphicsContext().save();
            event.getGraphicsContext().setFill(Color.DARKRED);
            event.getGraphicsContext().setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));
            event.getGraphicsContext().fillText("GAME OVER, YOU LOST THE TRAIN", (event.getScreenSize().getX() / 2) - 100, 200);
            event.getGraphicsContext().restore();
            long currentTime = System.currentTimeMillis();
            boolean endgame = false;
            while(!endgame){
                if (currentTime - System.currentTimeMillis() > 5000) {
                    System.exit(0);
                    endgame = true;
                }
            }
        }
    }


    public Vector2 getScreenSize() {
        return this.screenSize;
    }
}
