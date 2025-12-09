package org.gooseapple.game.ui.background;

import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.EventManager;
import org.gooseapple.core.event.ListenerPriority;
import org.gooseapple.core.event.events.RenderEvent;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.game.Game;
import org.gooseapple.game.event.EnteredTownEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Handles the parallax effect, drawing the background, as well as drawing the railroad tiles on top
 */
public class Parallax implements EventListener {
    private ArrayList<Rectangle> backgrounds;
    private ArrayList<RailroadTile> railroad;
    private double horizontalShift = 1;
    private double speed = 0;
    private BackgroundType backgroundType;
    private Vector2 screenSize;
    private Random random = new Random();

    private Game instance;

    public Parallax(BackgroundType backgroundType, Vector2 screenSize, Game game) {
        this.backgrounds = new ArrayList<>();
        this.railroad = new ArrayList<>();
        EventManager.getInstance().addListener(this);
        this.backgroundType = backgroundType;
        this.screenSize = screenSize.clone();
        this.instance = game;
        loadBackgrounds(backgroundType, screenSize);
        initRailroad();
    }

    private void loadBackgrounds(BackgroundType backgroundType, Vector2 screenSize) {
        switch (backgroundType) {
            case PLAINS:
                ArrayList<Rectangle> plainBackgrounds = new ArrayList<>();
                plainBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/plains/1.png"));
                plainBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/plains/2.png"));
                plainBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/plains/3.png"));
                plainBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/plains/4.png"));
                setBackgrounds(plainBackgrounds);
                break;
            case DUSTBOWL:
                ArrayList<Rectangle> dustbowlBackgrounds = new ArrayList<>();
                dustbowlBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/dustbowl/sky.png"));
                dustbowlBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/dustbowl/far-clouds.png"));
                dustbowlBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/dustbowl/far-mountains.png"));
                dustbowlBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/dustbowl/near-clouds.png"));
                dustbowlBackgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/dustbowl/mountains.png"));
                setBackgrounds(dustbowlBackgrounds);
                break;
            case MOUNTAINS:
                ArrayList<Rectangle> backgrounds = new ArrayList<>();
                backgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/mountains/sky.png"));
                backgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/mountains/clouds_bg.png"));
                backgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/mountains/cloud_lonely.png"));
                backgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/mountains/glacial_mountains.png"));
                backgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/mountains/clouds_mg_3.png"));
                backgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/mountains/clouds_mg_2.png"));
                backgrounds.add(new Rectangle(screenSize.clone(), new Vector2(0,0), false, "textures/parallax/mountains/clouds_mg_1.png"));
                setBackgrounds(backgrounds);
                break;
        }
    }

    //These may get moved to their own classes later on, but for now this is what I have
    private void initRailroad() {
        RailroadTile first = new RailroadTile(RailroadType.RAILROAD, new Vector2(0, screenSize.getY() - 40), 0);
        railroad.add(first);
        while (getRailroadEndX() < screenSize.getX()) {
            spawnNextRailroad();
        }
    }

    private double getRailroadEndX() {
        RailroadTile last = railroad.getLast();
        return last.getPosition().getX() + last.getSize().getX();
    }

    private int unspawnedTownTiles = 0;
    private void spawnNextRailroad() {
        RailroadTile last = railroad.getLast();

        if (this.instance.canSpawnTown()) {
            if (unspawnedTownTiles == 0) {
                Random random = new Random();
                unspawnedTownTiles = random.nextInt(8,20);

                EnteredTownEvent event = new EnteredTownEvent(unspawnedTownTiles);
                event.dispatch();

                RailroadTile tile = new RailroadTile(RailroadType.RAILROAD, new Vector2(getRailroadEndX(), screenSize.getY() - 40), unspawnedTownTiles);
                railroad.add(tile);
                return;
            }
        }

        ArrayList<RailroadType> valid = last.getNextValidTypes();


        RailroadType next = valid.get(random.nextInt(valid.size()));


        RailroadTile tile = new RailroadTile(next, new Vector2(getRailroadEndX(), screenSize.getY() - 40), unspawnedTownTiles);

        if (unspawnedTownTiles > 0) {
            unspawnedTownTiles -= 1;
            if (unspawnedTownTiles == 0) {
                //handle last case to allow town to end
                railroad.add(new RailroadTile(RailroadType.RAILROAD, new Vector2(getRailroadEndX(), screenSize.getY() - 40), 0));
            }
        }

        railroad.add(tile);
    }

    private void updateRailroad(double shift) {
        for (int i = 0; i < railroad.size(); i++) {
            RailroadTile t = railroad.get(i);
            t.getPosition().setX(t.getPosition().getX() - shift);
        }
        RailroadTile first = railroad.getFirst();
        if (first.getPosition().getX() + first.getSize().getX() < 0) {
            railroad.removeFirst();
        }
        while (getRailroadEndX() < screenSize.getX()) {
            spawnNextRailroad();
        }
    }

    public ArrayList<Rectangle> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(ArrayList<Rectangle> backgrounds) {
        this.backgrounds = backgrounds;
    }

    @EventHandler(priority = ListenerPriority.HIGHEST)
    public void onRender(RenderEvent event) {
        var gc = event.getGraphicsContext();
        horizontalShift += 1 * speed;

        for (int i = 0; i < backgrounds.size(); i++) {
            var rect = backgrounds.get(i);
            double baseX = rect.getPosition().getX();
            double baseY = rect.getPosition().getY();
            double width = rect.getSize().getX();
            double height = rect.getSize().getY();
            double offset = -(horizontalShift * i) * (1/4d);
            double x = baseX - offset;
            x = ((x % width) + width) % width * -1;
            gc.drawImage(rect.getTexture().getImage(), x, baseY, width, height);
            gc.drawImage(rect.getTexture().getImage(), x + width, baseY, width, height);
        }

        updateRailroad(speed);
        for (RailroadTile t : railroad) {
            gc.drawImage(t.getTexture().getImage(), t.getPosition().getX(), t.getPosition().getY(), t.getSize().getX(), t.getSize().getY());
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}