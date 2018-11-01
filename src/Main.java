import Checkers.GameThread;

public class Main
{
    private static final String[] textures = {
            "black|textures\\black.png",
            "qBlack|textures\\black_queen.png",
            "white|textures\\white.png",
            "qWhite|textures\\white_queen.png",
            "desk|textures\\desk.jpg",
            "stol|textures\\background.jpg",
            "btnPlay|textures\\button_playgame.png",
            "btnClear|textures\\button_clear.png"
    };

    public static void main(String[] args)
    {
        new GameThread(800, 600, 45, textures).run();
    }
}
