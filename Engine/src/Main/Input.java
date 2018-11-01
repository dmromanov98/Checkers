package Main;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.Vector;

public class Input extends GLFWKeyCallback {
    public static int KEY_PRESS = 1;
    public static int KEY_RELEASE = 0;
    public static int KEY_REPEAT = 2;
    public static int KEY_HOLD = 3;

    public static class KeyAction {
        public int key, action;
        private boolean hold;
        public Runnable keyMethod;

        public KeyAction(int key, int action, Runnable method) {
            //I used hand-made "action" check, because there is no button hold event
            this.key = key;
            this.action = action;
            this.keyMethod = method;
            this.hold = false;
        }

        public void check(int key, int action) {
            if (key == this.key) {
                if (this.action == action || (this.action == KEY_HOLD && this.action != KEY_REPEAT)) {
                    keyMethod.run();
                }
                this.hold = (action == KEY_PRESS) || (hold && !(action == KEY_RELEASE));
            }
        }

    }

    private Vector<KeyAction> keyActions;
    private Vector<KeyAction> keyActionsBuffer;

    public Input() {
        keyActions = new Vector<>();
        keyActionsBuffer = new Vector<>();
    }

    public void addKeyAction(KeyAction keyAction) {
        keyActionsBuffer.add(keyAction);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        for (KeyAction keyAction :
                keyActions) {
            keyAction.check(key, action);
        }
    }

    public void updateInput() {
        for (KeyAction keyAction :
                keyActions) {
            if (keyAction.hold && keyAction.action == KEY_HOLD)
                keyAction.keyMethod.run();
        }
        if (!keyActionsBuffer.isEmpty()) {
            keyActions.addAll(keyActionsBuffer);
            keyActionsBuffer.clear();
        }
    }
}
