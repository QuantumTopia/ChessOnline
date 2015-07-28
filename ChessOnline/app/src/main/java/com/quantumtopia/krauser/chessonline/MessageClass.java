package com.quantumtopia.krauser.chessonline;

/**
 * Created by Krauser on 1/10/2015.
 */
public class MessageClass
{
    private String message;
    private String sender;

    public MessageClass(String m, String s)
    {
        message = m;
        sender = s;
    }

    public String getMessage() { return message; }
    public String getSender() { return sender; }
}
