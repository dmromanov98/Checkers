package Physics;

import org.joml.Vector2f;

public abstract class CollideArea {
    public abstract boolean inArea(Vector2f dot);

    public abstract CollideArea copy();
}
