package Main;

import Graphics.Shader;
import MyMath.Matrix4f;
import Physics.Circle;
import Physics.Rectangle;
import org.joml.Vector2f;
import org.joml.Vector3f;

/*
нужно будет еще уточнить это. трабла с непонятностью, как вообще нормальные люди сохраняют корды и прочее
я не думаю, что пересоздавать матрицу каждый раз, когда я передавигаю что-то на чих есть классное решение проблемы
в случае работы с квадратиками хранить 16 флоатов чтобы двигать 4 вертекса? такое. матрица занимает памяти
больше, чем сам квадрат. оставлю этот вопрос открытым. мб ван дей перепишу это на векторы обратно.
*/
public class Transform {
    protected Vector2f position;
    protected Vector2f scale;
    public float angle;
    public float layer;

    public static class SpriteSpecification {
        public float[] position = {0, 0, 0, 0};
        public float[] scale = {1, 1};

        public void sendToShader(Shader shader) {
            shader.setUniform4f("absolute_position", position);
            shader.setUniform2f("scale", scale);
        }

        @Override
        public String toString() {
            return "pos: {" + position[0] + ';' + position[1] + ';' + position[2] +
                    "}, angle: " + position[3] +
                    ", size: {" + scale[0] + ';' + scale[1] + "}";
        }
    }

    public Transform() {
        position = new Vector2f(0, 0);
        layer = 0;
        angle = 0;
        scale = new Vector2f(1f, 1f);
    }

    public Transform(float layer, float scaleX, float scaleY) {
        position = new Vector2f(0, 0);
        this.layer = layer;
        scale = new Vector2f(scaleX, scaleY);
    }

    public Transform setPosition(Vector2f position) {
        this.position = new Vector2f(position);
        return this;
    }

    public Transform incLayer(float delta) {
        this.layer += delta;
        return this;
    }

    public Transform setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        return this;
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    public float getAngle() {
        return angle;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Transform translate(Vector2f v) {
        position.add(v);
        return this;
    }

    public Transform translate(float x, float y) {
        position.add(x, y);
        return this;
    }

    public Transform moveForward(float dist) {
        this.translate(new Vector2f(dist * (float) (Math.cos(angle)),
                dist * (float) (Math.sin(angle))));
        return this;
    }

    public Transform move(Vector2f vec) {
        double sin_res = Math.sin(angle);
        double cos_res = Math.cos(angle);
        this.translate(
                (float) (vec.x * cos_res - vec.y * sin_res),
                (float) (vec.x * sin_res + vec.y * cos_res)
        );
        return this;
    }

    public Transform turn(float angle) {
        this.angle += angle;
        return this;
    }

    public Transform rotate(float angle) {
        this.angle = angle;
        return this;
    }

    public Transform rescale(float scale) {
        this.scale = this.scale.mul(scale);
        return this;
    }

    public Transform scaleX(float scale) {
        this.scale.x *= scale;
        return this;
    }

    public Transform scaleY(float scale) {
        this.scale.y *= scale;
        return this;
    }

    public Transform setScale(Vector2f scale) {
        this.scale = scale;
        return this;
    }

    public Transform setLayer(float layer) {
        this.layer = layer;
        return this;
    }

    public Transform setAngle(float angle) {
        this.angle = angle;
        return this;
    }

    public Transform setScale(float x, float y) {
        this.scale = new Vector2f(x, y);
        return this;
    }

    //for OpenGL
    public SpriteSpecification spriteOpenGLOut() {
        SpriteSpecification oglResult = new SpriteSpecification();
        oglResult.position[0] = position.x;
        oglResult.position[1] = position.y;
        oglResult.position[2] = layer;
        oglResult.position[3] = angle;
        oglResult.scale[0] = scale.x;
        oglResult.scale[1] = scale.y;

        //System.out.println(oglResult);

        return oglResult;
    }

    public void matrixOpenGLOut(Shader shader) //shader must have a 'model' uniform
    {
        shader.setUniformMat4f("model", getMatrix());
    }

    public Matrix4f getMatrix() {
        return Matrix4f.translate(new Vector3f(position, layer))
                .multiply(Matrix4f.rotate(angle))
                .multiply(Matrix4f.resize(new Vector3f(scale, 1f)));

    }

    public Circle getCircleArea() {
        return new Circle(new Vector2f(position), Float.min(scale.x, scale.y));
    }

    public Rectangle getRectArea() {
        return Rectangle.fromTransform(this);
    }

    public Vector2f[] dotsOfRectangle() {
        Vector2f[] res = {
                new Vector2f(.5f * scale.x, .5f * scale.y),
                new Vector2f(-.5f * scale.x, .5f * scale.y),
                new Vector2f(-.5f * scale.x, -.5f * scale.y),
                new Vector2f(.5f * scale.x, -.5f * scale.y)
        };
        float sin = (float) Math.sin(angle), cos = (float) Math.cos(angle);
        for (Vector2f dot :
                res) {
            dot.x = dot.x * cos - dot.y * sin + position.x;
            dot.y = dot.x * cos + dot.y * sin + position.y;
        }
        return res;
    }

    public Transform(Transform transform) {
        this.angle = transform.angle;
        this.layer = transform.layer;
        this.position = new Vector2f(transform.position);
        this.scale = new Vector2f(transform.scale);
    }
}