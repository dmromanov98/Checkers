package Wraps;

import Main.Actor;
import Main.Game;
import Main.Transform;
import Patterns.Sprite;

public class SpriteWrap extends Wrap {
    private int ID = Wrap.spriteID; // Default sprite ID

    @Override
    public int getID() {
        return ID;
    }

    public Transform transform;
    public String texName;
    public Runnable updateMeth;

    public SpriteWrap(Transform transform, String texName, Runnable updateMeth) {
        this.transform = new Transform(transform);
        this.texName = texName;
        this.updateMeth = updateMeth;
    }

    public SpriteWrap(SpriteWrap wrap) {
        this.transform = new Transform(wrap.transform);
        this.texName = wrap.texName;
        this.updateMeth = wrap.updateMeth;
    }

    @Override
    public SpriteWrap copy() {
        return new SpriteWrap(this);
    }

    @Override
    public Actor getActor(Game game) {
        Sprite res = new Sprite() {
            @Override
            public void update() {
                updateMeth.run();
            }
        };
        res.transform = transform;
        try {
            res.texture = game.textureBank.Get(texName).getTexture();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.setSource(this);
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }
}
