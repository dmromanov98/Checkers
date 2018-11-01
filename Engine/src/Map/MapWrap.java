package Map;

import Physics.CollisionSpace;
import Wraps.Wrap;

import java.util.HashMap;
import java.util.Vector;

public class MapWrap {

    public MapWrap() {
        objects = new Vector<>();
        collideAreas = new HashMap<>();
    }

    public MapWrap(Vector<Wrap> objects, HashMap<String, CollisionSpace> collideAreas) {
        this.collideAreas = new HashMap<>(collideAreas);

        this.objects = new Vector<>(objects);

        for (Wrap w :
                this.objects) {
            w = w.copy();
        }
    }

    public Vector<Wrap> objects;
    public HashMap<String, CollisionSpace> collideAreas;

}
