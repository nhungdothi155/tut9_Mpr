package com.example.tut9_mpr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView ;
    private TextView name;
    private TextView email;
    private Button first;
    private Button last;
    private static int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.name);




        email = findViewById(R.id.email);
        new DowloadTask().execute("https://jsonplaceholder.typicode.com/users/" + i);
        new ImageDownload().execute("https://robohash.org/" + i + "?set=set2");
        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        last.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i  = i+1;
                        new DowloadTask().execute("https://jsonplaceholder.typicode.com/users/" + i);
                        new ImageDownload().execute("https://robohash.org/" + i + "?set=set2");

                    }
                }
        );
        first.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i  = i-1;
                        new DowloadTask().execute("https://jsonplaceholder.typicode.com/users/" + i);
                        new ImageDownload().execute("https://robohash.org/" + i + "?set=set2");

                    }
                }
        );


    }
    private class DowloadTask extends AsyncTask<String,Void, String>{


        @Override
        protected String doInBackground(String... strings) {
        URL url ;
        HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                Scanner sc = new Scanner(urlConnection.getInputStream());
                StringBuilder result = new StringBuilder();
                String line;
                while(sc.hasNextLine()) {
                    line = sc.nextLine();
                    result.append(line);
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject root = new JSONObject(s);
                String nameV = root.getString("name");
                String emailV = root.getString("email");
                name.setText(nameV);
                email.setText(emailV);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    private class ImageDownload extends  AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }









}