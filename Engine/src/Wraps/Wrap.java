package Wraps;

import Main.Actor;
import Main.Game;

public abstract class Wrap {

    public static final int actorID = 0,
            spriteID = 1,
            decalID = 2,
            backgroundID = 3;

    //private int ID = Wrap.actorID; // Default actor ID

    public abstract int getID();


    public abstract Actor getActor(Game game);

    public abstract void setID(int ID);

    public abstract Wrap copy();

}
