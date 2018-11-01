package Checkers.Objects;

import Checkers.Extensions.SmoothTransform;
import Graphics.Texture;
import Main.Game;
import Main.TextureBank;
import Main.Transform;
import Patterns.Sprite;

public class Checker extends Sprite
{
    public static Texture whiteTex, blackTex, qWhiteTex, qBlackTex;
    public static void loadTexures(Game game) throws TextureBank.NonExistentTextureException
    {
        whiteTex = game.textureBank.Get("white").getTexture();
        blackTex = game.textureBank.Get("black").getTexture();
        qWhiteTex = game.textureBank.Get("qWhite").getTexture();
        qBlackTex = game.textureBank.Get("qBlack").getTexture();
    }

    public char typ;

    public SmoothTransform smoothTransform;

    public Checker(Transform transform, char typ)
    {
        shader = defaultShader;
        this.renderIndex = 2;
        this.smoothTransform = new SmoothTransform(transform);
        this.transform = smoothTransform;
        changeTyp(typ);
    }

    public void changeTyp(char typ)
    {
        switch (typ){
            case 'w': texture = whiteTex; break;
            case 'W': texture = qWhiteTex; break;
            case 'b': texture = blackTex; break;
            case 'B': texture = qBlackTex; break;
        }
        this.typ = typ;
    }

    public void reverse()
    {
        switch (typ){
            case 'w': changeTyp('W'); break;
            case 'W': changeTyp('w'); break;
            case 'b': changeTyp('B'); break;
            case 'B': changeTyp('b'); break;
        }
    }

    public boolean isWhite()
    {
        return typ == 'w' || typ == 'W';
    }

    @Override
    public void update() {
        smoothTransform.update();
    }
}
