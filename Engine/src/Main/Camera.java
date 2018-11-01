package Main;

import Graphics.Shader;
import MyMath.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Camera() {
    }

    static private Matrix4f projection;
    static private Matrix4f view;
    static private Transform transform = new Transform();

    static private boolean viewMatrixShouldBeUpdated = false;

    static private void updateViewMatrix() {
        if (viewMatrixShouldBeUpdated) {
            view = Matrix4f.resize(new Vector3f(transform.getScale(), 1f)).
                    multiply(Matrix4f.rotate(transform.getAngle())).
                    multiply(Matrix4f.translate(new Vector3f(transform.getPosition(), transform.layer)));

            viewMatrixShouldBeUpdated = false;
        }
    }

    public static void initCamera(float width, float height) {
        projection = Matrix4f.orthographic(-.5f * width, .5f * width, -.5f * height, .5f * height, -1.0f, 1.0f);
        //System.out.println(projection);
        viewMatrixShouldBeUpdated = true;
        updateViewMatrix();
    }

    public static void toShader(Shader shader) {
        shader.setUniformMat4f("projection", projection);
        shader.setUniformMat4f("view", view);
    }

    public static Transform getTransform() {
        viewMatrixShouldBeUpdated = true;
        return transform;
    }

    public static void update() {
        updateViewMatrix();
    }
}
