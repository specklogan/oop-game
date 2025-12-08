package org.gooseapple.game.event;

import org.gooseapple.core.event.events.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class EnteredTownEvent extends Event {
    private int size = 0;
    private String name;

    public EnteredTownEvent(int size) {
        this.size = size;

        var townNames = new ArrayList<String>(Arrays.asList("Austin", "San Antonio", "Washington", "Omaha", "Baltimore", "Philadelphia", "New York", "Buffalo", "Des Moines", "Dallas", "Nacogdoches", "Palestine", "Lufkin", "Houston", "Chicago"));
        Random random = new Random();
        name = townNames.get(random.nextInt(townNames.size()));
    }

    public String getTownName() {
        return this.name;
    }
}
