package com.quantumtopia.krauser.chessonline;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Vector;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UtilityClass.publicRoomMessages = new Vector<MessageClass>();
        UtilityClass.cntxt = this.getApplicationContext();
        UtilityClass.readRegistrationInformation();
    }

    public void openGamePage(View v) { startActivity(new Intent(this, GamePage.class)); }
    public void openSettingsPage(View v)
    {
        startActivity(new Intent(this, SettingsPage.class));
    }
    public void register() { startActivity(new Intent(this, RegistrationPage.class)); }
    public void openPublicChatRoomPage(View v) { startActivity(new Intent(this, PublicChatroomPage.class)); }

    public void deregister(View v)
    {
        UtilityClass.deleteUserFile();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus)
        {
            if(!UtilityClass.registered)
                register();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        UtilityClass.readRegistrationInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
