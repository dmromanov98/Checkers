package Checkers.Objects;

import java.util.ArrayList;

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

    public boolean isTurnFinished(Turn turn)
    {
        return !isHereABoy(turn);
    }

    public boolean isHereABoy(Turn turn) {
        char checker = get(turn.x1, turn.y1), opposite = Character.toLowerCase(checker) == 'w' ? 'b' : 'w';
        char[] line1 = new char[8], line2 = new char[8];

        for (int shift = -4; shift < 4; shift++) {
            line1[shift + 4] =  get(turn.x1 + shift, turn.y1 + shift);
            line1[shift + 4] =  get(turn.x1 + shift, turn.y1 - shift);
        }
        int len = 8; char prev1 = '*', prev2 = '*';
        for (int i = 0; i<len; i++){
            if (i < 4) {
                if (Character.toLowerCase(line1[i]) == opposite && prev1 == ' ' ||
                    Character.toLowerCase(line2[i]) == opposite && prev2 == ' ' )
                {
                    if (checker == 'W' || checker == 'B')
                        return true;
                    else if ( Math.abs(i - 4) == 2 ) return true;
                }
                prev1 = line1[i];
                prev2 = line2[i];
            } else {
                char next1 = i+1 < len ? line1[i+1]:'*', next2 = i+1 < len ? line2[i+1]:'*';
                if (Character.toLowerCase(line1[i]) == opposite && next1 == ' ' ||
                        Character.toLowerCase(line2[i]) == opposite &&  next2 == ' ' )
                {
                    if (checker == 'W' || checker == 'B')
                        return true;
                    else if ( Math.abs(i - 4) == 2 ) return true;
                }
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
    }

    public boolean checkTurnPossibility(Turn turn)
    {
        char checker = get(turn.x1, turn.y1);
        if ( (Character.toLowerCase(checker) != 'w' && Character.toLowerCase(checker) != 'b') || //шашка ли вообще
             //(Character.toLowerCase(checker) != whomTurn) || //ход этой стороны?
             (turn.x2%2 == 0)&&(turn.y2%2 == 1) || (turn.x2%2 == 1)&&(turn.y2%2 == 0) || //ход на черные клетки?
             (!Character.isUpperCase(checker) && (isItABoy(turn) == null) && ( Character.toLowerCase(checker) == 'w' ?  (turn.y1 - turn.y2) > 0 : (turn.y1 - turn.y2) < 0)) || //чтобы белые вверх, а черные вниз
             (Math.abs(turn.x1 - turn.x2) != Math.abs(turn.y1 - turn.y2)))// || //ход по диагонали?
             //(!Character.isUpperCase(checker) && Math.abs(turn.x1 - turn.x2) > 1 && Math.abs(turn.x1 - turn.x2) < 3 && (isItABoy(turn)!=null))) //|| //не ходит ли обычная шашка как дамка?
             //((isItABoy(turn) == null) && isHereABoy(turn))) //isItABoy(turn) == //нет ли боя? если есть, то побили?
            return false;
        return true;
    }

    public boolean turn(Turn turn)
    {
        if (whomTurn == 't') {
            return true;
        } else {
            if (!checkTurnPossibility(turn))
                return false;
            set(isItABoy(turn), '*');
            applyTurn(turn);
            if (!isHereABoy(turn.flip()))
                nextTurn();
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