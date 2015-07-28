package com.quantumtopia.krauser.chessonline;

/**
 * Created by Krauser on 1/7/2015.
 */
public class ScreenCoordinates
{
    private int index;
    private int x;
    private int y;
    private int drawX;
    private int drawY;

    // 0 - white pawn           6 - black pawn
    // 1 - white castle         7 - black castle
    // 2 - white horse          8 - black horse
    // 3 - white officer        9 - black officer
    // 4 - white queen          10 - black queen
    // 5 - white king           11 - black king

    public ScreenCoordinates(int i, int x, int y, int dX, int dY)
    {
        index = i;
        this.x = x;
        this.y = y;
        drawX = dX;
        drawY = dY;
    }

    public int getIndex()
    {
        return index;
    }

    public int getX()
    {
        return x;
    }

    public int getY() { return y; }
    public int getDrawX()
    {
        return drawX;
    }

    public int getDrawY() { return drawY; }

}
