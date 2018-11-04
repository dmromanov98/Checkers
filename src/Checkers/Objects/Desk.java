package Checkers.Objects;

import Checkers.Extensions.SmoothTransform;
import Graphics.Texture;
import Main.*;
import Map.Map;
import Map.Decal;
import Patterns.Background;
import Physics.Rectangle;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Random;
import java.util.Vector;

public class Desk extends Map
{
    public static final float borderToDesk = 26f/500f,
                              checkerSizeToDesk = 56f/500f,
                              deltaLayer = .001f,
                              btnSizeXToDesk = .8f * 418f/500f, btnSizeYToDesk = .8f * 57f/500f;

    public final float startCellsX, endCellsX, startCellsY, endCellsY, checkerSize;

    private final Transform checkersSpawnPoint;

    protected Vector<Checker> checkers = new Vector<>();

    public void addChecker(Checker checker)
    {
        checkers.add(checker);
    }

    DeskArray logicDesk;

    private int width, height;

    public Desk(Game game) throws TextureBank.NonExistentTextureException
    {
        super();
        //получаем текстуры
        Texture deskTex = game.textureBank.Get("desk").getTexture(),
                backTex = game.textureBank.Get("stol").getTexture(),
                btnPlayTex = game.textureBank.Get("btnPlay").getTexture(),
                btnClearTex = game.textureBank.Get("btnClear").getTexture();

        //размеры экрана
        width = game.screenSize[0]; height = game.screenSize[1];
        int maxSize = Integer.max(width, height), minSize = Integer.min(width, height);

        //создаем стол
        addBackground(new Background(backTex, 0f));

        //создаем доску
        float deskScale = .82f*minSize;
        Transform deskTransform = new Transform().setScale(-deskScale, deskScale)
                                                 .setLayer(.1f)
                                                 .setPosition(0,0)
                                                 .setAngle(-(float)Math.PI/2);
        addDecal(new Decal(deskTransform, deskTex));

        //создаем шашули
        checkerSize = checkerSizeToDesk*deskScale;
        float layer = .5f;
        checkersSpawnPoint = new Transform().setPosition(-.25f*width - .25f*width/2, 0)
                                                      .setScale(checkerSize, checkerSize);

        Random random = new Random();
        for (int i = 0; i < 12; i++){
            float x = (random.nextFloat())*width*.1f - width*.08f,
                  y = (random.nextFloat())*height*.3f - height*.15f;

            Transform transform = new Transform(checkersSpawnPoint).translate(x, -y).setLayer(layer);
            addChecker(new Checker(transform,'w'));
            layer += deltaLayer;

            transform = new Transform(checkersSpawnPoint).translate(x+width*.8f, y).setLayer(layer);
            addChecker(new Checker(transform, 'b'));
            layer += deltaLayer;
        }

        //вводим переменые для расположения шашек на доске
        startCellsX = deskTransform.getPosition().x - .5f*deskScale + borderToDesk*deskScale + .5f*checkerSize;
        endCellsX = startCellsX + checkerSize*8;
        startCellsY = deskTransform.getPosition().y - .5f*deskScale + borderToDesk*deskScale + .5f*checkerSize;
        endCellsY = startCellsY + checkerSize*8;

        //ставлю кнопку для расставления шашек
        Transform btnTransform = new Transform().setPosition(-width*.53f + btnSizeYToDesk*deskScale + .5f*btnSizeXToDesk*deskScale,
                                                             height*.5f - btnSizeYToDesk*deskScale + .5f*btnSizeYToDesk*deskScale)
                                                .setLayer(.2f)
                                                .setScale(-btnSizeYToDesk*deskScale, btnSizeXToDesk*deskScale)
                                                .setAngle(-(float)Math.PI/2);
        addDecal(new Decal(btnTransform, btnPlayTex));
        Rectangle btnPlayArea = new Rectangle(
                new Vector2f(-width*.53f + btnSizeYToDesk*deskScale, height*.5f - btnSizeYToDesk*deskScale),
                new Vector2f(btnSizeXToDesk*deskScale, 0),
                new Vector2f(0,btnSizeYToDesk*deskScale)
        );

        //ставлю кнопку для очистки поля от шашек
        btnTransform = new Transform(btnTransform).translate(.536f*width, 0);
        addDecal(new Decal(btnTransform, btnClearTex));
        Rectangle btnClearArea = new Rectangle(
                new Vector2f(-width*.53f + btnSizeYToDesk*deskScale + .536f*width, height*.5f - btnSizeYToDesk*deskScale),
                new Vector2f(btnSizeXToDesk*deskScale, 0),
                new Vector2f(0,btnSizeYToDesk*deskScale)
        );

        //"логическая" доска
        logicDesk = new DeskArray();

        //создаю управление
        game.mouse.addMouseAction(new Mouse.MouseAction(Mouse.MOUSE_BUTTON_LEFT, Mouse.BUTTON_PRESS,
                ()-> lastMousePos = game.mouse.getAbsoluteMousePos() ));
        game.mouse.addMouseAction(new Mouse.MouseAction(Mouse.MOUSE_BUTTON_LEFT, Mouse.BUTTON_HOLD,
                ()-> {mouseLeftHold(game); lastMousePos = game.mouse.getAbsoluteMousePos();}));
        game.mouse.addMouseAction(new Mouse.MouseAction(Mouse.MOUSE_BUTTON_LEFT, Mouse.BUTTON_RELEASE,
                ()-> mouseLeftRelease()));

        game.mouse.addMouseAction(new Mouse.MouseAction(Mouse.MOUSE_BUTTON_MIDDLE, Mouse.BUTTON_PRESS,
                ()-> {if(currentChecker!=null) currentChecker.reverse();} ));

        game.mouse.addMouseAction(new Mouse.MouseAction(Mouse.MOUSE_BUTTON_LEFT, Mouse.BUTTON_PRESS,
                ()-> {if(btnPlayArea.inArea(game.mouse.getAbsoluteMousePos())) setCheckers();} ));
        game.mouse.addMouseAction(new Mouse.MouseAction(Mouse.MOUSE_BUTTON_LEFT, Mouse.BUTTON_PRESS,
                ()-> {if(btnClearArea.inArea(game.mouse.getAbsoluteMousePos())) clearCheckers(width, height);} ));
    }

    private boolean gameInProcess = true;

    public Vector2i getClosestCell(Transform transform)
    {
        float halfSize = checkerSize/2;
        Vector2f pos = new Vector2f(transform.getPosition());
        pos.x -= startCellsX-halfSize; pos.y -= startCellsY-halfSize;
        if (pos.x > 0 && pos.y > 0 && pos.x < (8*checkerSize+halfSize) && pos.y < (8*checkerSize+halfSize)){
            int cellX = Math.round(pos.x/checkerSize - .5f), cellY = Math.round(pos.y/checkerSize - .5f);
            return new Vector2i(cellX, cellY);
        }
        return null; //если не на поле
    }

    public void setCheckerOnCell(Checker checker, int x, int y)
    {
        checker.transform.setPosition(x*checkerSize + startCellsX, y*checkerSize + startCellsY);
    }

    private boolean objectIsMoving = false;
    private Checker currentChecker = null;
    private Vector2f lastMousePos;
    private Vector2i from, to;

    public void mouseLeftHold(Game game) //вежливо вырван из чьего-то редактора
    {
        if (objectIsMoving) { //что-то выделяли??
            if (currentChecker == null) { //на самом деле ничего не выделили? исправляем.
                objectIsMoving = false;
            } else {
                Transform transform = currentChecker.transform;
                Vector2f mousePos = game.mouse.getAbsoluteMousePos();
                transform.translate(mousePos.add(-lastMousePos.x, -lastMousePos.y));
                return; //все хорошо. пока до следующей итерации
            }
        }

        currentChecker = null; //пытаюсь найти новый объект

        for (Checker a :
                checkers) { //ищу среди живых объектов
            Transform transform = null;
            if (a != null)
                transform = a.transform;

            if (transform != null)
                if (transform.getRectArea().inArea(game.mouse.getAbsoluteMousePos())) {
                    currentChecker = a;
                    break;
                }
        }

        if (currentChecker != null) { //если нашел, сообщаю об этом
            objectIsMoving = true;
            from = getClosestCell(currentChecker.transform);
        }
    }

    public void mouseLeftRelease()
    {
        if (currentChecker != null) {
            Transform transform = currentChecker.transform;
            Vector2i closest = getClosestCell(transform);
            if (closest != null) {
                setCheckerOnCell(currentChecker, closest.x, closest.y);
                to = new Vector2i(closest);
                if (gameInProcess)
                    turn(from, to);
            }

            objectIsMoving = false;

            //допущение, чтобы работало
            for (Checker c:
                    checkers) {
                Vector2i vec = getClosestCell(c.transform);
                if ( vec!=null && logicDesk.get(vec.x, vec.y) == '*'){
                    killChecker(c);
                    logicDesk.set(vec.x, vec.y, ' ');
                }
            }
        }
    }

    public void killChecker(Checker c)
    {
        Random random = new Random();
        float x = (random.nextFloat())*width*.1f - width*.08f + checkersSpawnPoint.getPosition().x,
                y = (random.nextFloat())*height*.3f - height*.15f + checkersSpawnPoint.getPosition().y;
        if(c.isWhite()){
            c.changeTyp('w');
            c.smoothTransform.setPosition(x, -y);
        } else {
            c.changeTyp('b');
            c.smoothTransform.setPosition(x+width*.8f, y);
        }
    }

    public void setCheckers()
    {
        char[] desk = DeskArray.checkerDesk.clone();
        for (Checker c:
             checkers) {
            boolean brk = false;
            for(int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++)
                    if (c.typ == desk[x + y * 8]) {
                        setCheckerOnCell(c, x, y);
                        desk[x + y * 8] = '*';
                        brk = true;
                        break;
                    }
                if(brk)
                    break;
            }
        }

        logicDesk.newGame(); //todo: не работает полностью проверка, посмотреть на это можно тут
    }

    public void clearCheckers(int width, int height)
    {
        Random random = new Random();
        for (Checker c:
             checkers) {
            float x = (random.nextFloat())*width*.1f - width*.08f + checkersSpawnPoint.getPosition().x,
                  y = (random.nextFloat())*height*.3f - height*.15f + checkersSpawnPoint.getPosition().y;
            if(c.isWhite()){
                c.changeTyp('w');
                c.smoothTransform.setPosition(x, -y);
            } else {
                c.changeTyp('b');
                c.smoothTransform.setPosition(x+width*.8f, y);
            }
        }

        logicDesk = new DeskArray();
    }

    @Override
    public void update() {
        super.update();

        for (Checker c:
                checkers) {
            c.update();
        }
    }

    @Override
    public void drawAll() {
        super.drawAll();

        for (Checker c:
             checkers) {
            c.draw();
        }
    }

    public void turn(Vector2i from, Vector2i to)
    {
        if (from == null || to == null) {
            logicDesk.toTrainingMode();
        } else {
            if (!from.equals(to)) {
                Turn t = new Turn(from.x, from.y, to.x, to.y);
                if (logicDesk.turn(t)) {
                    if (currentChecker.isWhite()){
                        if(to.y == 7) {
                            currentChecker.changeTyp('W');
                            if (gameInProcess)
                                logicDesk.set(to.x,to.y,'W');
                        }
                    } else {
                        if(to.y == 0) {
                            currentChecker.changeTyp('B');
                            if (gameInProcess)
                                logicDesk.set(to.x,to.y,'B');
                        }
                    }
                    setCheckerOnCell(currentChecker, to.x, to.y);
                } else {
                    if (currentChecker != null)
                        setCheckerOnCell(currentChecker, from.x, from.y);
                }
            }
        }
    }
}
