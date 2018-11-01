package Physics;

import org.joml.Vector2f;

import java.util.Vector;

public class CollisionSpace extends CollideArea {
    private Vector<Rectangle> collisions;

    public CollisionSpace() {
        collisions = new Vector<>();
    }

    public void addArea(Rectangle area) {
        if (area != null)
            collisions.add(area);
    }

    @Override
    public boolean inArea(Vector2f dot) {
        for (CollideArea area :
                collisions) {
            if (area.inArea(dot))
                return true;
        }
        return false;
    }

    public Vector<Rectangle> getRectangles() {
        return collisions;
    }

    public CollideArea inWhatArea(Vector2f dot) {
        for (CollideArea area :
                collisions) {
            if (area.inArea(dot))
                return area;
        }
        return null;
    }

    public void clear() {
        collisions.clear();
    }

    @Override
    public CollideArea copy() {
        CollisionSpace res = new CollisionSpace();
        res.collisions = new Vector<>(collisions);
        for (CollideArea area :
                res.collisions) {
            area = area.copy();
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder().append("\nNumber of rectangles = ").append(collisions.size())
                .append("\nRectangles:\n");
        for (Rectangle rect :
                collisions) {
            info.append(rect).append('\n');
        }
        return info.toString();
    }
}
