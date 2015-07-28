package com.quantumtopia.krauser.chessonline;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class PublicChatroomPage extends Activity
{
    public static String message;
    public static LinearLayout l1;
    public static LinearLayout l2;
    public static ImageView profilePic;
    public static TextView tv;
    public static String user;
    private Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chatroom_page);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask()
        {

            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getMessage(null);
                    }
                });
            }

        },
        //Set how long before to start calling the TimerTask (in milliseconds)
        0,
        //Set the amount of time between each execution (in milliseconds)
        1000);
    }

    public void sendMessage(View v)
    {
        EditText messageET = (EditText) findViewById(R.id.send_message_edit_text);
        message = messageET.getText().toString();
        user = UtilityClass.userName;
        UtilityClass.sendPublicMessageToServer(message);
        updateMessages();
        messageET.setText("");
    }

    public void getMessage(View v)
    {
        UtilityClass.gotNewMessage = false;
        message = UtilityClass.getPublicMessageFromServer();
        if(UtilityClass.gotNewMessage)
            updateMessages();
    }

    private void updateMessages()
    {
        //add LInearLayout
        LinearLayout myLInearLayout =(LinearLayout) findViewById(R.id.conversations_linear_layout);

        LinearLayout ll2 = new LinearLayout(this);
        //add LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll2.setOrientation(LinearLayout.HORIZONTAL);

        // add Button
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.dark_square);
        iv.setId(999 + UtilityClass.currentId);

        //add textView
        TextView valueTV = new TextView(this);
        valueTV.setText(user + "\n" + message);
        valueTV.setId(999 * UtilityClass.currentId);
        valueTV.setLayoutParams(params);

        //add the textView and the Button to LinearLayout
        ll2.addView(iv);
        ll2.addView(valueTV);

        myLInearLayout.addView(ll2);
        /*
        LinearLayout container = (LinearLayout) findViewById(R.id.conversations_linear_layout);
        //container.removeAllViews()

        TextView messageTV = new TextView(this);
        messageTV.setId(999 + UtilityClass.currentId);
        messageTV.setText(user + "\n" + message);
        container.addView(messageTV);

        TextView reference = (TextView) findViewById(999 + UtilityClass.currentId);
        LinearLayout.LayoutParams messageTextViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        messageTextViewParams.setMargins(UtilityClass.pixelsFromDps(5), UtilityClass.pixelsFromDps(3), UtilityClass.pixelsFromDps(5), UtilityClass.pixelsFromDps(2));
        reference.setLayoutParams(messageTextViewParams);
        reference.setBackgroundResource(R.drawable.dark_square);
        messageTV.setPadding(UtilityClass.pixelsFromDps(5), UtilityClass.pixelsFromDps(2), UtilityClass.pixelsFromDps(5), UtilityClass.pixelsFromDps(5));
        //b1.setTextAppearance(UtilityClass.cntxt, R.style.BlackText);
        */
        ScrollView sv = (ScrollView) findViewById(R.id.conversation_scroll_view);
        sv.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if(!hasFocus)
        {
            t.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_public_chatroom_page, menu);
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
