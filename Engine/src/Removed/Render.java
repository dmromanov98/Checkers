package Removed;

import Main.Actor;
import Main.Camera;
import Map.Decal;

import java.util.Vector;

public class Render {
    private Vector<Vector<Actor>> renderLine; //разные слои рендера. 0 -- пол карты и бэкграунд, 1 -- декали,
    //2 -- динамические объекты

    public Render() {
        renderLine = new Vector<>();
        for (int i = 0; i < 3; i++)
            renderLine.add(new Vector<>());
    }


    public void addToRenderLine(Actor a) {
        if (a.renderIndex != -1) {
            renderLine.get(a.renderIndex).add(a);
        }
    }

    public void drawAll() //TODO: обобщить шейдеры для каждого типа объектов
    {
        //map
        for (Actor a :
                renderLine.get(0)) {
            a.draw();
        }

        //decals
        Decal.shader.enable();
        Camera.toShader(Decal.shader);
        for (Actor a :
                renderLine.get(1)) {
            a.draw();
        }
        Decal.shader.disable();

        //dynamic objects
        for (Actor a :
                renderLine.get(2)) {
            a.draw();
        }
    }
}
