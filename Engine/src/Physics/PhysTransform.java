package Physics;

import Main.Transform;
import org.joml.Vector2f;
import Map.Map;

import java.util.Vector;

public class PhysTransform extends Transform {
    private final Map map;

    public void applyCollisionSpace(String name) {
        CollideArea area = map.collisionSpaces.getOrDefault(name, null);
        if (area != null)
            collisions.add(area);
        else
            System.err.println("There is no collision area named " + name);
    }

    public void applyCollisionSpaces(String[] names) {
        for (String str :
                names) {
            applyCollisionSpace(str);
        }
    }

    public void applyCollisionSpace(CollideArea area) {
        collisions.add(area);
    }

    private Vector<CollideArea> collisions;

    public PhysTransform(Map map, float mass) {
        super();
        this.mass = mass;
        this.map = map;
        collisions = new Vector<>();
        impulse = new Vector2f();
    }

    public PhysTransform(Map map, float mass, String[] areas) {
        super();
        this.map = map;
        collisions = new Vector<>();
        this.mass = mass;
        impulse = new Vector2f();
        applyCollisionSpaces(areas);
    }

    public CollideArea collided() //если столкнулся, выдаст ссылку на область
    {
        Vector2f[] dots = dotsOfRectangle();
        for (CollideArea area :
                collisions) {
            for (Vector2f dot :
                    dots) {
                if (area.inArea(dot))
                    return area;
            }
        }
        return null;
    }

    public void addForce(Vector2f force) {
        impulse.add(force);
    }

    public void addForce(Vector2f force, float secs) {
        impulse.add(force.x * secs, force.y * secs);
    }

    public void setImpulse(float x, float y) {
        impulse.set(x, y);
    }

    public Vector2f getImpulse() {
        return new Vector2f(impulse);
    }

    public void updatePosition(float secs) {
        Vector2f frictionVec = new Vector2f(impulse).normalize().mul(-friction * mass);
        if (impulse.lengthSquared() > frictionVec.lengthSquared())
            addForce(frictionVec, secs);
        else
            setImpulse(0, 0);
        position.add(secs * impulse.x / mass, secs * impulse.y / mass);
    }

    private Vector2f impulse;
    public float friction = 0;
    public float mass;
}
