package Graphics;

import Utils.Textfile;

import java.util.HashMap;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

public class ShaderCompiler {
    private ShaderCompiler() {
    }

    private static HashMap<String, Integer> shaders = new HashMap<>();

    public static void vertexShader(String name, String data) throws ShaderCompilingError {
        int ID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(ID, data);

        glCompileShader(ID);
        if (glGetShaderi(ID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new ShaderCompilingError("Failed to compile vertex shader. Name: " + '"' + name + '"' + '\n' + glGetShaderInfoLog(ID));
        }

        shaders.put(name, ID);
    }

    public static void fragmentShader(String name, String data) throws ShaderCompilingError {
        int ID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(ID, data);

        glCompileShader(ID);
        if (glGetShaderi(ID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile fragment shader!");
            System.err.println(glGetShaderInfoLog(ID));
            throw new ShaderCompilingError("Failed to compile fragment shader. Name: " + '"' + name + '"' + '\n' + glGetShaderInfoLog(ID));
        }

        shaders.put(name, ID);
    }

    public static int getShader(String name) throws Error {
        if (shaders.containsKey(name))
            return shaders.get(name);
        else
            throw new Error("Shader called '" + name + "' is not exist.");
    }

    //if you have finished working with shaders, it's good if you delete them
    public static void deleteShaders() {
        for (int shader :
                shaders.values()) {
            glDeleteShader(shader);
        }

        shaders.clear();
    }

    public static void printAllShaders() {
        for (String s :
                shaders.keySet()) {
            System.out.print('"' + s + '"' + ", ");
        }
        System.out.println();
    }

    public static void addShadersFromAList(Vector<Textfile> shaders) {
        for (Textfile file :
                shaders) {
            switch (file.name.substring(file.name.lastIndexOf('/') + 1, file.name.indexOf('.')).toLowerCase()) {
                case "vertex":
                    vertexShader(file.name, file.data);
                    break;
                case "fragment":
                    fragmentShader(file.name, file.data);
                    break;
            }
        }
    }

    public static class ShaderCompilingError extends Error {
        public ShaderCompilingError(String message) {
            super(message);
        }
    }
}
