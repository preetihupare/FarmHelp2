package com.ynot.farmhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFarm extends AppCompatActivity {

    public static final String TAG = "TAG";
    private static final int MY_REQUEST_CODE = 1;
    EditText F_Name, M_Number, E_Add, F_Area, F_Location;
    Spinner F_City, F_Crop, F_Market, F_Soil;
    Button saveBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;
    private View parent_view;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);

//        // Here we are initialising the progress dialog box
        dialog = new ProgressDialog(this);

        parent_view = findViewById(android.R.id.content);
        Intent data = getIntent();
        final String fName = data.getStringExtra("fName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");
        String area = data.getStringExtra("farmArea");
        String location = data.getStringExtra("farmLocation");
        String crop = data.getStringExtra("cropName");
        String city = data.getStringExtra("city");
        String market = data.getStringExtra("marketName");
        String soil = data.getStringExtra("soilName");


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        F_Name = findViewById(R.id.farmer_name);
        M_Number = findViewById(R.id.mobile_number);
        E_Add = findViewById(R.id.email_address);
        F_Area = findViewById(R.id.farm_area);
        F_Location = findViewById(R.id.farm_location);

        F_Crop = (Spinner) findViewById(R.id.m_crop);
        F_City = (Spinner) findViewById(R.id.m_city);
        F_Market = (Spinner) findViewById(R.id.m_market);
        F_Soil = (Spinner) findViewById(R.id.m_soil);

//      FirebaseArrayList
        String cropValue = crop;
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.list_crop, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        F_Crop.setAdapter(adapter1);
        if (cropValue != null) {
            int spinnerPosition = adapter1.getPosition(cropValue);
            F_Crop.setSelection(spinnerPosition);
        }

        String cityValue = city;
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.list_city, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        F_City.setAdapter(adapter2);
        if (cityValue != null) {
            int spinnerPosition = adapter2.getPosition(cityValue);
            F_City.setSelection(spinnerPosition);
        }

        String marketValue = market;
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.list_market, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        F_Market.setAdapter(adapter3);
        if (marketValue != null) {
            int spinnerPosition = adapter3.getPosition(marketValue);
            F_Market.setSelection(spinnerPosition);
        }

        String soilValue = soil;
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.list_soil, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        F_Soil.setAdapter(adapter4);
        if (soilValue != null) {
            int spinnerPosition = adapter4.getPosition(soilValue);
            F_Soil.setSelection(spinnerPosition);
        }

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(F_Name.getText().toString().isEmpty() || E_Add.getText().toString().isEmpty() || M_Number.getText().toString().isEmpty() || F_Area.getText().toString().isEmpty() || F_Location.getText().toString().isEmpty()){
                    Snackbar.make(parent_view, "One or many fields are empty.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final String email = E_Add.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final ProgressDialog progress = new ProgressDialog(MyFarm.this);

                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("fName",F_Name.getText().toString());
                        edited.put("phone",M_Number.getText().toString());
                        edited.put("farmArea",F_Area.getText().toString());
                        edited.put("farmLocation",F_Location.getText().toString());
                        edited.put("cropName", F_Crop.getSelectedItem().toString());
                        edited.put("city", F_City.getSelectedItem().toString());
                        edited.put("marketName", F_Market.getSelectedItem().toString());
                        edited.put("soilName", F_Soil.getSelectedItem().toString());

                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                        progress.setTitle("Profile Data Updating...");
                        progress.setMessage("Please wait..");
                        progress.show();

                        Runnable progressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                progress.cancel();
                                Snackbar.make(parent_view, "Profile Data Updated Successfully", Snackbar.LENGTH_SHORT).show();
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 3000);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyFarm.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        E_Add.setText(email);
        F_Name.setText(fName);
        M_Number.setText(phone);
        F_Area.setText(area);
        F_Location.setText(location);
        F_Crop.setSelection(adapter1.getPosition(cropValue));
        F_City.setSelection(adapter2.getPosition(cityValue));
        F_Market.setSelection(adapter3.getPosition(marketValue));
        F_Soil.setSelection(adapter4.getPosition(soilValue));


        Log.d(TAG, "onCreate: " + fName + " " + email + " " + phone+ " " + area + " " + location + " "+ adapter1 + " " + adapter2 + " " + adapter3 + " " + adapter4);

    }
}