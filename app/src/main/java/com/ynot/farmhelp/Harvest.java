package com.ynot.farmhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class Harvest extends AppCompatActivity {

    TextView harvestDate, expectedDate, cropName, monthDemand, nextMonthDemand, remainingDemand;
    ImageView calendarImage, calendarImage2, tomato, onion, potato, cropImage;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest);

        harvestDate = findViewById(R.id.harvestDate);
        expectedDate = findViewById(R.id.expectedDate);
        calendarImage = findViewById(R.id.calendarImage);
        calendarImage2 = findViewById(R.id.calendarImage2);
        tomato = findViewById(R.id.tomato);
        onion = findViewById(R.id.onion);
        potato = findViewById(R.id.potato);
        cropImage = findViewById(R.id.cropImage);
        cropName = findViewById(R.id.cropName);
        monthDemand = findViewById(R.id.monthDemand);
        nextMonthDemand = findViewById(R.id.nextMonthDemand);
        remainingDemand = findViewById(R.id.remainingDemand);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        int images[] = {R.drawable.tomato, R.drawable.onion, R.drawable.potato};


        harvestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Harvest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        harvestDate.setText(date);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Harvest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        harvestDate.setText(date);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        expectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Harvest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        expectedDate.setText(date);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        calendarImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Harvest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        expectedDate.setText(date);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        tomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cropImage.setImageResource(images[0]);
                cropName.setText("Tomato");
                monthDemand.setText("50891/Qtls");
                nextMonthDemand.setText("42561/Qtls");
                remainingDemand.setText("8330/Qtls");
            }
        });

        onion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cropImage.setImageResource(images[1]);
                cropName.setText("Onion");
                monthDemand.setText("105754/Qtls");
                nextMonthDemand.setText("55780/Qtls");
                remainingDemand.setText("12543/Qtls");
            }
        });

        potato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cropImage.setImageResource(images[2]);
                cropName.setText("Potato");
                monthDemand.setText("99540/Qtls");
                nextMonthDemand.setText("48560/Qtls");
                remainingDemand.setText("10642/Qtls");
            }
        });
    }

}