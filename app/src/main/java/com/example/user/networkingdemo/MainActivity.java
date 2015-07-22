package com.example.user.networkingdemo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import java.io.*;
import java.net.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import android.widget.Toast;


public class MainActivity extends Activity {

    private ImageView imageView;
    private static final String IMAGE_URL = "http://www.google.com/images/srpr/logo11w.pmg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView1);
    }

    public void startDownload(View v) {
        if (isNetworkAvailable()) {
            new DownloadTask().execute(IMAGE_URL);
        } else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();

        }
    }

    public void downloadText(View v) {
        if (isNetworkAvailable()) {
            new ReadStreamTask().execute("http://www.i-ducate.com");
        } else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable()) {
            available = true;
        }
        return available;
    }

    private String readStream(String urlStr) throws IOException {
        String str = "";
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while ((line = reader.readLine()) != null) {
                str += line;
            }
        } catch (Exception e) {
            Log.d("NetworkingDemo", e.toString());
        } finally {
            inputStream.close();
            reader.close();
        }
        return str;
    }

    private class ReadStreamTask extends AsyncTask<String, Void, String> {

        String str = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                str = readStream(params[0]);
            } catch (Exception e) {
                Log.d("NetworkingDemo", e.toString());
            }
            return str;

        }

        @Override
        public void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }


    }


    public Bitmap downloadImage(String urlStr) throws IOException {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.d("NetworkingDemo", e.toString());

        } finally {
            inputStream.close();
        }
        return bitmap;
    }


    public class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                bitmap = downloadImage(params[0]);
            } catch (Exception e) {
                Log.d("NetworkingDemo", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}

