package Graphics;

import Utils.BufferUtils;

import Utils.File;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;

public class Texture {
    private int texture;
    private int layer = 0;
    private String name = "tex" + layer;
    public final int WIDTH, HEIGHT;

    private Texture(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public static Texture monoColor(int r, int g, int b, int a) {
        Texture result = new Texture(1, 1);
        int[] data = {a << 24 | b << 16 | g << 8 | r};

        result.texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result.texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, result.WIDTH, result.HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

        return result;
    }

    public Texture(File.Image image) {
        texture = glGenTextures();
        WIDTH = image.WIDTH;
        HEIGHT = image.HEIGHT;
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.WIDTH, image.HEIGHT, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(image.data));
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind(Shader shader) {
        glActiveTexture(GL_TEXTURE0 + layer);
        glBindTexture(GL_TEXTURE_2D, texture);
        shader.setUniform1i(name, layer);
    }

    public void delete() {
        glDeleteTextures(texture);
        this.texture = 0;
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
