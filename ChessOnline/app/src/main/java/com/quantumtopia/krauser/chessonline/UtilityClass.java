package com.quantumtopia.krauser.chessonline;


import android.content.Context;
import android.content.Intent;
import android.os.Message;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by Krauser on 1/7/2015.
 */
public class UtilityClass
{
    public static Vector<ScreenCoordinates>     points;
    public static Vector<Vector<Integer>>       figureMoves;
    private static int                          screenWidth;
    private static int                          screenHeight;
    public static int[]                         gameState;
    public static int[]                         moveState;
    public static Context                       cntxt;
    public static boolean                       registered = false;
    public static Vector<MessageClass>          publicRoomMessages;
    public static int                           mostRecentMessageId = 0;
    public static String                        userName;
    public static int                           currentId;
    public static boolean                       gotNewMessage;
    public static int                           publicMessageId;

    public UtilityClass()
    {
    }

    public static void createPoints()
    {
        points = new Vector<ScreenCoordinates>();
        screenHeight = GamePage.screenHeight;
        screenWidth = GamePage.screenWidth;

        int sectionX = screenWidth / 8;
        int sectionY = sectionX;

        int currentX1 = GamePage.imageX;
        int currentY1 = GamePage.imageY;
        int currentX2 = GamePage.imageX + sectionX;
        int currentY2 = GamePage.imageY + sectionY;

        //System.out.println("X section: " + sectionX);
        //System.out.println("Image position:" + currentX1 + " " + currentY1);

        for(int i = 0; i < 64; i++)
        {
            if(i % 8 == 0 && i != 0)
            {
                currentX1 = GamePage.imageX;
                currentX2 = currentX1 + sectionX;
                currentY1 = currentY2;
                currentY2 = currentY2 + sectionY;
                //System.out.println("\n");
            }

            int xMidPoint = ( currentX1 + currentX2 ) / 2;
            int yMidPoint = ( currentY1 + currentY2 ) / 2;
            //System.out.print(i + "( " + xMidPoint + " , " + yMidPoint + " )\t");
            ScreenCoordinates sc = new ScreenCoordinates(i, xMidPoint, yMidPoint, currentX1 - GamePage.imageX + 5, currentY1 - GamePage.imageY + 5);
            points.addElement(sc);
            //System.out.println("Point created: " + xMidPoint + " " + yMidPoint);

            currentX1 = currentX2;
            currentX2 = currentX2 + sectionX;

        }
    }

    public static void initializeGameState()
    {
        moveState = new int[64];
        gameState = new int[64];
        // 0 - white pawn           6 - black pawn
        // 1 - white castle         7 - black castle
        // 2 - white horse          8 - black horse
        // 3 - white officer        9 - black officer
        // 4 - white queen          10 - black queen
        // 5 - white king           11 - black king

        for(int i = 0; i < 64; i++)
        {
            gameState[i] = -1;
            moveState[i] = 0;
        }

        for(int i = 8; i < 16; i++)
        {
            gameState[i] = 0;
            gameState[i + 40] = 6;
        }

        gameState[0] = 1;   gameState[7] = 1;
        gameState[1] = 2;   gameState[6] = 2;
        gameState[2] = 3;   gameState[5] = 3;
        gameState[3] = 5;   gameState[4] = 4;

        gameState[56] = 7;   gameState[63] = 7;
        gameState[57] = 8;   gameState[62] = 8;
        gameState[58] = 9;   gameState[61] = 9;
        gameState[59] = 11;   gameState[60] = 10;
    }

    public static Vector<Integer> getValidMoves(int index)
    {
        Vector<Integer> toReturn = new Vector<Integer>();
        if(gameState[index] == 0)
            checkWhitePawn(toReturn, index);
        else if(gameState[index] == 1)
            checkCastle(toReturn, index, 6);
        else if(gameState[index] == 2)
            checkHorse(toReturn, index, 6);
        else if(gameState[index] == 3)
            checkKnight(toReturn, index, 6);
        else if(gameState[index] == 4)
            checkQueen(toReturn, index, 6);
        else if(gameState[index] == 5)
            checkKing(toReturn, index, 6);
        else if(gameState[index] == 6)
            checkBlackPawn(toReturn, index);
        else if(gameState[index] == 7)
            checkCastle(toReturn, index, 0);
        else if(gameState[index] == 8)
            checkHorse(toReturn, index, 0);
        else if(gameState[index] == 9)
            checkKnight(toReturn, index, 0);
        else if(gameState[index] == 10)
            checkQueen(toReturn, index, 0);
        else if(gameState[index] == 11)
            checkKing(toReturn, index, 0);


        return toReturn;
    }

    private static void checkWhitePawn(Vector<Integer> toReturn, int index)
    {
        if(index > 55)
            return;

        if(gameState[index + 8] == -1)
        {
            toReturn.addElement(index + 8);
            if (index < 16 && gameState[index + 16] == -1)
                toReturn.addElement(index + 16);
        }
        if(isLeftMost(index))
        {
            if(gameState[index + 9] > 5)
            {
                toReturn.addElement(index + 9);
            }
        }
        else if(isRightMost(index))
        {
            if(gameState[index + 7] > 5)
            {
                toReturn.addElement(index + 7);
            }
        }
        else
        {
            if(gameState[index + 9] > 5)
            {
                toReturn.addElement(index + 9);
            }

            if(gameState[index + 7] > 5)
            {
                toReturn.addElement(index + 7);
            }
        }
    }

    private static void checkBlackPawn(Vector<Integer> toReturn, int index)
    {
        if(index < 8)
            return;

        if(gameState[index - 8] == -1)
        {
            toReturn.addElement(index - 8);
            if (index > 47 && gameState[index - 16] == -1)
                toReturn.addElement(index - 16);
        }
        if(isLeftMost(index))
        {
            if(gameState[index - 9] > -1 && gameState[index - 9] < 6)
            {
                toReturn.addElement(index - 9);
            }
        }
        else if(isRightMost(index))
        {
            if(gameState[index - 7] > -1 && gameState[index - 7] < 6)
            {
                toReturn.addElement(index - 7);
            }
        }
        else
        {
            if(gameState[index - 9] > -1 && gameState[index - 9] < 6)
            {
                toReturn.addElement(index - 9);
            }

            if(gameState[index - 7] > -1 && gameState[index - 7] < 6)
            {
                toReturn.addElement(index - 7);
            }
        }
    }

    private static void checkCastle(Vector<Integer> toReturn, int index, int enemy)
    {
        boolean checkLeft = (isLeftMost(index))? false: true;
        boolean checkRight = (isRightMost(index))? false: true;
        boolean checkUp = (isTop(index))? false: true;
        boolean checkDown = (isBottom(index))? false: true;

        System.out.println(checkLeft + " " + checkDown + " " + checkUp + " " + checkRight);

        for(int i = 1; i < 7; i++)
        {
            if(checkLeft)
                if(gameState[index - i] == -1)
                    toReturn.addElement(index - i);
                else if(gameState[index - i] >= enemy && gameState[index - i] <= enemy + 5)
                {
                    toReturn.addElement(index - i);
                    checkLeft = false;
                }
                else checkLeft = false;

            if(checkRight)
                if(gameState[index + i] == -1)
                    toReturn.addElement(index + i);
                else if(gameState[index + i] >= enemy && gameState[index + i] <= enemy + 5)
                {
                    toReturn.addElement(index + i);
                    checkRight = false;
                }
                else checkRight = false;

            if(checkDown)
                if(gameState[index + i * 8] == -1)
                    toReturn.addElement(index + i * 8);
                else if(gameState[index + i * 8] >= enemy && gameState[index + i * 8] <= enemy + 5)
                {
                    toReturn.addElement(index + i * 8);
                    checkDown = false;
                }
                else checkDown = false;

            if(checkUp)
                if(gameState[index - i * 8] == -1)
                    toReturn.addElement((index - i * 8));
                else if(gameState[index - i * 8] >= enemy && gameState[index - i * 8] <= enemy + 5)
                {
                    toReturn.addElement(index - i * 8);
                    checkUp = false;
                }
                else checkUp = false;

            if(isLeftMost(index - i))       checkLeft  = false;
            if(isTop(index - i * 8))        checkUp    = false;
            if(isBottom(index + i * 8))     checkDown  = false;
            if(isRightMost(index + i))      checkRight = false;
        }
    }

    private static void checkHorse(Vector<Integer> toReturn, int index, int enemy)
    {
        //2 spaces right 1 space up
        boolean checkRightUp = (!isRightMost(index) && !isRightMost(index + 1) && !isTop(index))? true: false;
        //1 space right 2 spaces up
        boolean checkUpRight = (!isRightMost(index) && !isTop(index) && !isTop(index - 8))? true: false;
        //1 space left 2 spaces up
        boolean checkUpLeft = (!isLeftMost(index) && !isTop(index) && !isTop(index - 8))? true: false;
        //2 spaces left 1 space up
        boolean checkLeftUp = (!isLeftMost(index) && !isLeftMost(index - 1) && !isTop(index))? true: false;
        //2 spaces left 1 space down
        boolean checkLeftDown = (!isLeftMost(index) && !isLeftMost(index - 1) && !isBottom(index))? true: false;
        //1 space left 2 spaces down
        boolean checkDownLeft = (!isLeftMost(index) && !isBottom(index) && !isBottom(index + 8))? true: false;
        //1 space right 2 spaces down
        boolean checkDownRight = (!isRightMost(index) && !isBottom(index) && !isBottom(index + 8))? true: false;
        //2 spaces right 1 space down
        boolean checkRightDown = (!isRightMost(index) && !isRightMost(index + 1) && !isBottom(index))? true: false;

        System.out.println(checkRightUp + " " + checkUpRight + " " + checkUpLeft + " " + checkLeftUp +
            " " + checkLeftDown + " " + checkDownLeft + " " + checkDownRight + " " + checkRightDown);

        if(checkRightUp)
            if(gameState[index - 6] == -1 || gameState[index - 6] >= enemy && gameState[index - 6] <= enemy + 5)
                toReturn.addElement(index - 6);
        if(checkUpRight)
            if(gameState[index - 15] == -1 || gameState[index - 15] >= enemy && gameState[index - 15] <= enemy + 5)
                toReturn.addElement(index - 15);
        if(checkUpLeft)
            if(gameState[index - 17] == -1 || gameState[index - 17] >= enemy && gameState[index - 17] <= enemy + 5)
                toReturn.addElement(index - 17);
        if(checkLeftUp)
            if(gameState[index - 10] == -1 || gameState[index - 10] >= enemy && gameState[index - 10] <= enemy + 5)
                toReturn.addElement(index - 10);
        if(checkLeftDown)
            if(gameState[index + 6] == -1 || gameState[index + 6] >= enemy && gameState[index + 6] <= enemy + 5)
                toReturn.addElement(index + 6);
        if(checkDownLeft)
            if(gameState[index + 15] == -1 || gameState[index + 15] >= enemy && gameState[index +15] <= enemy + 5)
                toReturn.addElement(index + 15);
        if(checkDownRight)
            if(gameState[index + 17] == -1 || gameState[index + 17] >= enemy && gameState[index + 17] <= enemy + 5)
                toReturn.addElement(index + 17);
        if(checkRightDown)
            if(gameState[index + 10] == -1 || gameState[index + 10] >= enemy && gameState[index + 10] <= enemy + 5)
                toReturn.addElement(index + 10);
    }

    private static void checkKnight(Vector<Integer> toReturn, int index, int enemy)
    {
        boolean checkRightUp = (isRightMost(index) || isTop(index))? false: true;
        boolean checkLeftUp = (isLeftMost(index) || isTop(index))? false: true;
        boolean checkLeftDown = (isLeftMost(index) || isBottom(index))? false: true;
        boolean checkRightDown = (isRightMost(index) || isBottom(index))? false: true;

        for(int i = 1; i < 7; i++)
        {
            if(checkRightUp)
                if(gameState[index - i * 7] == -1)
                    toReturn.addElement(index - i * 7);
                else if(gameState[index - i * 7] >= enemy && gameState[index - i * 7] <= enemy + 5)
                {
                    toReturn.addElement(index - i * 7);
                    checkRightUp = false;
                }
                else    checkRightUp = false;

            if(checkLeftUp)
                if(gameState[index - i * 9] == -1)
                    toReturn.addElement(index - i * 9);
                else if(gameState[index - i * 9] >= enemy && gameState[index - i * 9] <= enemy + 5)
                {
                    toReturn.addElement(index - i * 9);
                    checkLeftUp = false;
                }
                else    checkLeftUp = false;

            if(checkLeftDown)
                if(gameState[index + i * 7] == -1)
                    toReturn.addElement(index + i * 7);
                else if(gameState[index + i * 7] >= enemy && gameState[index + i * 7] <= enemy + 5)
                {
                    toReturn.addElement(index + i * 7);
                    checkLeftDown = false;
                }
                else    checkLeftDown = false;

            if(checkRightDown)
                if(gameState[index + i * 9] == -1)
                    toReturn.addElement(index + i * 9);
                else if(gameState[index + i * 9] >= enemy && gameState[index + i * 9] <= enemy + 5)
                {
                    toReturn.addElement(index + i * 9);
                    checkRightDown = false;
                }
                else    checkRightDown = false;

            if(isRightMost(index - i * 7) || isTop(index - i * 7))      checkRightUp    = false;
            if(isLeftMost(index - i * 9) || isTop(index - i * 9))       checkLeftUp     = false;
            if(isLeftMost(index + i * 7) || isBottom(index + i * 7))    checkLeftDown   = false;
            if(isRightMost(index + i * 9) || isBottom(index + i * 9))   checkRightDown  = false;
        }
    }

    private static void checkQueen(Vector<Integer> toReturn, int index, int enemy)
    {
        boolean checkLeft = (isLeftMost(index))? false: true;
        boolean checkRight = (isRightMost(index))? false: true;
        boolean checkUp = (isTop(index))? false: true;
        boolean checkDown = (isBottom(index))? false: true;
        boolean checkRightUp = (isRightMost(index) || isTop(index))? false: true;
        boolean checkLeftUp = (isLeftMost(index) || isTop(index))? false: true;
        boolean checkLeftDown = (isLeftMost(index) || isBottom(index))? false: true;
        boolean checkRightDown = (isRightMost(index) || isBottom(index))? false: true;

        for(int i = 1; i < 7; i++)
        {
            if(checkRightUp)
                if(gameState[index - i * 7] == -1)
                    toReturn.addElement(index - i * 7);
                else if(gameState[index - i * 7] >= enemy && gameState[index - i * 7] <= enemy + 5)
                {
                    toReturn.addElement(index - i * 7);
                    checkRightUp = false;
                }
                else    checkRightUp = false;

            if(checkLeftUp)
                if(gameState[index - i * 9] == -1)
                    toReturn.addElement(index - i * 9);
                else if(gameState[index - i * 9] >= enemy && gameState[index - i * 9] <= enemy + 5)
                {
                    toReturn.addElement(index - i * 9);
                    checkLeftUp = false;
                }
                else    checkLeftUp = false;

            if(checkLeftDown)
                if(gameState[index + i * 7] == -1)
                    toReturn.addElement(index + i * 7);
                else if(gameState[index + i * 7] >= enemy && gameState[index + i * 7] <= enemy + 5)
                {
                    toReturn.addElement(index + i * 7);
                    checkLeftDown = false;
                }
                else    checkLeftDown = false;

            if(checkRightDown)
                if(gameState[index + i * 9] == -1)
                    toReturn.addElement(index + i * 9);
                else if(gameState[index + i * 9] >= enemy && gameState[index + i * 9] <= enemy + 5)
                {
                    toReturn.addElement(index + i * 9);
                    checkRightDown = false;
                }
                else    checkRightDown = false;

            if(checkLeft)
                if(gameState[index - i] == -1)
                    toReturn.addElement(index - i);
                else if(gameState[index - i] >= enemy && gameState[index - i] <= enemy + 5)
                {
                    toReturn.addElement(index - i);
                    checkLeft = false;
                }
                else checkLeft = false;

            if(checkRight)
                if(gameState[index + i] == -1)
                    toReturn.addElement(index + i);
                else if(gameState[index + i] >= enemy && gameState[index + i] <= enemy + 5)
                {
                    toReturn.addElement(index + i);
                    checkRight = false;
                }
                else checkRight = false;

            if(checkDown)
                if(gameState[index + i * 8] == -1)
                    toReturn.addElement(index + i * 8);
                else if(gameState[index + i * 8] >= enemy && gameState[index + i * 8] <= enemy + 5)
                {
                    toReturn.addElement(index + i * 8);
                    checkDown = false;
                }
                else checkDown = false;

            if(checkUp)
                if(gameState[index - i * 8] == -1)
                    toReturn.addElement((index - i * 8));
                else if(gameState[index - i * 8] >= enemy && gameState[index - i * 8] <= enemy + 5)
                {
                    toReturn.addElement(index - i * 8);
                    checkUp = false;
                }
                else checkUp = false;

            if(isLeftMost(index - i))       checkLeft  = false;
            if(isTop(index - i * 8))        checkUp    = false;
            if(isBottom(index + i * 8))     checkDown  = false;
            if(isRightMost(index + i))      checkRight = false;

            if(isRightMost(index - i * 7) || isTop(index - i * 7))      checkRightUp    = false;
            if(isLeftMost(index - i * 9) || isTop(index - i * 9))       checkLeftUp     = false;
            if(isLeftMost(index + i * 7) || isBottom(index + i * 7))    checkLeftDown   = false;
            if(isRightMost(index + i * 9) || isBottom(index + i * 9))   checkRightDown  = false;
        }
    }

    private static void checkKing(Vector<Integer> toReturn, int index, int enemy)
    {
        boolean checkLeft = (isLeftMost(index))? false: true;
        boolean checkRight = (isRightMost(index))? false: true;
        boolean checkUp = (isTop(index))? false: true;
        boolean checkDown = (isBottom(index))? false: true;
        boolean checkRightUp = (isRightMost(index) || isTop(index))? false: true;
        boolean checkLeftUp = (isLeftMost(index) || isTop(index))? false: true;
        boolean checkLeftDown = (isLeftMost(index) || isBottom(index))? false: true;
        boolean checkRightDown = (isRightMost(index) || isBottom(index))? false: true;

        if(checkRightUp)
            if(gameState[index - 7] == -1)
                toReturn.addElement(index - 7);
            else if(gameState[index - 7] >= enemy && gameState[index - 7] <= enemy + 5)
            {
                toReturn.addElement(index - 7);
            }

        if(checkLeftUp)
            if(gameState[index - 9] == -1)
                toReturn.addElement(index - 9);
            else if(gameState[index - 9] >= enemy && gameState[index - 9] <= enemy + 5)
            {
                toReturn.addElement(index - 9);
            }

        if(checkLeftDown)
            if(gameState[index + 7] == -1)
                toReturn.addElement(index + 7);
            else if(gameState[index + 7] >= enemy && gameState[index + 7] <= enemy + 5)
            {
                toReturn.addElement(index + 7);
            }

        if(checkRightDown)
            if(gameState[index + 9] == -1)
                toReturn.addElement(index + 9);
            else if(gameState[index + 9] >= enemy && gameState[index + 9] <= enemy + 5)
            {
                toReturn.addElement(index + 9);
            }

        if(checkLeft)
            if(gameState[index - 1] == -1)
                toReturn.addElement(index - 1);
            else if(gameState[index - 1] >= enemy && gameState[index - 1] <= enemy + 5)
            {
                toReturn.addElement(index - 1);
            }

        if(checkRight)
            if(gameState[index + 1] == -1)
                toReturn.addElement(index + 1);
            else if(gameState[index + 1] >= enemy && gameState[index + 1] <= enemy + 5)
            {
                toReturn.addElement(index + 1);
            }

        if(checkDown)
            if(gameState[index + 8] == -1)
                toReturn.addElement(index + 8);
            else if(gameState[index + 8] >= enemy && gameState[index + 8] <= enemy + 5)
            {
                toReturn.addElement(index + 8);
            }

        if(checkUp)
            if(gameState[index - 8] == -1)
                toReturn.addElement((index - 8));
            else if(gameState[index - 8] >= enemy && gameState[index - 8] <= enemy + 5)
            {
                toReturn.addElement(index - 8);
            }
    }

    private static boolean isLeftMost(int index)
    {
        return (index % 8 == 0)? true: false;
    }

    private static boolean isRightMost(int index)
    {
        return ((index - 7) % 8 == 0)? true: false;
    }

    private static boolean isTop(int index)
    {
        return (index < 8)? true: false;
    }

    private static boolean isBottom(int index)
    {
        return (index > 55)? true: false;
    }

    public static void printGameState()
    {
        String toPrint = "";
        for(int i = 0; i < 64; i++)
        {
            if(i % 8 == 0 && i != 0)
                toPrint += "\n";
            toPrint += "\t" + gameState[i] + "\t";
        }
        System.out.println(toPrint);
    }

    public static String readRegistrationInformation()
    {
        if(fileExists("userInfo.txt"))
        {
            try
            {
                registered = true;
                FileInputStream fin = cntxt.openFileInput("userInfo.txt");
                int c;
                userName = "";
                while ((c = fin.read()) != -1)
                {
                    userName += Character.toString((char) c);
                }
                System.out.println("Username: " + userName);
            }
            catch (IOException e) { e.printStackTrace(); }
        }
        else
        {
        }

        return null;
    }

    public static void writeRegistrationInformation(String userName)
    {
        try
        {
            FileOutputStream fos = cntxt.openFileOutput("userInfo.txt", Context.MODE_PRIVATE);
            fos.write(userName.getBytes());
            fos.close();
            registered = true;
            UtilityClass.userName = userName;
        }
        catch(IOException e) { e.printStackTrace(); }
    }

    public static void deleteUserFile()
    {
        cntxt.deleteFile("userInfo.txt");
        registered = false;
    }

    public static boolean fileExists(String fname)
    {
        File file = cntxt.getFileStreamPath(fname);
        return file.exists();
    }

    public static String getPublicMessageFromServer()
    {
        ServerCommunication.receivedFromServer = false;
        ServerCommunication sc = new ServerCommunication("getPublicRoomMessage.php");
        sc.execute();

        while(!ServerCommunication.receivedFromServer)
        {
        }

        String message = "";
        try
        {
            message = ServerCommunication.posts.getString("message");
            PublicChatroomPage.user = ServerCommunication.posts.getString("user");
            String user = PublicChatroomPage.user;
            currentId = Integer.parseInt(ServerCommunication.posts.getString("id"));
            if(currentId > mostRecentMessageId && !(user.equals(userName)))
            {
                gotNewMessage = true;
                mostRecentMessageId = currentId;
                MessageClass m = new MessageClass(message, user);
                publicRoomMessages.addElement(m);
                System.out.println("Message received: " + message);
            }
        }
        catch (JSONException e) {e.printStackTrace(); }

        ServerCommunication.receivedFromServer = false;
        return message;
    }

    public static void sendPublicMessageToServer(String message)
    {
        ServerCommunication.receivedFromServer = false;
        ServerCommunication sc = new ServerCommunication("sendPublicRoomMessage.php?" + message + "=" + userName);
        sc.execute();

        /*
        while(!ServerCommunication.receivedFromServer)
        {
        }

        String message = "";
        String user = "";
        try
        {
            message = ServerCommunication.posts.getString("mostRecentMessage");
            user = ServerCommunication.posts.getString("user");
            int currentId = Integer.parseInt(ServerCommunication.posts.getString("id"));
            if(currentId > mostRecentMessageId)
            {
                mostRecentMessageId = currentId;
                MessageClass m = new MessageClass(message, user);
                publicRoomMessages.addElement(m);
            }
        }
        catch (JSONException e) {e.printStackTrace(); }

        ServerCommunication.receivedFromServer = false;
        */
    }

    public static int pixelsFromDps(int dp)
    {
        float scale = cntxt.getResources().getDisplayMetrics().density;
        return (int) (dp*scale + 0.5f);
    }
}
