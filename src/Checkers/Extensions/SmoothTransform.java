package Checkers.Extensions;

import Main.Transform;
import org.joml.Vector2f;

public class SmoothTransform extends Transform
{
    public SmoothTransform(Transform transform)
    {
        super(transform);
    }

    private Vector2f destPos;
    private Vector2f firstPos;
    private Vector2f speed;
    private int frames;

    @Override
    public SmoothTransform setPosition(float x, float y) {
        firstPos = new Vector2f(position);
        destPos = new Vector2f(x, y);
        frames = Math.round( new Vector2f(- firstPos.x + destPos.x, - firstPos.y + destPos.y).length()/10 );
        speed = new Vector2f((- firstPos.x + destPos.x)/frames,( - firstPos.y + destPos.y)/frames);
        return this;
    }

    public SmoothTransform setPosition(Vector2f destPos) {
        return setPosition(destPos.x, destPos.y);
    }

    @Override
    public SmoothTransform translate(float x, float y) {
        setPosition(x + position.x, y + position.y);
        return this;
    }

    public void update()
    {
        if (frames > 0){
            super.translate(speed);
            frames -= 1;
            if (frames == 0){
                position = destPos;
                destPos = null;
                speed = null;
                firstPos = null;
            }
        }
    }

    public Transform superSetPosition(float x, float y)
    {
        return super.setPosition(x,y);
    }

    public Transform superSetPosition(Vector2f vec)
    {
        return super.setPosition(vec);
    }
}
