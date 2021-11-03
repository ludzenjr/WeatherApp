package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherDisplay extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("#.##");
    public String url;
    TextView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);

        //Get the Intent from activity_main.xml
        Intent intent = getIntent();
        String output = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent intent1 = getIntent();
        url = intent1.getStringExtra("url");


        //Get the display area
        TextView textView = findViewById(R.id.Result);
        textView.setTextColor(Color.rgb(68, 134, 199));
        textView.setText(output);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jsonresponse = new JSONObject(response);
                    JSONArray jsonArray = jsonresponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonresponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double temp_min = jsonObjectMain.getDouble("temp_min")-273.15;
                    double temp_max = jsonObjectMain.getDouble("temp_max")-273.15;
                    double feelslike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonresponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonresponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonresponse.getJSONObject("sys");
                    String countryname = jsonObjectSys.getString("country");
                    String sunriseT = jsonObjectSys.getString("sunrise");
                    String sunsetT = jsonObjectSys.getString("sunset");
                    String cityName = jsonresponse.getString("name");

                    String sunrise = unix(sunriseT);
                    String sunset = unix(sunsetT);

                    results = findViewById(R.id.Result);
                    results.setTextColor(Color.rgb(68, 134, 199));

                    output += "Current weather of " + cityName + " (" + countryname + ")"
                            + "\n Temp: " + df.format(temp) + " °C"
                            + "\n Temp_min: " + df.format(temp_min) + " °C"
                            + "\n Temp_max: " + df.format(temp_max) +" °C"
                            + "\n Feels Like: " + df.format(feelslike) + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Wind Speed: " + wind + "m/s"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa"
                            + "\n Sunrise: " + sunrise
                            + "\n Sunset: " + sunset
                            + "\n Description: " + description;

                    results.setText(output);

                    //map_intent to pass longitude and latitude
                    JSONObject coord = new JSONObject(jsonresponse.getString("coord"));
                    String lon, lat;
                    lon = coord.getString("lon");
                    lat = coord.getString("lat");

                    //map_btn onClick Listener
                    Button map_button = (Button) findViewById(R.id.map_btn);
                    map_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent map_intent = new Intent(WeatherDisplay.this, MapsActivity.class);
                            map_intent.putExtra("latitude", lat);
                            map_intent.putExtra("longitude", lon);
                            map_intent.putExtra("city", cityName);
                            startActivity(map_intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public String unix(String value){
        long unixSeconds = Long.parseLong(value);

        Date date = new java.util.Date(unixSeconds*1000L);

        SimpleDateFormat sdf = new java.text.SimpleDateFormat(" HH:mm:ss ");

        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        return formattedDate;

    }
}