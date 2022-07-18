package com.ynot.farmhelp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.text.format.DateFormat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class Harvest extends AppCompatActivity {

    Spinner selectCrop;
    TextView cropName, monthDemandStart, nextMonthDemandStart, remainingDemand, enterData, monthDemandEnd, nextMonthDemandEnd, counterText, mdm, mdnm, rhnm;
    EditText harvestDate, plantingDate;
    ImageView calendarImage, calendarImage2, tomato, onion, potato, cropImage;
    DatePickerDialog.OnDateSetListener setListener;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    StorageReference storageReference;

    EditText Area;
    String AreaAcre, PlantDateString, SelectedCropName, timedata, day1;
    Button submit;
    int cropNumber,counter;
    int StartDayOfMonth;

    TextView firstDayofMonth, newdate;
    String StartDay;
    int Day1OfMonth;
    LinearLayout plantingDateLayout;
    private int Hour1ofDay;
    private int Minum1oHour;
    private int AM_PM_1ofDay;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

//        harvestDate = findViewById(R.id.harvestDate);
        plantingDate = findViewById(R.id.plantingDate);
//        calendarImage = findViewById(R.id.calendarImage);
        calendarImage2 = findViewById(R.id.calendarImage2);
        tomato = findViewById(R.id.tomato);
        onion = findViewById(R.id.onion);
        potato = findViewById(R.id.potato);
        cropImage = findViewById(R.id.cropImage);
        cropName = findViewById(R.id.cropName);
        monthDemandStart = findViewById(R.id.monthDemandStart);
        nextMonthDemandStart = findViewById(R.id.nextMonthDemandStart);
        monthDemandEnd = findViewById(R.id.monthDemandEnd);
        nextMonthDemandEnd = findViewById(R.id.nextMonthDemandEnd);
        remainingDemand = findViewById(R.id.remainingDemand);
        enterData = findViewById(R.id.enterData);
        selectCrop = (Spinner) findViewById(R.id.selectCrop);

        Area = findViewById(R.id.HarvestArea);
        AreaAcre = Area.getText().toString();
        submit = findViewById(R.id.submitHarvest);

        newdate = findViewById(R.id.newdate);
        rhnm = findViewById(R.id.rhnm);
        mdnm = findViewById(R.id.mdnm);
        mdm = findViewById(R.id.mdm);

        counterText = findViewById(R.id.counterData);

//        firstDayofMonth = findViewById(R.id.lastDayofMonth);

        plantingDateLayout = findViewById(R.id.plantingDateLayout);
        //images array
        int images[] = {R.drawable.tomato, R.drawable.onion, R.drawable.potato};

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

                // editText on click code
                if ( position == 1 ) {
                    cropImage.setImageResource(images[0]);
                    cropName.setText("Tomato");
//                    mdm.setText("");
                    mdnm.setText("Market Demand After 2 Month*");
                    rhnm.setText("Remaining Harvest For Next 2 Month*");
                    SelectedCropName = "tomato";
                    fetchTomatoData();
                    cropNumber = 1;
                }
                else if ( position == 2 ){
                    cropImage.setImageResource(images[1]);
                    cropName.setText("Onion");
                    mdnm.setText("Market Demand After 3 Month*");
                    rhnm.setText("Remaining Harvest For Next 3 Month*");
                    SelectedCropName = "onion";
                    fetchOnionData();
                    cropNumber = 2;
                }
                else if ( position == 3 ) {
                    cropImage.setImageResource(images[2]);
                    cropName.setText("Potato");
                    //mdm.setText("");
                    mdnm.setText("Market Demand After 3 Month*");
                    rhnm.setText("Remaining Harvest For Next 3 Month*");
                    SelectedCropName = "potato";
                    fetchPotatoData();
                    cropNumber = 3;
                }
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

        DocumentReference documentReference = fStore.collection("harvest/").document("tomato");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    monthDemandStart.setText(documentSnapshot.getString("thisMonth"));
                    monthDemandEnd.setText(documentSnapshot.getString("thisMonthEnd"));
                    nextMonthDemandStart.setText(documentSnapshot.getString("nextMonth"));
                    nextMonthDemandEnd.setText(documentSnapshot.getString("nextMonthEnd"));
                    remainingDemand.setText(documentSnapshot.getString("remaining"));
                    counterText.setText(documentSnapshot.getString("counter"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

//        harvestDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(Harvest.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        month = month + 1;
//                        String date = day + "/" + month + "/" + year;
//                        harvestDate.setText(date);
//                    }
//                }, year, month, day);
//                datePickerDialog.show();
//            }
//        });

        //Last Day Of Month
        StartDayOfMonth = Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH);
        StartDay = String.valueOf(StartDayOfMonth);


        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        SimpleDateFormat output1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String localT = String.valueOf(localTime);
        String localD = String.valueOf(localDate);
        try {
            Date t = input1.parse(localD + " " + localT);
            String cday = (String) DateFormat.format("dd", t);
            newdate.setText(DateFormat.format("dd" + " " + "hh:mm", t));
            timedata = newdate.getText().toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day1 = timedata;

        plantingDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Harvest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        plantingDate.setText(date);
                        PlantDateString = date;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        plantingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Harvest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        plantingDate.setText(date);
                        PlantDateString = date;
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
                        plantingDate.setText(date);
                        PlantDateString = date;
                    }
                }, year, month, day);
                Calendar cal=Calendar.getInstance();
                cal.add(Calendar.MONTH,1);
                long afterTwoMonthsinMilli=cal.getTimeInMillis();
                datePickerDialog.getDatePicker().setMaxDate(afterTwoMonthsinMilli);
                datePickerDialog.show();
            }
        });

        tomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage.setImageResource(images[0]);
                cropName.setText("Tomato");
                mdnm.setText("Market Demand After 2 Month*");
                rhnm.setText("Remaining Harvest For Next 2 Month*");
                SelectedCropName="tomato";
                fetchTomatoData();
            }
        });

        onion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage.setImageResource(images[1]);
                cropName.setText("Onion");
                mdnm.setText("Market Demand After 3 Month*");
                rhnm.setText("Remaining Harvest For Next 3 Month*");
                SelectedCropName="onion";
                fetchOnionData();
            }
        });

        potato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage.setImageResource(images[2]);
                cropName.setText("Potato");
                mdnm.setText("Market Demand After 3 Month*");
                rhnm.setText("Remaining Harvest For Next 3 Month*");
                SelectedCropName="potato";
                fetchPotatoData();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SelectedCropName=="tomato")
                {submitTomatoHData();}
                else if (SelectedCropName=="onion")
                {submitOnionHData();}
                else if (SelectedCropName=="potato")
                {submitPotatoHData();}
            }
        });
    }

    private void submitPotatoHData() {
        AreaAcre = Area.getText().toString();
        double FinalData;
        double OldData;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);

//        Time today = new Time(Time.getCurrentTimezone());
//        today.setToNow();
////        if(!today.isEmpty())
//        newdate.setText(today.monthDay + "%k:%M:%S");

        if(!AreaAcre.isEmpty()) {
            if (StartDayOfMonth == day) {
                if(day1.equals("01 00:00"))
                    counter = 0;
                {
                    if (counter == 0) {
                        double areaData = Double.parseDouble(Area.getText().toString());
                        OldData = Double.parseDouble(remainingDemand.getText().toString());
                        double AD;
                        AD = areaData * 10;
                        if (OldData - AD >= 0) {
                            FinalData = OldData - AD;
                            AreaAcre = String.valueOf(FinalData);
                            Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                        } else {
                            AreaAcre = "0";
                            Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                        }

                        fStore.collection("harvest").document("potato").update("remaining", nextMonthDemandEnd.getText().toString(), "previousMonth", AreaAcre)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        counter++;
                                    }
                                });

                    } else {
                        double areaData = Double.parseDouble(Area.getText().toString());
                        OldData = Double.parseDouble(remainingDemand.getText().toString());
                        double AD;
                        AD = areaData * 10;
                        if (OldData - AD >= 0) {
                            FinalData = OldData - AD;
                            AreaAcre = String.valueOf(FinalData);
                            Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                        } else {
                            AreaAcre = "0";
                            Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                        }

                        fStore.collection("harvest").document("potato").update("remaining", AreaAcre)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                });
                    }
                }
            }
            else
            {
                double areaData = Double.parseDouble(Area.getText().toString());
                OldData = Double.parseDouble(remainingDemand.getText().toString());
                double AD;
                AD = areaData * 10;
                if(OldData - AD >=0)
                {
                    FinalData = OldData - AD;
                    AreaAcre = String.valueOf(FinalData);
                    Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                }

                else {
                    AreaAcre = "0";
                    Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                }
                fStore.collection("harvest").document("potato").update("remaining", AreaAcre)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
            }
        }
        else {
            Toast.makeText(Harvest.this, "Harvest Area is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitOnionHData() {
        AreaAcre = Area.getText().toString();
        double FinalData;
        double OldData;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(!AreaAcre.isEmpty()) {
            if (StartDayOfMonth == day) {
                if(counter == 0){
                    double areaData = Double.parseDouble(Area.getText().toString());
                    OldData = Double.parseDouble(remainingDemand.getText().toString());
                    double AD;
                    AD = areaData * 10;
                    if(OldData - AD >=0)
                    {
                        FinalData = OldData - AD;
                        AreaAcre = String.valueOf(FinalData);
                        Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AreaAcre = "0";
                        Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                    }

                    fStore.collection("harvest").document("onion").update("remaining", nextMonthDemandEnd.getText().toString(), "previousMonth", AreaAcre)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    counter++;
                                }
                            });
                }else
                {
                    double areaData = Double.parseDouble(Area.getText().toString());
                    OldData = Double.parseDouble(remainingDemand.getText().toString());
                    double AD;
                    AD = areaData * 10;
                    if(OldData - AD >=0)
                    {
                        FinalData = OldData - AD;
                        AreaAcre = String.valueOf(FinalData);
                        Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AreaAcre = "0";
                        Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                    }

                    fStore.collection("harvest").document("onion").update("remaining", AreaAcre)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });
                }
            }
            else
            {
                double areaData = Double.parseDouble(Area.getText().toString());
                OldData = Double.parseDouble(remainingDemand.getText().toString());
                double AD;
                AD = areaData * 10;
                if(OldData - AD >=0)
                {
                    FinalData = OldData - AD;
                    AreaAcre = String.valueOf(FinalData);
                    Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                }
                else {
                    AreaAcre = "0";
                    Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                }
                fStore.collection("harvest").document("onion").update("remaining", AreaAcre)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
            }
        }
        else {
            Toast.makeText(Harvest.this, "Harvest Area is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitTomatoHData() {
        AreaAcre = Area.getText().toString();
        double FinalData;
        double OldData;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(!AreaAcre.isEmpty()) {
            if (StartDayOfMonth == day) {
                if(counter == 0){
                double areaData = Double.parseDouble(Area.getText().toString());
                OldData = Double.parseDouble(remainingDemand.getText().toString());
                    double AD;
                    AD = areaData * 10;
                    if(OldData - AD >=0)
                    {
                        FinalData = OldData - AD;
                        AreaAcre = String.valueOf(FinalData);
                        Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                    }
                else {
                    AreaAcre = "0";
                    Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                }

                fStore.collection("harvest").document("tomato").update("remaining", nextMonthDemandEnd.getText().toString(), "previousMonth", AreaAcre)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                counter++;
                            }
                        });
                }else
                {
                    double areaData = Double.parseDouble(Area.getText().toString());
                    OldData = Double.parseDouble(remainingDemand.getText().toString());
                    double AD;
                    AD = areaData * 10;
                    if(OldData - AD >=0)
                    {
                        FinalData = OldData - AD;
                        AreaAcre = String.valueOf(FinalData);
                        Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AreaAcre = "0";
                        Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                    }

                    fStore.collection("harvest").document("tomato").update("remaining", AreaAcre)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });
                }
            }
            else
            {
                double areaData = Double.parseDouble(Area.getText().toString());
                OldData = Double.parseDouble(remainingDemand.getText().toString());
                double AD;
                AD = areaData * 10;
                if(OldData - AD >=0)
                {
                    FinalData = OldData - AD;
                    AreaAcre = String.valueOf(FinalData);
                    Toast.makeText(Harvest.this, "Data Added", Toast.LENGTH_SHORT).show();
                }
                else {
                    AreaAcre = "0";
                    Toast.makeText(Harvest.this, "Sorry, Remaining Harvest Reached to Zero", Toast.LENGTH_SHORT).show();
                }
                fStore.collection("harvest").document("tomato").update("remaining", AreaAcre)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
            }
        }
        else {
            Toast.makeText(Harvest.this, "Harvest Area is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchTomatoData() {
        DocumentReference documentReference = fStore.collection("harvest/").document("tomato");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    monthDemandStart.setText(documentSnapshot.getString("thisMonth"));
                    monthDemandEnd.setText(documentSnapshot.getString("thisMonthEnd"));
                    nextMonthDemandStart.setText(documentSnapshot.getString("nextMonth"));
                    nextMonthDemandEnd.setText(documentSnapshot.getString("nextMonthEnd"));
                    remainingDemand.setText(documentSnapshot.getString("remaining"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
    }

    private void fetchOnionData() {
        DocumentReference documentReference = fStore.collection("harvest/").document("onion");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    monthDemandStart.setText(documentSnapshot.getString("thisMonth"));
                    monthDemandEnd.setText(documentSnapshot.getString("thisMonthEnd"));
                    nextMonthDemandStart.setText(documentSnapshot.getString("nextMonth"));
                    nextMonthDemandEnd.setText(documentSnapshot.getString("nextMonthEnd"));
                    remainingDemand.setText(documentSnapshot.getString("remaining"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
    }

    private void fetchPotatoData() {
        DocumentReference documentReference = fStore.collection("harvest/").document("potato");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    monthDemandStart.setText(documentSnapshot.getString("thisMonth"));
                    monthDemandEnd.setText(documentSnapshot.getString("thisMonthEnd"));
                    nextMonthDemandStart.setText(documentSnapshot.getString("nextMonth"));
                    nextMonthDemandEnd.setText(documentSnapshot.getString("nextMonthEnd"));
                    remainingDemand.setText(documentSnapshot.getString("remaining"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
    }
}