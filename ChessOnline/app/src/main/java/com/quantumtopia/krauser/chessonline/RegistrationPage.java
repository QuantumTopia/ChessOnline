package com.quantumtopia.krauser.chessonline;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Handler;


public class RegistrationPage extends Activity
{
    private Handler h;
    private TransparentProgressDialog pd;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        h = new Handler();
        pd = new TransparentProgressDialog(RegistrationPage.this, R.drawable.spinner);
        /*r = new Runnable()
        {
            @Override
            public void run()
            {
                if (pd.isShowing())
                {
                    pd.dismiss();
                }
            }
        };*/
    }

    public void register(View v)
    {
        EditText et = (EditText) findViewById(R.id.user_name_edit_text);
        String userName = et.getText().toString();

        ServerCommunication.receivedFromServer = false;
        ServerCommunication sc = new ServerCommunication("register.php?" + userName);
        sc.execute();

        pd.show();
        h.postDelayed(r,10000);
        System.out.println("Spinner showing");

        while(!ServerCommunication.receivedFromServer)
        {
        }

        String response = "";
        try{ response = ServerCommunication.posts.getString("registration"); } catch (JSONException e) {e.printStackTrace(); }

        if(response.equals("true"))
        {
            UtilityClass.writeRegistrationInformation(userName);
            startActivity(new Intent(this, MainActivity.class));
            ServerCommunication.receivedFromServer = false;
        }

        h.removeCallbacks(r);
        if (pd.isShowing() )
        {
            System.out.println("Spinner removed");
            pd.dismiss();
        }

        /*
        String sizeOfArray = ServerCommunication.posts.getString("size");
        int size = Integer.parseInt(sizeOfArray);

        for(int i=0; i<s; i++)
        {
            String element = Integer.toString(i);
            String str = posts.getString(element);
            String[] parts = str.split(";", 2);
            System.out.println("Received from server: " + str);
            UtilityClass.addMessage(parts[0], parts[1], true, false);
        }
        JSONObject obj = new JSONObject("{interests : [{interestKey:Dogs}, {interestKey:Cats}]}");

        List<String> list = new ArrayList<String>();
        JSONArray array = obj.getJSONArray("interests");
        for(int i = 0 ; i < array.length() ; i++){
            list.add(array.getJSONObject(i).getString("interestKey"));

        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_page, menu);
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
