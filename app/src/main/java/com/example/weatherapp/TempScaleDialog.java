package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TempScaleDialog extends AppCompatDialogFragment {

    private RadioGroup radioGroupScale;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.temp_scale_dialog,null);

        radioGroupScale=view.findViewById(R.id.radioGroupScale);
        if (Settings.temp_scale.equals("metric")){
            radioGroupScale.check(R.id.radioBtnCelsius);
        }else if (Settings.temp_scale.equals("imperial")){
            radioGroupScale.check(R.id.radioBtnFahrenheit);
        }


        builder.setView(view).setTitle("Temperature Unit").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        radioGroupScale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=radioGroupScale.getCheckedRadioButtonId();
                if (id==R.id.radioBtnCelsius){
                    Settings.temp_scale="metric";
                }else if (id==R.id.radioBtnFahrenheit){
                    Settings.temp_scale="imperial";
                }
            }
        });
        return builder.create();
    }
}