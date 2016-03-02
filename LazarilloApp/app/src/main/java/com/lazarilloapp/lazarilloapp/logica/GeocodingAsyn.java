package com.lazarilloapp.lazarilloapp.logica;

import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by santiago on 22/01/16.
 */
public class GeocodingAsyn extends AsyncTask<String, String, String> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            String ubicacion = URLEncoder.encode(params[0], "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
