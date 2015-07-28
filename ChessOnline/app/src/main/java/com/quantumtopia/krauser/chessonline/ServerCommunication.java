package com.quantumtopia.krauser.chessonline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;

public class ServerCommunication extends AsyncTask<String, Void, String>
{
    private String url = "http://qtonlinechess.appspot.com/";
    public static JSONObject posts;
    public static boolean receivedFromServer;

    public ServerCommunication(String query)
    {
        url += query;
    }

    @Override
    protected String doInBackground(String... params)
    {
        System.out.println("Request: " + url);
        url = url.replaceAll(" ", "%20");
        Decode(downloadFile(url));

        return null;
    }

    private void Decode(CharSequence result)
    {
        try
        {
            posts = new JSONObject(result.toString()).getJSONObject("posts");
            receivedFromServer = true;
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected String downloadFile(String url)
    {
        // to fill-in url content
        StringBuilder builder = new StringBuilder();

        // local objects declarations
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                // Failed to download file

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
