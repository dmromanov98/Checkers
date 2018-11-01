package Patterns;

import Graphics.*;
import Main.Actor;
import Main.Camera;
import Main.Transform;
import org.joml.Vector2f;

public abstract class Sprite extends Actor {
    private static VertexArray mesh;

    private static void createDrawConfiguration() {
        mesh = VertexArray.quad;
    }

    protected static Shader defaultShader;

    protected static Texture defaultTexture;

    public static void init() throws Error {
        createDrawConfiguration();
        try {
            defaultShader = ShaderProgramsList.getShaderProgram("default/sprite");
        } catch (ShaderProgramsList.ShaderProgramIsNotExistException e) {
            throw new Error("Cannot find default sprite shader. R u dumb? DO NOT MESS WITH THE ENGINE'S PATH!!!");
        }
        defaultTexture = Texture.monoColor(255, 255, 255, 255);
    }

    //------------------------------------------------------------------------

    public Transform transform;
    public Shader shader;
    public Texture texture = defaultTexture;

    public Sprite(Vector2f position, float layer) {
        transform = new Transform();
        transform.translate(position);
        transform.layer = layer;
        shader = defaultShader;
        this.renderIndex = 2;
    }

    public Sprite() {
        transform = new Transform();
        shader = defaultShader;
        this.renderIndex = 2;
    }

    @Override
    public final void draw() //TODO: настраивать конфиги рендера в другом месте
    {
        shader.enable();
        Camera.toShader(shader);
        transform.spriteOpenGLOut().sendToShader(shader);
        texture.bind(shader);
        mesh.render();
        shader.disable();
    }

    @Override
    public Transform tryToGetTransform() {
        return transform;
    }
}