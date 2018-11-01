package Map;

import Graphics.Shader;
import Graphics.ShaderProgramsList;
import Graphics.Texture;
import Graphics.VertexArray;
import Main.Actor;
import Main.Transform;
import MyMath.Matrix4f;

//TODO: странный баг: декали крашат программу, когда нет бэкграунда. почему?!!!!
public class Decal extends Actor {
    private final Texture texture;
    private final Matrix4f matrix;
    public final Transform sourceTransform;//чтобы восстановить изначальные данные
    private static VertexArray mesh;

    public static Shader shader;

    public static void init() {
        try {
            shader = ShaderProgramsList.getShaderProgram("default/decal");
        } catch (Exception e) {
            e.printStackTrace();
            shader = null;
        }
        mesh = VertexArray.quad;
    }

    public Decal(Transform transform, Texture texture) {
        this.texture = texture;
        matrix = transform.getMatrix();
        sourceTransform = transform;
        alive = false;
        renderIndex = 1;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw() {
        shader.setUniformMat4f("model", matrix);
        texture.bind(shader);
        mesh.draw();
        texture.unbind();
    }

    @Override
    public Transform tryToGetTransform() {
        return sourceTransform;
    }
}
