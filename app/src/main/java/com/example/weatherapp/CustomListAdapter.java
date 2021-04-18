package com.example.weatherapp;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] days;
    private final String[] day_weather_status;
    private final String[] day_temp;
    private final Integer[] imgID;

    public CustomListAdapter(Activity context, String[] days, String[] status, String[] day_temp, Integer[] imgID){
        super(context,R.layout.day_list,days);
        this.context=context;
        this.days=days;
        this.day_weather_status=status;
        this.day_temp=day_temp;
        this.imgID=imgID;
    }
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.day_list,null,true);
        TextView txtDayName=(TextView)rowView.findViewById(R.id.day_name);
        TextView txtWeatherStatus=(TextView)rowView.findViewById(R.id.weather_status);
        TextView txtTemp=(TextView)rowView.findViewById(R.id.temp);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.list_image);

        txtDayName.setText(days[position]);
        txtWeatherStatus.setText(day_weather_status[position]);
        txtTemp.setText(day_temp[position]);
        imageView.setImageResource(imgID[position]);
        return rowView;
    }
}