package com.ynot.farmhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    Spinner select_soil, select_crop, select_market, select_city;
    String selected_crop, selected_market,selected_city,selected_soil;

    EditText mFullName, mEmail, mPassword, mPhone, mFarmArea, mFarmLocation;
    Button mRegisterBtn, mPWD;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.email);
        mPhone      = findViewById(R.id.phone);
        mPassword   = findViewById(R.id.pwd);
        mFarmArea   = findViewById(R.id.area);
        mFarmLocation = findViewById(R.id.farm_location);
        mPWD = findViewById(R.id.showHideBtn);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }


        mPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                global.hideKeyboard();

                if(mPWD.getText().equals("Hide"))
                {
                    mPWD.setText("Show");
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else if(mPWD.getText().equals("Show"))
                {
                    mPWD.setText("Hide");
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        select_crop = (Spinner) findViewById(R.id.select_crop);
        // Initializing a String Array
        String[] list_crop = new String[]{
                "Select Crop",
                "Tomato",
                "Onion",
                "Potato"
        };

        final List<String> cropList = new ArrayList<>(Arrays.asList(list_crop));

        {
            // Initializing an ArrayAdapter
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,list_crop){
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
            select_crop.setAdapter(spinnerArrayAdapter);

            select_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        };

        select_city  = (Spinner) findViewById(R.id.select_city);
        // Initializing a String Array
        String[] list_city = new String[]{
                "Select City",
                "Kolhapur",
                "Sangli",
                "Satara"
        };

        final List<String> cityList = new ArrayList<>(Arrays.asList(list_city));

        {
            // Initializing an ArrayAdapter
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,list_city){
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
                                            @NonNull ViewGroup parent) {
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
            select_city.setAdapter(spinnerArrayAdapter);

            select_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        };

//        selected_city = this.select_city.getSelectedItem().toString();

        ////-------------------------------////

        //Select Market Spinner
        select_market = (Spinner) findViewById(R.id.select_market);
        // Initializing a String Array
        String[] list_market = new String[]{
                "Select Market",
                "Kolhapur",
                "Sangli",
                "Satara"
        };

        final List<String> marketList = new ArrayList<>(Arrays.asList(list_market));

        {
            // Initializing an ArrayAdapter
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,list_market){
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
            select_market.setAdapter(spinnerArrayAdapter);

            select_market.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        };

        select_soil = (Spinner) findViewById(R.id.select_soil);
        // Initializing a String Array
        String[] list_soil = new String[]{
                "Select Soil",
                "Alluvial",
                "Regur",
                "Red",
                "Laterite",
                "Desert",
                "Mountain"
        };

        final List<String> soilList = new ArrayList<>(Arrays.asList(list_soil));

        {
            // Initializing an ArrayAdapter
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,list_soil){
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
            select_soil.setAdapter(spinnerArrayAdapter);

            select_soil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        };

//        selected_soil = this.select_soil.getSelectedItem().toString();




        View parent_view = findViewById(android.R.id.content);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName   = mFullName.getText().toString();
                final String email      = mEmail.getText().toString().trim();
                final String phone      = mPhone.getText().toString();
                final String farmArea  = mFarmArea.getText().toString();
                final String farmLocation = mFarmLocation.getText().toString();
                String password = mPassword.getText().toString().trim();

                final  String market_Name = select_market.getSelectedItem().toString();
                final  String city_Name = select_city.getSelectedItem().toString();
                final  String crop_Name = select_crop.getSelectedItem().toString();
                final  String soilName = select_soil.getSelectedItem().toString();

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

                //Date And Time
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String RegistrationDate = df.format(date);

                if(fullName.isEmpty()){
                    mFullName.setError("Full Name is Required");
                    return;
                }

                if(fullName.matches(".*\\d.*"))
                {
                    mFullName.setError("Full Name contains invalid data");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(!TextUtils.isEmpty(email)) {
                    EditText email1 = (EditText) findViewById(R.id.email);
                    if (!Patterns.EMAIL_ADDRESS.matcher(email1.getText().toString()).matches()) {
                        mEmail.setError("Invalid Email Address");
                        return;
                    }
                }


                if(phone.isEmpty()){
                    mPhone.setError("Mobile Number is Required");
                    return;
                }

                if(!TextUtils.isEmpty(phone)) {
                    EditText phone1 = (EditText) findViewById(R.id.phone);
                    if (!Patterns.PHONE.matcher(phone1.getText().toString()).matches()) {
                        mPhone.setError("Invalid Mobile Number");
                        return;
                    }
                }

                if(farmArea.isEmpty()){
                    mFarmArea.setError("Farm Area is Required");
                    return;
                }

                if(farmLocation.isEmpty()){
                    mFarmLocation.setError("Farm Location is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must contains minimum 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                // register the user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();

                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("city",city_Name);

                            user.put("marketName",market_Name);
                            user.put("cropName",crop_Name);
                            user.put("soilName",soilName);
                            user.put("farmArea",farmArea);
                            user.put("farmLocation",farmLocation);

                            //Date And Time
                            user.put("RegistrationDate",RegistrationDate);
                            user.put("ProfileID",userID);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            Intent i = new Intent(getApplicationContext(),HomePage.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("EXIT", true);
                            startActivity(i);
                            finish();


                        }else {
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        //Go to Login Page
        mLoginBtn = findViewById(R.id.GotoLogin);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


    }
}