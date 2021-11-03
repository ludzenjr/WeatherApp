package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.weatherapp.OUTPUT";
    EditText etCity;
    TextView tvResult;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";

    private final String appid = "329c2af16fc9ae3894d9c5961fade5f6";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        Intent intent = new Intent(this, WeatherDisplay.class);
        String tempUrl = "";
        String city_name = etCity.getText().toString().trim();
        boolean zip = TextUtils.isDigitsOnly(city_name);
        if(city_name.equals("")){
            tvResult.setText("Please enter a city or zip code");
        }else{
            if(zip == true){
                tempUrl = url + "?zip=" + city_name +"&appid=" + appid;
                intent.putExtra("url",tempUrl);
                startActivity(intent);
            }else{
                tempUrl = url + "?q=" + city_name + "&appid=" + appid;
                intent.putExtra("url",tempUrl);
                startActivity(intent);
            }

        }

    }



}