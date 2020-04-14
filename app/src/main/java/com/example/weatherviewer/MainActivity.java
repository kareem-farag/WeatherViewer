package com.example.weatherviewer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    DownloadTask task;
    TextView weatherData;
    TextView latText ;
    TextView lonText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //http://muziworld.com/mobile/learn/language-types
        weatherData = findViewById(R.id.weatherData);
        latText = findViewById(R.id.lat);
        lonText = findViewById(R.id.lon);

        task = new DownloadTask();



        //Toast.makeText(getApplicationContext(),weather,Toast.LENGTH_LONG).show();
    }

    protected void getWeather (View view)  {


        task.execute("https://api.openweathermap.org/data/2.5/weather?lat=" + latText.getText().toString() + "&lon=" + latText.getText().toString()+ "&appid=a60d7940ab0f65ddcbe341bba0950e8b");

    };
    public class DownloadTask extends AsyncTask<String ,Void,String>{

        HttpURLConnection connection = null ;
        @Override
        protected String doInBackground(String... urls) {
            String output = "";
            try {
                // get the url as a vaild url to be used
                URL url = new URL (urls[0]) ;
                // open connection from the url open connection
                connection =(HttpURLConnection) url.openConnection();
                // get input stream from the connection
                InputStream is = connection.getInputStream();
                // get the InputStreamReader class to read the json data
                InputStreamReader reader = new InputStreamReader(is);
                int data = reader.read();

                while (data!=-1) {
                    char current = (char) data ;
                    output += current;
                    data = reader.read();
                }
                return output;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("malformedurl" ,e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("IOException" ,e.getMessage());
            }


            return output;
        };
        protected void onPostExecute (String result){
            super.onPostExecute(result);
            JSONObject jsonObject = null ;
            JSONArray jsonArray = null ;
            try {

                jsonObject = new JSONObject(result);
                jsonArray = new JSONArray(jsonObject.getString("weather") );
                String weather = "" ;
                for (int i=0 ; i<jsonArray.length();i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    weather += jObject.getString("description");

                }
                weatherData.setText(weather);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
