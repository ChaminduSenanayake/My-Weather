package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Settings extends AppCompatActivity  {
    public static String city_name;
    public static String temp_scale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LinearLayout city_name=(LinearLayout)findViewById(R.id.city_name);
        city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCityNameDialog();
            }
        });

        LinearLayout temp_scale=(LinearLayout)findViewById(R.id.temperature_scale);
        temp_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTempScaleDialog();
            }
        });
    }

    private void openTempScaleDialog() {
        TempScaleDialog tsd=new TempScaleDialog();
        tsd.show(getSupportFragmentManager(),"Temperature Unit");
    }

    private void openCityNameDialog() {
        CityNameDialog cnd=new CityNameDialog();
        cnd.show(getSupportFragmentManager(),"City Name");

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}