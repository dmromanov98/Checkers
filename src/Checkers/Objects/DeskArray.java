package Checkers.Objects;

import java.util.ArrayList;
import java.util.Vector;

public class DeskArray
{
    public static final char[] checkerDesk =
            {
                    'w',' ','w',' ','w',' ','w',' ',
                    ' ','w',' ','w',' ','w',' ','w',
                    'w',' ','w',' ','w',' ','w',' ',
                    ' ',' ',' ',' ',' ',' ',' ',' ',
                    ' ',' ',' ',' ',' ',' ',' ',' ',
                    ' ','b',' ','b',' ','b',' ','b',
                    'b',' ','b',' ','b',' ','b',' ',
                    ' ','b',' ','b',' ','b',' ','b'
            };

    private char[] desk;

    public DeskArray()
    {
        desk = new char[8*8];
        for (char c:
             desk) {
            c = ' ';
        }
    }

    public void newGame()
    {
        desk = checkerDesk.clone();
        whomTurn = 'w';
    }

    public char get(int x, int y)
    {
        if (x < 8 && x >= 0 && y < 8 && y >= 0)
            return desk[x + y*8];
        else
            return '*';//шашкам не познать, что происходит за краем их вселенной...
    }

    public char get(Turn t)
    {
        if (t.x1 < 8 && t.x1 >= 0 && t.y1 < 8 && t.y1 >= 0)
            return desk[t.x1 + t.y1*8];
        else
            return '*';//шашкам не познать, что происходит за краем их вселенной...
    }

    public void set(int x, int y, char ch)
    {
        if (x < 8 && x >= 0 && y < 8 && y >= 0)
            desk[x + y*8] = ch;
    }

    public void set(Turn t, char ch) //для упрощения кода в некоторых местах интерпретирую ход как двумерный вектор
    {
        if (t == null) return;
        if (t.x1 < 8 && t.x2 >= 0 && t.y1 < 8 && t.y2 >= 0)
            desk[t.x1 + t.y1*8] = ch;
    }

    private static int checkLineLen = 16;

    private boolean turnInKilledVector(Turn turn)
    {
        for (Turn t:
             killedCheckers) {
            if (t.equals(turn))
                return true;
        }
        return false;
    }

    public boolean isHereABoy(Turn turn) {
        char checker = get(turn.x1, turn.y1), opposite = Character.toLowerCase(checker) == 'w' ? 'b' : 'w';
        char[] line1 = new char[checkLineLen], line2 = new char[checkLineLen];

        for (int shift = -checkLineLen/2; shift < checkLineLen/2; shift++) {
            Turn curTl1 = new Turn(turn.x1 + shift, turn.y1 + shift, 0, 0);
            Turn curTl2 = new Turn(turn.x1 - shift, turn.y1 + shift, 0, 0);

            if (turnInKilledVector(curTl1))
            {
                line1[shift + checkLineLen/2] = 'x';
            }
            else line1[shift + checkLineLen/2] =  get(curTl1);

            if (turnInKilledVector(curTl2))
            {
                line2[shift + checkLineLen/2] = 'x';
            }
            else line2[shift + checkLineLen/2] =  get(curTl2);
        }

        line1[checkLineLen/2] = 'X';
        line2[checkLineLen/2] = 'X';
        System.out.print(line1);
        System.out.print("|||||||");
        System.out.println(line2);

        int len = checkLineLen; char prev1 = '*', prev2 = '*';
        for (int i = 0; i<len; i++){
            char next1 = i+1 < len ? line1[i+1]:'*', next2 = i+1 < len ? line2[i+1]:'*';

            if (i < checkLineLen/2) {
                if (Character.toLowerCase(line1[i]) == opposite && prev1 == ' ' && (next1 == ' ' || next1 == 'X') ||
                    Character.toLowerCase(line2[i]) == opposite && prev2 == ' ' && (next2 == ' ' || next2 == 'X'))
                {
                    if ((checker == 'W') || (checker == 'B') || (Math.abs(i-checkLineLen/2) == 1))
                    {
                        System.out.println(i);
                        return true;
                    }
                }
            } else {
                if (Character.toLowerCase(line1[i]) == opposite && next1 == ' ' && (prev1 == ' ' || prev1 == 'X') ||
                        Character.toLowerCase(line2[i]) == opposite &&  next2 == ' ' && (prev2 == ' ' || prev2 == 'X'))
                {
                    if ((checker == 'W') || (checker == 'B') || (Math.abs(i-checkLineLen/2) == 1))
                        return true;
                }
            }

            prev1 = line1[i];
            prev2 = line2[i];
        }
        return false;
    }

    public boolean boyCheck(char side) //есть ли за выбранную сторону бой где либо
    {
        for (int x = 0; x < 8; x++)
            for(int y = 0; y < 8; y++)
            {
                if (Character.toLowerCase(get(x, y)) == side)
                {
                    if (isHereABoy(new Turn(x, y,0,0)))
                        return true;
                }
            }
        return false;
    }

    public Turn isItABoy(Turn turn)
    {
        char checker = get(turn.x1, turn.y1), opposite = Character.toLowerCase(checker) == 'w' ? 'b' : 'w';
        int x,y, dX, dY;
        if (turn.x1 - turn.x2 > 0) {
            if (turn.y1 - turn.y2 > 0) {
                dX = -1; dY = -1;
            } else {
                dX = -1; dY = 1;
            }
        }else{
            if (turn.y1 - turn.y2 > 0) {
                dX = 1; dY = -1;
            } else {
                dX = 1; dY = 1;
            }
        }
        x = turn.x1; y = turn.y1;
        while (x != turn.x2) {
            System.out.println(get(x,y));
            if ( Character.toLowerCase(get(x,y)) == opposite) {
                return new Turn(x,y,0,0);
            }
            x+= dX; y+= dY;
        }
        return null;
    }

    private void nextTurn()
    {
        whomTurn = whomTurn == 'w' ? 'b' : 'w';
        for(Turn t: killedCheckers)
        {
            set(t, '*');
        }
        killedCheckers.clear();
    }

    public boolean checkTurnPossibility(Turn turn)
    {
        char checker = get(turn.x1, turn.y1); Turn boy = isItABoy(turn);

        boolean isItAChecker_check = Character.toLowerCase(checker) == 'w' || Character.toLowerCase(checker) == 'b';
        boolean properColour_check = Character.toLowerCase(checker) == whomTurn;
        boolean properDestinationColour_check = ((turn.x2%2 == 0)||(turn.y2%2 == 1)) && ((turn.x2%2 == 1)||(turn.y2%2 == 0));
        boolean isItAQueen_check = Character.isUpperCase(checker);
        boolean properDestinationWay_check = (((Character.toLowerCase(checker) == 'w') ?  (turn.y1 - turn.y2) <= 0 : (turn.y1 - turn.y2) >= 0) || boy != null) && get(turn.x2, turn.y2) == ' ';
        boolean boy_check = (boy != null) || !boyCheck(Character.toLowerCase(checker));
        boolean properDist_check = (boy != null) ? turn.length() == 2 : turn.length() == 1;

        boolean result = isItAChecker_check && properColour_check && properDestinationColour_check && (properDestinationWay_check || isItAQueen_check) && boy_check && (properDist_check || isItAQueen_check );

        System.out.println((((Character.toLowerCase(checker) == 'w') ?  (turn.y1 - turn.y2) < 0 : (turn.y1 - turn.y2) > 0)) + "  " + (turn.y1 - turn.y2) + " " + get(turn.x2, turn.y2));
        System.out.println(isItAChecker_check + " " + properColour_check + " " + properDestinationColour_check + " " + isItAQueen_check + " " + properDestinationWay_check + " " + boy_check );

        return result;
/*
        if ( (Character.toLowerCase(checker) != 'w' && Character.toLowerCase(checker) != 'b') || //шашка ли вообще
                (Character.toLowerCase(checker) != whomTurn) || //ход этой стороны?
                (turn.x2%2 == 0)&&(turn.y2%2 == 1) || (turn.x2%2 == 1)&&(turn.y2%2 == 0) || //ход на черные клетки?
                (!Character.isUpperCase(checker) && (isItABoy(turn) == null) && ( Character.toLowerCase(checker) == 'w' ?  (turn.y1 - turn.y2) > 0 : (turn.y1 - turn.y2) < 0)) || //чтобы белые вверх, а черные вниз
                (Math.abs(turn.x1 - turn.x2) != Math.abs(turn.y1 - turn.y2)) || //ход по диагонали?
                (!Character.isUpperCase(checker) && Math.abs(turn.x1 - turn.x2) > 1 && Math.abs(turn.x1 - turn.x2) < 3 && (isItABoy(turn)!=null)) || //не ходит ли обычная шашка как дамка?
                ((isItABoy(turn) == null) && isHereABoy(turn))) //isItABoy(turn) == //нет ли боя? если есть, то побили?
            return false;
        return true;*/
    }

    Vector<Turn> killedCheckers = new Vector<>();

    public boolean turn(Turn turn)
    {
        if (whomTurn == 't') {
            return true;
        } else {
            if (!checkTurnPossibility(turn))
                return false;
            //set(isItABoy(turn), '*');
            Turn boy = isItABoy(turn);
            applyTurn(turn);
            if (boy == null) {
                nextTurn();
            } else {
                killedCheckers.add(boy);
                System.out.println(killedCheckers);
                System.out.println(!isHereABoy(turn.flip()));
                if (!isHereABoy(turn.flip()))
                {
                    nextTurn();
                }
            }
            return true;
        }
    }

    public void applyTurn(Turn turn)
    {
        char buf = get(turn.x1, turn.y1); //взяли шашку
        set(turn, ' '); set(turn.flip(), buf); //переместили
    }

    private char whomTurn = 't'; //w - white, b - black, t = training

    public char getWhomTurn()
    {
        return whomTurn;
    }

    public void toTrainingMode()
    {
        whomTurn = 't';
    }
}