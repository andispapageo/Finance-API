package com.example.user.bdswiss_test.feature;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONObject;
import org.json.JSONArray;

public class RestfulApiClient {

    private final String urlString = "https://mt4-api-staging.herokuapp.com/rates";
    public static class HttpGetRequest extends AsyncTask<String, Void, String>
    {
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        String inputLine;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            URL myUrl = null;
            try {
                myUrl = new URL(stringUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection  = null;
            try {
                connection = (HttpURLConnection)myUrl.openConnection();

                connection.setRequestMethod("GET");
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();

                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();

                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return null;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }
    public String load() throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(6000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream());

        InputStream istream = conn.getInputStream();
        String result = null;
        return result;
    }
}
