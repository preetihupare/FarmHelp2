package com.ynot.farmhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Harvest extends AppCompatActivity {

    Spinner selectCrop;
    TextView cropName, monthDemand, nextMonthDemand, remainingDemand, enterData;
    EditText harvestDate, expectedDate;
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
        enterData = findViewById(R.id.enterData);

        selectCrop = (Spinner) findViewById(R.id.selectCrop);
        // Initializing a String Array
        String[] listCrop = new String[]{
                "Select Crop",
                "Tomato",
                "Onion",
                "Potato"
        };

        final List<String> cropList = new ArrayList<>(Arrays.asList(listCrop));

            // Initializing an ArrayAdapter
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,listCrop){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
            selectCrop.setAdapter(spinnerArrayAdapter);

            selectCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);
                    // If user change the default selection
                    // First item is disable and it is used for hint

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

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