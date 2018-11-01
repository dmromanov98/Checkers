package Main;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.util.Vector;
import java.util.function.Consumer;

public class Mouse {
    public static int BUTTON_PRESS = 1;
    public static int BUTTON_RELEASE = 0;
    public static int BUTTON_HOLD = 3;

    public static int MOUSE_BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    public static int MOUSE_BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
    public static int MOUSE_BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

    static private class MousePos extends GLFWCursorPosCallback {
        public Vector2f pos = new Vector2f(0, 0);
        private long window;

        public MousePos(long window, float xpos, float ypos) {
            this.window = window;
            setMousePos(xpos, ypos);
        }

        public void setMousePos(float x, float y) {
            this.pos.x = x;
            this.pos.y = y;
            GLFW.glfwSetCursorPos(window, pos.x, pos.y);
        }

        public void setMousePos(Vector2f pos) {
            this.pos = pos;
            GLFW.glfwSetCursorPos(window, pos.x, pos.y);
        }

        @Override
        public void invoke(long window, double xpos, double ypos) {
            this.pos.x = (float) xpos;
            this.pos.y = (float) ypos;
        }
    }

    static private class MouseButton extends GLFWMouseButtonCallback {
        public boolean holdLB, holdRB, holdMB;
        private Vector<MouseAction> mouseActions;

        public MouseButton(Vector<MouseAction> mouseActions) {
            holdLB = false;
            holdMB = false;
            holdRB = false;
            this.mouseActions = mouseActions;
        }

        @Override
        public void invoke(long window, int button, int action, int mods) {
            for (MouseAction mouseAction :
                    mouseActions) {
                mouseAction.check(button, action);
            }

            switch (button) {
                case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
                    this.holdRB = (action == BUTTON_PRESS) || (this.holdRB && !(action == BUTTON_RELEASE));
                    break;
                case GLFW.GLFW_MOUSE_BUTTON_LEFT:
                    this.holdLB = (action == BUTTON_PRESS) || (this.holdLB && !(action == BUTTON_RELEASE));
                    break;
                case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
                    this.holdMB = (action == BUTTON_PRESS) || (this.holdMB && !(action == BUTTON_RELEASE));
                    break;
            }
        }
    }

    static private class MouseWheel extends GLFWScrollCallback {

        float mouseWheel = 0;
        float delta = 0;

        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            delta += (float) yoffset;
        }
    }

    public static class MouseAction {
        public int button, action;
        public final Runnable mouseMethod;

        public MouseAction(int button, int action, Runnable mouseMethod) {
            this.button = button;
            this.action = action;
            this.mouseMethod = mouseMethod;
        }

        public void check(int key, int action) {
            if (this.button == key && this.action == action)
                mouseMethod.run();
        }
    }

    public static final byte DELTA_WHEEL_ACTION = 0;
    public static final byte ABSOLUTE_WHEEL_ACTION = 1;

    public static class WheelAction {
        public final Consumer<Float> method;
        public byte action;
        public final float MIN_WHEEL_MOVEMENT = .0001f;

        public WheelAction(byte action, Consumer<Float> method) {
            this.method = method;
            this.action = action;
        }

        public void run(MouseWheel mouseWheel) {
            switch (action) {
                case 0:
                    if (Math.abs(mouseWheel.delta) - MIN_WHEEL_MOVEMENT > 0)
                        method.accept(mouseWheel.delta);
                    break;
                case 1:
                    method.accept(mouseWheel.mouseWheel);
                    break;
            }
        }
    }

    private MousePos mousePos;
    private MouseButton mouseButton;
    private MouseWheel mouseWheel;
    private int[] screenSize;
    private Vector<MouseAction> mouseActions;
    private Vector<MouseAction> mouseActionsBuffer;
    private Vector<WheelAction> wheelActions;

    public Mouse(long window, int[] screenSize) {
        this.screenSize = screenSize;
        mouseActions = new Vector<>();
        mouseActionsBuffer = new Vector<>();
        wheelActions = new Vector<>();

        mousePos = new MousePos(window, screenSize[0] / 2, screenSize[1] / 2);
        mouseButton = new MouseButton(mouseActions);
        mouseWheel = new MouseWheel();

        GLFW.glfwSetMouseButtonCallback(window, mouseButton);
        GLFW.glfwSetCursorPosCallback(window, mousePos);
        GLFW.glfwSetScrollCallback(window, mouseWheel);
    }

    public void addMouseAction(MouseAction mouseAction) {
        mouseActionsBuffer.add(mouseAction);
    }

    //TODO: нужно сделать через буфер, пока будет так
    public void addWheelAction(WheelAction wheelAction) {
        wheelActions.add(wheelAction);
    }

    public Vector2f getAbsoluteMousePos() //returns cords with shift
    {
        Vector2f pos = Camera.getTransform().getPosition(),
                mPos = getMousePos();

        return getMousePos().add(-pos.x, -pos.y);
    }

    public Vector2f getMousePos() //returns cords on current window
    {
        Vector2f scale = Camera.getTransform().getScale();
        return new Vector2f(
                (mousePos.pos.x - .5f * screenSize[0]) / scale.x,
                (-mousePos.pos.y + .5f * screenSize[1]) / scale.y);
    }

    public Vector2f getRawMousePos() {
        return new Vector2f(
                (mousePos.pos.x - .5f * screenSize[0]),
                (-mousePos.pos.y + .5f * screenSize[1]));
    }

    public float getWheelPos() {
        return mouseWheel.mouseWheel;
    }

    public void setMousePos(Vector2f pos) {
        mousePos.setMousePos(pos.x + .5f * screenSize[0], pos.y + .5f * screenSize[1]);
    }

    public void updateMouse() {
        //button hold
        for (MouseAction mouseAction :
                mouseActions) {
            if (mouseAction.action == BUTTON_HOLD) {
                switch (mouseAction.button) {
                    case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
                        if (mouseButton.holdRB)
                            mouseAction.mouseMethod.run();
                        break;
                    case GLFW.GLFW_MOUSE_BUTTON_LEFT:
                        if (mouseButton.holdLB)
                            mouseAction.mouseMethod.run();
                        break;
                    case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
                        if (mouseButton.holdMB)
                            mouseAction.mouseMethod.run();
                        break;
                }
            }
        }

        //deleting
        if (!mouseActionsBuffer.isEmpty()) {
            mouseActions.addAll(mouseActionsBuffer);
            mouseActionsBuffer.clear();
        }

        //updating wheel
        mouseWheel.mouseWheel += mouseWheel.delta;
        for (WheelAction action :
                wheelActions) {
            action.run(mouseWheel);
        }
        mouseWheel.delta = 0;
    }
}
