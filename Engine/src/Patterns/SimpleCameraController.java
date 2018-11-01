package Patterns;

import Main.*;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class SimpleCameraController extends Actor //TODO: обобщить этот класс
{
    public SimpleCameraController(Game game) {
        game.input.addKeyAction(new Input.KeyAction(GLFW_KEY_W, Input.KEY_HOLD, () -> moveUp()));
        game.input.addKeyAction(new Input.KeyAction(GLFW_KEY_S, Input.KEY_HOLD, () -> moveDown()));
        game.input.addKeyAction(new Input.KeyAction(GLFW_KEY_D, Input.KEY_HOLD, () -> moveRigth()));
        game.input.addKeyAction(new Input.KeyAction(GLFW_KEY_A, Input.KEY_HOLD, () -> moveLeft()));
        //game.input.addKeyAction( new Input.KeyAction(GLFW_KEY_Q, Input.KEY_HOLD, ()-> turnLeft()));
        //game.input.addKeyAction( new Input.KeyAction(GLFW_KEY_E, Input.KEY_HOLD, ()-> turnRigth()));
        //game.input.addKeyAction( new Input.KeyAction(GLFW_KEY_R, Input.KEY_HOLD, ()-> bringCloser()));
        //game.input.addKeyAction( new Input.KeyAction(GLFW_KEY_F, Input.KEY_HOLD, ()-> bringFarther()));
        game.input.addKeyAction(new Input.KeyAction(GLFW_KEY_T, Input.KEY_PRESS, () -> reset()));

        game.mouse.addWheelAction(new Mouse.WheelAction(Mouse.DELTA_WHEEL_ACTION,
                (Float delta) -> deltaSize += delta / 100f));

        deltaSize = 1f;

        Camera.getTransform().setPosition(0, 0);

        alive = true;
    }

    private static Vector2f speedVec = new Vector2f(0, 0);
    private static final float resist = 0.95f;
    private static final float speed = 0.2f;

    private static float deltaAngle = 0;
    private static final float turnSpeed = .002f;
    private static final float turnResist = .95f;

    private static float deltaSize = 0;
    private static final float resizeSpeed = .01f;
    private static final float resizeResist = .95f;

    private static void moveUp() {
        speedVec.y -= speed;
    }

    private static void moveDown() {
        speedVec.y += speed;
    }

    private static void moveRigth() {
        speedVec.x -= speed;
    }

    private static void moveLeft() {
        speedVec.x += speed;
    }

    private static void turnRigth() {
        deltaAngle += turnSpeed;
    }

    private static void turnLeft() {
        deltaAngle -= turnSpeed;
    }

    private static void bringCloser() {
        deltaSize += resizeSpeed;
    }

    private static void bringFarther() {
        deltaSize -= resizeSpeed;
    }

    private static void reset() {
        Camera.getTransform().setPosition(0f, 0f).rotate(0f);
        deltaAngle = 0;
        speedVec = new Vector2f(0, 0);
    }

    @Override
    public void update() {
        Camera.getTransform().move(speedVec).turn(deltaAngle).setScale(new Vector2f(deltaSize, deltaSize));
        speedVec.mul(resist);
        deltaAngle *= turnResist;
        //deltaSize *= resizeResist;
    }

    @Override
    public void draw() {
    }

    @Override
    public Transform tryToGetTransform() {
        return Camera.getTransform();
    }
}
