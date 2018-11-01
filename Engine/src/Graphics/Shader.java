package Graphics;

import MyMath.Matrix4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    private boolean enabled = false;

    public final int ID;
    private Map<String, Integer> locationCache = new HashMap<>();

    public Shader(int[] shaders) {
        ID = glCreateProgram();
        for (int shader :
                shaders) {
            glAttachShader(ID, shader);
        }

        glLinkProgram(ID);
        System.out.println(glGetProgramInfoLog(ID));
        glValidateProgram(ID);
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name))
            return locationCache.get(name);

        int result = glGetUniformLocation(ID, name);
        if (result == -1)
            System.err.println("Could not find uniform variable '" + name + "'!");
        else
            locationCache.put(name, result);
        return result;
    }

    public void setUniform1i(String name, int value) {
        if (!enabled) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1fv(String name, float[] f) {
        if (!enabled) enable();
        glUniform1fv(getUniform(name), f);
    }

    public void setUniform2fv(String name, float[] f) {
        if (!enabled) enable();
        glUniform2fv(getUniform(name), f);
    }

    public void setUniform3fv(String name, float[] f) {
        if (!enabled) enable();
        glUniform3fv(getUniform(name), f);
    }

    public void setUniform1f(String name, float[] f) {
        if (!enabled) enable();
        glUniform1f(getUniform(name), f[0]);
    }

    public void setUniform2f(String name, float[] f) {
        if (!enabled) enable();
        glUniform2f(getUniform(name), f[0], f[1]);
    }

    public void setUniform3f(String name, float[] f) {
        if (!enabled) enable();
        glUniform3f(getUniform(name), f[0], f[1], f[2]);
    }

    public void setUniform4f(String name, float[] f) {
        if (!enabled) enable();
        glUniform4f(getUniform(name), f[0], f[1], f[2], f[3]);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled) enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }

}
