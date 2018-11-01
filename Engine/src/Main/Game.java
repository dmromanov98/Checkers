package Main;

import Graphics.ShaderCompiler;
import Graphics.ShaderProgramsList;
import Graphics.VertexArray;
import Map.Map;
import Patterns.Background;
import Map.Decal;
import Utils.File;
import Patterns.Sprite;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {
    private final String[] defaultClasses = {"sprite", "background", "decal"};

    public final int[] screenSize = {0, 0}; // [0] -- WIDTH; [1] -- HEIGHT
    private long window;

    public Input input;
    public Mouse mouse;
    public TextureBank textureBank = new TextureBank();

    public int fps = 60;

    public Map map;


    public Game(int width, int height) {
        this.screenSize[0] = width;
        this.screenSize[1] = height;
    }

    private void initClasses() {
        VertexArray.init();//preparing common meshes
        Sprite.init();
        Decal.init();
        Background.init();
    }

    private void loadAndInitDefaultShaderPrograms() {
        ShaderCompiler.addShadersFromAList(File.loadTextfilesFromAList("Engine/shaders/shader_list.txt"));
        ShaderCompiler.printAllShaders();
        for (String className :
                defaultClasses) {
            int[] shaders = {
                    ShaderCompiler.getShader("default/" + className + "/vertex.default"),
                    ShaderCompiler.getShader("default/" + className + "/fragment.default")
            };

            ShaderProgramsList.CreateShaderProgram("default/" + className, shaders);
        }
    }

    public void init() throws Error {
        if (!glfwInit()) {
            throw new Error("Could not initialize GLFW!");
        }

        //creating a window
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        window = glfwCreateWindow(screenSize[0], screenSize[1], "OpenGL learning", NULL, NULL);

        if (window == NULL) {
            throw new Error("Could not create GLFW window!");
        }

        System.out.println("Window is created.");

        //initializing inputs
        mouse = new Mouse(window, screenSize);
        input = new Input();

        //moving the window to the window of the screen
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - screenSize[0]) / 2,
                (vidmode.height() - screenSize[1]) / 2);

        //another initializing of OpenGL
        glfwMakeContextCurrent(window);
        createCapabilities();

        //setting up the keyboard
        glfwSetKeyCallback(window, input);
        //glfwSetInputMode(window, GLFW_STICKY_KEYS, 1);

        //TODO: delete this. render settings must be inside of "draw" methods
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        //setting up the camera
        glViewport(0, 0, screenSize[0], screenSize[1]);
        Camera.initCamera(screenSize[0], screenSize[1]);

        //loading shaders
        loadAndInitDefaultShaderPrograms();

        //initializing classes
        initClasses();

        //for beauty
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    }

    private void update() {
        glfwPollEvents();
        input.updateInput();
        mouse.updateMouse();
        map.update();
        Camera.update();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        map.drawAll();

        int error = glGetError();
        if (error != GL_NO_ERROR)
            System.out.println(error);

        glfwSwapBuffers(window);
    }

    public void mainloop() {
        glfwShowWindow(window);
        System.out.println("Window is showed.");

        update();

        if (map == null)
            throw new Error("There is no map. Check your source code.");

        System.out.println("Mainloop started.");
        while (true) {
            update();
            render();

            if (glfwWindowShouldClose(window)) {
                System.out.println("Window is closed.");
                break;
            }
            syncFrameRate(fps, System.nanoTime());
        }
    }

    public void closeGame() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static void syncFrameRate(float fps, long lastNanos) {
        long targetNanos = lastNanos + (long) (1_000_000_000.0f / fps) - 1_000_000L;  // subtract 1 ms to skip the last sleep call
        try {
            while (System.nanoTime() < targetNanos) {
                Thread.sleep(1);
            }
        } catch (InterruptedException ignore) {
        }
    }
}
