package Checkers.Objects;

public class Turn
{
    public final int x1,y1, x2,y2;

    public Turn(int x1, int y1, int x2, int y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Turn flip()
    {
        return new Turn(x2,y2,x1,y1);
    }

    @Override
    public String toString() {
        return "From (" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ")";
    }
}