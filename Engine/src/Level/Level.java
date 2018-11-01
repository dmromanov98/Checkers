package Level;

import Main.Actor;
import Map.Map;

import java.util.Vector;

public class Level {
    public Level(Map map, Vector<Actor> objects) {
        this.map = map;
        startObjects = objects;
    }

    private Vector<Actor> startObjects;

    private Map map;
    private Vector<Actor> actors;

    public void startLevel() {
        actors = new Vector<>();
        actors.addAll(startObjects);
    }
}
