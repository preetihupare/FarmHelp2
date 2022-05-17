package com.ynot.farmhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

    private View parent_view;

    EditText mFullName, mEmail, mPassword, mPhone, mFarmArea, mFarmLocation;
    Button mRegisterBtn;
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
        mPassword   = findViewById(R.id.password);
        mFarmArea   = findViewById(R.id.area);
        mFarmLocation = findViewById(R.id.farm_location);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        //Select Crop Spinner
        select_crop = (Spinner) findViewById(R.id.select_crop);
        List<String> list_crop = new ArrayList<String>();
        list_crop.add("Select Crop");
        list_crop.add("Tomato");
        list_crop.add("Onion");
        list_crop.add("Potato");

        ArrayAdapter<String> arrayCrop = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_crop);
        arrayCrop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_crop.setAdapter(arrayCrop);
        select_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                select_crop.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selected_crop = select_crop.getSelectedItem().toString();

        //Select City Spinner
        select_city  = (Spinner) findViewById(R.id.select_city);
        List<String> list_city = new ArrayList<String>();
        list_city.add("Select City");
        list_city.add("Kolhapur");
        list_city.add("Sangli");
        list_city.add("Satara");
        ArrayAdapter<String> arrayCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_city);
        arrayCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_city.setAdapter(arrayCity);
        select_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                select_city.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selected_city = this.select_city.getSelectedItem().toString();

        //Select Market Spinner
        select_market = (Spinner) findViewById(R.id.select_market);
        List<String> list_market = new ArrayList<String>();
        list_market.add("Select Market");
        list_market.add("Kolhapur");
        list_market.add("Sangli");
        list_market.add("Satara");
        ArrayAdapter<String> arrayMarket = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_market);
        arrayMarket.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_market.setAdapter(arrayMarket);
        select_market.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                select_market.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selected_market = this.select_market.getSelectedItem().toString();

        //Select Soil
        select_soil = (Spinner) findViewById(R.id.select_soil);
        List<String> list_soil = new ArrayList<String>();
        list_soil.add("Select Soil");
        list_soil.add("Alluvial Soil");
        list_soil.add("Regur or Black Soil");
        list_soil.add("Red Soil");
        list_soil.add("Laterite Soil");
        list_soil.add("Desert Soil");
        list_soil.add("Mountain Soil");

        ArrayAdapter<String> arraySoil = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_soil);
        arraySoil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_soil.setAdapter(arraySoil);
        select_soil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                select_soil.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selected_soil = this.select_soil.getSelectedItem().toString();

        parent_view = findViewById(android.R.id.content);
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


                //Date And Time
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String RegistrationDate = df.format(date);

                if(fullName.isEmpty()){
                    mFullName.setError("Full Name is Required");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!TextUtils.isEmpty(email)) {
                    if (!email.matches(emailPattern)) {
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }

                if(phone.isEmpty()){
                    mPhone.setError("Mobile Number is Required");
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