package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FetchData fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.city_name="Colombo";//set default city name
        Settings.temp_scale="metric";//set default Temp unit scale
        fd=new FetchData();
        fd.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this,Settings.class));break;
            case R.id.about:
                startActivity(new Intent(this,About.class));break;
        }
        return true;
    }

    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you need to exit ?");
        builder.setTitle(R.string.alert_title);
        builder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        fd.cancel(true); // clear FetchData object
        new FetchData().execute(); // create new Fetch data object

    }

    public class FetchData extends AsyncTask<String,Void,String>{
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = ""; // Create string for store json object from openweathermap
        String city=Settings.city_name; //Get static city name from Settings.java
        String units=Settings.temp_scale;//Get static city name from Settings.java
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            final String[] days=new String[7];
            final String[] temp_list=new String[7];
            final String[] status_list=new String[7];
            final Integer[] icon_list=new Integer[7];
            final String[] date_list=new String[7];
            final String[] desc_list=new String[7];
            final String[] humidity_list=new String[7];
            try {
                    JSONObject weather_data = new JSONObject(forecastJsonStr); // Get json object in forecastJsonStr
                    JSONArray daily_arr = weather_data.getJSONArray("daily"); //Get json array from json object

                    for (int i=0;i<7;i++){
                        JSONObject weather_obj = daily_arr.getJSONObject(i);
                        String dt=weather_obj.getString("dt");

                        //add humidity to the array
                        String humidity=weather_obj.getString("humidity");
                        humidity_list[i]=humidity;

                        //convert dt into weekday
                        long unix_time_int=Long.parseLong(dt);
                        Date uxDate=new Date(unix_time_int*1000);
                        SimpleDateFormat sdf= new SimpleDateFormat("EEEE");
                        String weekday = sdf.format(uxDate);

                        if(i==0){weekday=weekday+"(Today)";}
                        days[i]=weekday;

                        //Get Date
                        SimpleDateFormat date_Format= new SimpleDateFormat("yyyy-MM-dd");
                        String date = date_Format.format(uxDate);
                        date_list[i]=date;

                        //Get temp from json object
                        JSONObject temp_obj = weather_obj.getJSONObject("temp");
                        String temp=temp_obj.getString("day");
                        if(units.equals("metric")){
                            temp_list[i]=temp+"\u2103";
                        }else if (units.equals("imperial")){
                            temp_list[i]=temp+"\u2109";
                        }


                        //Get weather from json object
                        JSONArray weather_arr = weather_obj.getJSONArray("weather");
                        JSONObject weather_detail_obj = weather_arr.getJSONObject(0);
                        String weather_status=weather_detail_obj.getString("main");
                        status_list[i]=weather_status;

                        //Get weather description from json object
                        String weather_desc=weather_detail_obj.getString("description");
                        desc_list[i]=weather_desc;

                        //Get weather icon

                        String weather_icon=weather_detail_obj.getString("icon");
                        switch (weather_icon){
                            case "01d":icon_list[i] = R.drawable.clear_sky; break;
                            case "02d":icon_list[i]=R.drawable.few_clouds;break;
                            case "03d":icon_list[i]=R.drawable.scatterd_clouds;break;
                            case "04d":icon_list[i]=R.drawable.broken_clouds;break;
                            case "09d":icon_list[i]=R.drawable.shower_rain ;break;
                            case "10d":icon_list[i]=R.drawable.rain;break;
                            case "11d":icon_list[i]=R.drawable.thunderstorm;break;
                            case "13d":icon_list[i]=R.drawable.snow;break;
                            case "50d":icon_list[i] = R.drawable.mist;break;
                            default:icon_list[i] = R.drawable.clear_sky;
                        }
                    }

                //use custom list adapter to create a list
                CustomListAdapter adapter=new CustomListAdapter(MainActivity.this,days,status_list,temp_list,icon_list);
                ListView listView=(ListView)findViewById(R.id.list_view);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedDate=date_list[+position];
                        String selectedTemp=temp_list[+position];
                        Integer selectedIcon=icon_list[+position];
                        String selectedDesc=desc_list[+position];
                        String selectedHumidity=humidity_list[+position];

                        Intent i = new Intent(MainActivity.this, DetailView.class);
                        i.putExtra("date",selectedDate);
                        i.putExtra("city",city);
                        i.putExtra("icon",selectedIcon);
                        i.putExtra("temp",selectedTemp);
                        i.putExtra("desc",selectedDesc);
                        i.putExtra("humidity",selectedHumidity);
                        startActivity(i);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Convert city name into latitude and longitude
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> address = coder.getFromLocationName(city,5);
                List<Double> add_list = new ArrayList<>(address.size());
                for (Address a:address){
                    if(a.hasLatitude()&&a.hasLongitude()){
                        add_list.add(a.getLatitude());
                        add_list.add(a.getLongitude());
                    }
                }

                double lat=add_list.get(0);
                double lon=add_list.get(1);

                final String BASE_URL ="https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=current,hourly,minutely,alerts&units="+units+"&appid=3277ece3810b81f097e5e6e949734fc9";
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) { return null; }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line1;

                while ((line1 = reader.readLine()) != null) { buffer.append(line1 + "\n"); }
                if (buffer.length() == 0) { return null; }
                forecastJsonStr =buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally{
                if (urlConnection != null) { urlConnection.disconnect(); }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

    }

}