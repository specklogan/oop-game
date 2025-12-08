package org.gooseapple.game.ui.background;

import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;
import org.gooseapple.core.render.Texture;

import java.util.ArrayList;
import java.util.Arrays;

public class RailroadTile extends Rectangle {
    private ArrayList<RailroadType> nextValidTypes;

    public RailroadTile(RailroadType type, Vector2 position, int townTiles) {

        super(new Vector2(120, 40), position, false, null);

        String texturePath = loadRailroadType(type, townTiles);
        setTexture(new Texture(texturePath));
    }

    private String loadRailroadType(RailroadType type, int townTiles) {
        if (townTiles > 0) { //there's probably a better way to do this, but for now this is fine
            switch (type) {
                case RAILROAD:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD_TOWN_1, RailroadType.RAILROAD_TOWN_2, RailroadType.RAILROAD_TRAIN_STATION, RailroadType.RAILROAD));
                    return "textures/parallax/railroad/railroad.png";
                case RAILROAD_TOWN_1:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD_TOWN_1, RailroadType.RAILROAD_TOWN_2, RailroadType.RAILROAD));
                    return "textures/parallax/railroad/town_1.png";
                case RAILROAD_TOWN_2:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD_TOWN_1, RailroadType.RAILROAD_TRAIN_STATION, RailroadType.RAILROAD));
                    return "textures/parallax/railroad/town_2.png";
                case RAILROAD_TRAIN_STATION:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD_TOWN_1, RailroadType.RAILROAD_TOWN_2, RailroadType.RAILROAD));
                    return "textures/parallax/railroad/train_station.png";
            }
        } else {
            switch (type) {
                case RAILROAD:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD, RailroadType.RAILROAD_WIRE_START));
                    return "textures/parallax/railroad/railroad.png";
                case RAILROAD_WIRE_START:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD_WIRE_END, RailroadType.RAILROAD_WIRE_MIDDLE));
                    return "textures/parallax/railroad/railroad_wire_start.png";
                case RAILROAD_WIRE_MIDDLE:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD_WIRE_MIDDLE, RailroadType.RAILROAD_WIRE_END));
                    return "textures/parallax/railroad/railroad_wire_middle.png";
                case RAILROAD_WIRE_END:
                    nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD));
                    return "textures/parallax/railroad/railroad_wire_end.png";
            }
        }

        nextValidTypes = new ArrayList<>(Arrays.asList(RailroadType.RAILROAD));
        return "textures/parallax/railroad/railroad.png";
    }

    public void setNextValidTypes(ArrayList<RailroadType> nextValidTypes) {
        this.nextValidTypes = nextValidTypes;
    }

    public ArrayList<RailroadType> getNextValidTypes() {
        return nextValidTypes;
    }
}
