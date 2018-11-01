package Physics;

import Main.Transform;
import org.joml.Vector2f;

public class Rectangle extends CollideArea {
    private Vector2f rectA, rectB;
    private Vector2f shift;

    public Rectangle(Vector2f shift, Vector2f rectA, Vector2f rectB) {
        this.rectA = rectA;
        this.rectB = rectB;
        this.shift = shift;
    }

    public static Rectangle fromTransform(Transform transform) {
        float sin = (float) Math.sin(transform.getAngle()),
                cos = (float) Math.cos(transform.getAngle()),
                sX = transform.getScale().x,
                sY = transform.getScale().y;

        return new Rectangle(
                new Vector2f(.5f * sX * (sin - cos), -.5f * sY * (sin + cos)).add(transform.getPosition()),
                new Vector2f(sX * cos, sX * sin),
                new Vector2f(-sY * sin, sY * cos)
        );
    }

    public Transform toTransform() {
        Vector2f diag = new Vector2f(rectA.x + rectB.x, rectA.y + rectB.y);
        return new Transform().setPosition(shift.x + .5f * diag.x, shift.y + .5f * diag.y)
                .turn((float) Math.atan2(diag.y, diag.x))
                .setScale(new Vector2f(rectA.length(), rectB.length()));
    }

    @Override
    public boolean inArea(Vector2f dot) {
        //return .5f*(dot.dot(rectA) + dot.dot(rectB)) <= rectA.dot(rectB);
        float Dx = shift.x, Dy = shift.y,
                Ax = Dx + rectA.x, Ay = Dy + rectA.y,
                Cx = Dx + rectB.x, Cy = Dy + rectB.y,
                Bx = Ax + Cx - Dx, By = Ay + Cy - Dy;


        boolean P0 = 0 < (dot.x - Ax) * (By - Ay) - (dot.y - Ay) * (Bx - Ax),
                P1 = 0 < (dot.x - Bx) * (Cy - By) - (dot.y - By) * (Cx - Bx),
                P2 = 0 < (dot.x - Cx) * (Dy - Cy) - (dot.y - Cy) * (Dx - Cx),
                P3 = 0 < (dot.x - Dx) * (Ay - Dy) - (dot.y - Dy) * (Ax - Dx);

        return (P0 && P1 && P2 && P3) == (P0 || P1 || P2 || P3);
    }

    @Override
    public String toString() {
        float Dx = shift.x, Dy = shift.y,
                Ax = Dx + rectA.x, Ay = Dy + rectA.y,
                Cx = Dx + rectB.x, Cy = Dy + rectB.y,
                Bx = Ax + Cx - Dx, By = Ay + Cy - Dy;
        return "A = (" + Ax + " " + Ay + ")\n" +
                "B = (" + Bx + " " + By + ")\n" +
                "C = (" + Cx + " " + Cy + ")\n" +
                "D = (" + Dx + " " + Dy + ")";
    }

    @Override
    public CollideArea copy() {
        return new Rectangle(
                new Vector2f(shift),
                new Vector2f(rectA),
                new Vector2f(rectB)
        );
    }
}
