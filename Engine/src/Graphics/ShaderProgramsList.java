package Graphics;

import java.util.HashMap;

public class ShaderProgramsList {
    private static HashMap<String, Shader> shaders_ = new HashMap<>();

    private ShaderProgramsList() {
    }

    public static void CreateShaderProgram(String name, int[] shaders) {
        shaders_.put(name, new Shader(shaders));
    }

    public static Shader getShaderProgram(String name) throws ShaderProgramIsNotExistException {
        if (shaders_.containsKey(name))
            return shaders_.get(name);
        else
            throw new ShaderProgramIsNotExistException("There is no '" + name + "' shader program!");
    }

    public static class ShaderProgramIsNotExistException extends Exception {
        public ShaderProgramIsNotExistException(String message) {
            super(message);
        }
    }
}
