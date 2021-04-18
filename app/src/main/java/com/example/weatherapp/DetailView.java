package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        TextView txtdate=(TextView)findViewById(R.id.date);
        TextView txtcity=(TextView)findViewById(R.id.city);
        ImageView imageView=(ImageView)findViewById(R.id.icon);
        TextView txtTemp=(TextView)findViewById(R.id.temp);
        TextView txtDesc=(TextView)findViewById(R.id.description);
        TextView txtHumidity=(TextView)findViewById(R.id.humidity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String date = extras.getString("date");
            String city = extras.getString("city");
            Integer icon = extras.getInt("icon");
            String temp = extras.getString("temp");
            String desc = extras.getString("desc");
            String humidity = extras.getString("humidity");

            txtdate.setText(date);
            txtcity.setText(city);
            imageView.setImageResource(icon);
            txtTemp.setText(temp);
            txtDesc.setText(desc);
            txtHumidity.setText("Humidity : "+humidity+" %");
        }
    }

}