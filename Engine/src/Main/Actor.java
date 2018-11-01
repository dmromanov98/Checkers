package Main;//import SpriteTest.Engine.MyMath.Vector2;

import Wraps.Wrap;

public abstract class Actor {
    public boolean alive = false;
    public short renderIndex = -1; //-1 -- not visible, 0 -- map and background, 1 -- static obj, 2 -- dynamic obj

    private boolean remFlag = false;

    public void delete() {
        remFlag = true;
    }

    abstract public void update();

    abstract public void draw();

    public boolean willBeRemoved() {
        return remFlag;
    }

    public Transform tryToGetTransform() {
        return null;
    }

    public Actor setSource(Wrap wrap) {
        source = wrap.copy();
        return this;
    }

    public Wrap source = null;
}