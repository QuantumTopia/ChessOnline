package com.quantumtopia.krauser.chessonline;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.graphics.Point;
import android.view.Display;

import java.util.Vector;

public class GamePage extends Activity
{
    private int i, j;
    private TableLayout kTable;
    public static int screenWidth;
    public static int screenHeight;
    public static int imageX;
    public static int imageY;
    private ImageView iv;
    private boolean loaded = false;
    private SurfaceView gameSurface;
    private SurfaceHolder gameSurfaceHolder;
    private Bitmap bitmap;
    private int figureIndex = -1;
    public static boolean isYourTurn = true;
    public static boolean preTurn = true;
    public static boolean postTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        UtilityClass.initializeGameState();

        gameSurface = (SurfaceView) findViewById(R.id.game_surface_view);
        gameSurfaceHolder = gameSurface.getHolder();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chess_board_small);
    }

    private void drawGameState(Canvas canvas)
    {
        Paint paint = new Paint();
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenWidth, true);
        canvas.drawBitmap(newBitmap, new Matrix(), paint);
        for(int i = 0; i < 64; i++)
        {
            float x = UtilityClass.points.elementAt(i).getDrawX();
            float y = UtilityClass.points.elementAt(i).getDrawY();
            int size = screenWidth / 8 - 10;
            int figure = UtilityClass.gameState[i];

            Bitmap figureBitmap;
            Bitmap newFigureBitmap;

            switch (figure)
            {
                case 0:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_pawn);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 1:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_tower);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 2:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_horse);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 3:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_knight);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 4:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_queen);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 5:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_king);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 6:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_pawn);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 7:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_tower);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 8:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_horse);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 9:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_knight);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 10:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_queen);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
                case 11:
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                    break;
            }
            if(UtilityClass.moveState[i] == 12)
            {
                if(UtilityClass.gameState[i] > -1)
                {
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_square);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                }
                else
                {
                    figureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_square);
                    newFigureBitmap = Bitmap.createScaledBitmap(figureBitmap, size, size, true);
                    canvas.drawBitmap(newFigureBitmap, x, y, paint);
                }
            }
        }
        if(figureIndex > -1)
        {
            float x = UtilityClass.points.elementAt(figureIndex).getDrawX();
            float y = UtilityClass.points.elementAt(figureIndex).getDrawY();
            int size = screenWidth / 8 - 10;
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.heavy_black_square);
            Bitmap newB = Bitmap.createScaledBitmap(b, size, size, true);
            canvas.drawBitmap(newB, x, y, paint);
        }
    }

    public void getScreenSize()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        int xy[] = new int[2];
        gameSurface.getLocationOnScreen(xy);
        imageX = xy[0];
        imageY = xy[1];
    }

    public int getClosestIndex(float x, float y)
    {
        float distance = screenWidth / 14;
        int index = 64;
        for(int i = 0; i < 64; i++)
        {
            int pointX = UtilityClass.points.get(i).getX();
            int pointY = UtilityClass.points.get(i).getY();
            float currentDistance = (float) Math.sqrt( (x - pointX) * (x - pointX) + (y - pointY) * (y - pointY));;
            //System.out.println("point: " + pointX + " " + pointY + "Distance: " + currentDistance);

            if(currentDistance < distance)
            {
                index = i;
                distance = currentDistance;
            }
        }

        return index;
    }

    private void drawBoard()
    {
        Canvas canvas = gameSurfaceHolder.lockCanvas();
        drawGameState(canvas);
        gameSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void checkMove(int indexTouched)
    {
        if(indexTouched < 64 && UtilityClass.gameState[indexTouched] > -1)
        {
            Vector<Integer> availableMoves = UtilityClass.getValidMoves(indexTouched);
            for(int i = 0; i < availableMoves.size(); i++)
            {
                UtilityClass.moveState[availableMoves.elementAt(i)] = 12;
            }
            figureIndex = indexTouched;
            preTurn = false;
            postTurn = true;

            drawBoard();

            //resetPreTurn();
        }
    }

    public void makeExternalMove(int sourceIndex, int destinationIndex)
    {
        UtilityClass.gameState[destinationIndex] = UtilityClass.gameState[sourceIndex];
        UtilityClass.gameState[sourceIndex] = -1;
        drawBoard();
    }

    public void testMakeMove(View v)
    {
        EditText et1 = (EditText) findViewById(R.id.test_ET_1);
        String text1 = et1.getText().toString();
        EditText et2 = (EditText) findViewById(R.id.test_ET_2);
        String text2 = et2.getText().toString();

        makeExternalMove(Integer.parseInt(text1), Integer.parseInt(text2));
    }

    private void makeMove(int destination)
    {
        if(destination > -1 && destination < 64 && UtilityClass.moveState[destination] == 12)
        {
            UtilityClass.gameState[destination] = UtilityClass.gameState[figureIndex];
            UtilityClass.gameState[figureIndex] = -1;
            preTurn = true;
            postTurn = false;
            figureIndex = -1;

            resetPreTurn();
            drawBoard();

        }
        if(destination == figureIndex)
        {
            //UtilityClass.gameState[figureIndex] = -1;
            preTurn = true;
            postTurn = false;
            figureIndex = -1;
            resetPreTurn();
            drawBoard();
        }
    }

    public void clearBoard(View v)
    {
        for(int i = 0; i < 64; i++)
        {
            UtilityClass.gameState[i] = -1;
        }
        drawBoard();
    }

    private void resetPreTurn()
    {
        for(int i = 0; i < 64; i++)
        {
            if(UtilityClass.moveState[i] == 12)
                UtilityClass.moveState[i] = 0;
        }
        figureIndex = -1;
    }

    public void test(View v)
    {
        EditText et1 = (EditText) findViewById(R.id.test_ET_1);
        String text1 = et1.getText().toString();
        EditText et2 = (EditText) findViewById(R.id.test_ET_2);
        String text2 = et2.getText().toString();

        UtilityClass.gameState[Integer.parseInt(text1)] = Integer.parseInt(text2);

        drawBoard();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(loaded)
            getScreenSize();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus)
        {
            getScreenSize();
            UtilityClass.createPoints();
            drawBoard();
            loaded = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // MotionEvent object holds X-Y values
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //resetState();

            int indexTouched = getClosestIndex(event.getX(), event.getY());
            String text = "You click at x = " + event.getX() + " and y = " + event.getY()
                    + "\nIndex clicked: " + indexTouched;
            //Toast.makeText(this, text, Toast.LENGTH_LONG).show();

            if(isYourTurn && postTurn)
                makeMove(indexTouched);
            else if(isYourTurn && preTurn)
                checkMove(indexTouched);
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
