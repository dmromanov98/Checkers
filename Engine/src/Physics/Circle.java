package Physics;

import org.joml.Vector2f;

public class Circle extends CollideArea {
    private Vector2f center;
    private float radius;

    public Circle(Vector2f center, float radius) {
        this.center = new Vector2f(center);
        this.radius = radius;
    }

    @Override
    public boolean inArea(Vector2f dot) {
        return dot.sub(center, new Vector2f()).length() <= radius;
    }

    @Override
    public CollideArea copy() {
        return new Circle(
                new Vector2f(center),
                radius
        );
    }
}
