package Checkers;

import Checkers.Objects.Checker;
import Checkers.Objects.Desk;
import Main.Game;

public class GameThread extends Thread
{
    private int fps;
    private int width, height;
    private String[] textures;

    public GameThread(int width, int height, int fps, String[] textures)
    {
        super("Game");
        setPriority(MAX_PRIORITY);
        this.fps = fps;
        this.width = width;
        this.height = height;
        this.textures = textures;
    }

    public Game game;

    private void init() {
        game = new Game(width, height);
        game.init();
        game.fps = fps;
        game.textureBank.addTexturesFromList(textures);
        try {
            Checker.loadTexures(game);
            game.map = new Desk(game);
        } catch(Exception e){e.printStackTrace(); throw new Error();}
    }

    @Override
    public void run() {
        super.run();
        init();

        /*TODO: game begins*/

        game.mainloop();
        game.closeGame();
    }
}
